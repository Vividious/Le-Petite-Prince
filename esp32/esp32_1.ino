
#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>
#include <map>

const char* WIFI_SSID = "";
const char* WIFI_PASSWORD = "";

const char* MQTT_SERVER_ADDRESS = "192.168.0.188";

const String CENTRAL_CONTROL_DEVICE_NAME = "vividious-iot";

const String THIS_DEVICE_NAME = "esp32_1";

const String TOPIC_BASE = CENTRAL_CONTROL_DEVICE_NAME + "/" + THIS_DEVICE_NAME;

const String LED_SENSOR_NAME = "red_led";

const String SOIL_MOISTURE_SENSOR_NAME = "moisture_sensor";

WiFiClient espClient;
PubSubClient client(espClient);
const String HANDSHAKE_TOPIC = TOPIC_BASE + "/howdy";
const String SOIL_MOISTURE_MEASUREMENT_TOPIC = TOPIC_BASE + "/measurement/soil_moisture";

bool handshake_sent = false;


// ACTIONS
String ACTION_TURN_OFF = "TURN_OFF";
String ACTION_TURN_ON = "TURN_ON";
String ACTION_MEASURE = "MEASURE";


// Timers
unsigned long soilSensorMeasurementInterval = 10000;
unsigned long previousMillis = 0;

void setup() {
  Serial.begin(115200);
  pinMode(18, OUTPUT);

  setup_wifi();

  client.setServer(MQTT_SERVER_ADDRESS, 1883);
  client.setCallback(callback);
}

void setup_wifi() {

  while (!Serial)
    ;
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(WIFI_SSID);

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}

bool isRedLedSensor(const char* sensorNameFromMessage) {
  return strcmp(sensorNameFromMessage, LED_SENSOR_NAME.c_str()) == 0;
}

bool isSoilMoistureSensor(const char* sensorNameFromMessage) {
  return strcmp(sensorNameFromMessage, SOIL_MOISTURE_SENSOR_NAME.c_str()) == 0;
}

std::map<String, String> parseSettingsMap(String message) {
  StaticJsonDocument<512> doc;
  deserializeJson(doc, message);
  std::map<String, String> settingsMap;

  JsonObject settings = doc["settings"];

  for (JsonObject::iterator it = settings.begin(); it != settings.end(); ++it) {
    settingsMap[it->key().c_str()] = it->value().as<String>();
  }
  return settingsMap;
}

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String message = String((char*)payload);

  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    message += (char)payload[i];
  }
  Serial.println();


  if (String(topic) == CENTRAL_CONTROL_DEVICE_NAME + "/sensors_assemble") {
    sendHandshakes();
  } else if (String(topic) == TOPIC_BASE + "/actions/TURN_ON") {
    const char* sensorName = parseSensorNameFromPayload(payload, length);

    if (isRedLedSensor(sensorName)) {
      digitalWrite(18, HIGH);
    }

  } else if (String(topic) == TOPIC_BASE + "/actions/TURN_OFF") {
    const char* sensorName = parseSensorNameFromPayload(payload, length);

    if (isRedLedSensor(sensorName)) {
      digitalWrite(18, LOW);
    }
  } else if (String(topic) == TOPIC_BASE + "/actions/MEASURE") {
    const char* sensorName = parseSensorNameFromPayload(payload, length);
    std::map<String, String> settings = parseSettingsMap(message);


    if (isSoilMoistureSensor(sensorName)) {
      String intervalStr = settings["INTERVAL"];
      soilSensorMeasurementInterval = intervalStr.toInt();
    }
  }
}



const char* parseSensorNameFromPayload(byte* payload, unsigned int length) {
  StaticJsonDocument<256> doc;
  deserializeJson(doc, payload, length);
  const char* sensorName = doc["sensorName"];
  return sensorName;
}



void reconnect() {
  // Loop until we're reconnected
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    // Attempt to connect
    if (client.connect("ESP32_1", "vividious", "devTest1234")) {
      Serial.println("connected");
      if (!handshake_sent) {
        sendHandshakes();
      }
      String actionsTopic = TOPIC_BASE + "/actions/+";
      String controlDeviceResponseTopic = CENTRAL_CONTROL_DEVICE_NAME + "/good_to_see_you/+";
      String sensorsAssembleTopic = TOPIC_BASE + "/sensors_assemble";

      client.subscribe(actionsTopic.c_str());
      client.subscribe(controlDeviceResponseTopic.c_str());
      client.subscribe(sensorsAssembleTopic.c_str());
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void sendHandshake(const String sensorName, const std::initializer_list<const String> actions, const std::map<String, String>& settings) {
  StaticJsonDocument<256> sensorJson;
  sensorJson["parentDeviceName"] = THIS_DEVICE_NAME;
  sensorJson["sensorName"] = sensorName;

  JsonArray availableActions = sensorJson.createNestedArray("availableActions");
  for (const String action : actions) {
    availableActions.add(action);
  }

  JsonObject settingsObject = sensorJson.createNestedObject("settings");
  for (const auto& kv : settings) {
    settingsObject[kv.first] = kv.second;
  }

  uint8_t buffer[256];
  unsigned int n = serializeJson(sensorJson, buffer);

  client.publish(HANDSHAKE_TOPIC.c_str(), buffer, n, false);
}

void sendHandshake(const String sensorName, const std::initializer_list<const String> actions) {
  std::map<String, String> emptySettings;
  sendHandshake(sensorName, actions, emptySettings);
}

void redLedHandshake() {
  sendHandshake(LED_SENSOR_NAME, { ACTION_TURN_ON, ACTION_TURN_OFF });
}

void soilSensorHandshake() {
  std::map<String, String> soilSensorSettings = {
    { "measurementName", "soil_moisture" }, { "dryValue", "2815" }, { "wetValue", "1280" }
  };
  sendHandshake(SOIL_MOISTURE_SENSOR_NAME, { ACTION_MEASURE }, soilSensorSettings);
}

void sendHandshakes() {
  redLedHandshake();
  soilSensorHandshake();
}


void loop() {
  unsigned long currentMillis = millis();

  if (!client.connected()) {
    reconnect();
  }

  client.loop();

  if (currentMillis - previousMillis >= soilSensorMeasurementInterval) {
    StaticJsonDocument<256> sensorJson;
    sensorJson["parentDeviceName"] = THIS_DEVICE_NAME;
    sensorJson["sensorName"] = SOIL_MOISTURE_SENSOR_NAME;
    sensorJson["measurementValue"] = analogRead(33);
    sensorJson["measurementName"] = "soil_moisture";
    sensorJson["interval"] = soilSensorMeasurementInterval;

    uint8_t buffer[256];
    unsigned int n = serializeJson(sensorJson, buffer);

    client.publish(SOIL_MOISTURE_MEASUREMENT_TOPIC.c_str(), buffer, n, false);
    previousMillis = currentMillis;
  }
}
