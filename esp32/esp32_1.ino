
#include <WiFi.h>
#include <PubSubClient.h>
#include <ArduinoJson.h>

const char* WIFI_SSID = "";
const char* WIFI_PASSWORD = "";

const char* MQTT_SERVER_ADDRESS = "192.168.0.188";

const String CENTRAL_CONTROL_DEVICE_NAME = "vividious-iot";

const String THIS_DEVICE_NAME = "esp32_1";

const String TOPIC_BASE = CENTRAL_CONTROL_DEVICE_NAME + "/" + THIS_DEVICE_NAME;

const String LED_SENSOR_NAME = "red_led";

WiFiClient espClient;
PubSubClient client(espClient);
const String handshakeTopic = TOPIC_BASE + "/howdy";

bool handshake_sent = false;



void setup() {
  pinMode(18, OUTPUT);

  setup_wifi();

  client.setServer(MQTT_SERVER_ADDRESS, 1883);
  client.setCallback(callback);
}

void setup_wifi() {
  Serial.begin(115200);
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

void callback(char* topic, byte* payload, unsigned int length) {
  Serial.print("Message arrived on topic: ");
  Serial.print(topic);
  Serial.print(". Message: ");
  String msg;

  for (int i = 0; i < length; i++) {
    Serial.print((char)payload[i]);
    msg += (char)payload[i];
  }
  Serial.println();

  if (String(topic) == TOPIC_BASE + "/actions/TURN_ON") {
    const char* sensorName = parseSensorNameFromPayload(payload, length);

    if (isRedLedSensor(sensorName)) {
      digitalWrite(18, HIGH);
    }

  } else if (String(topic) == TOPIC_BASE + "/actions/TURN_OFF") {
    const char* sensorName = parseSensorNameFromPayload(payload, length);

    if (isRedLedSensor(sensorName)) {
      digitalWrite(18, LOW);
    }
  }
}

bool isRedLedSensor(const char* sensorNameFromMessage) {
  return strcmp(sensorNameFromMessage, LED_SENSOR_NAME.c_str()) == 0;
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

      client.subscribe(actionsTopic.c_str());
      client.subscribe(controlDeviceResponseTopic.c_str());
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      // Wait 5 seconds before retrying
      delay(5000);
    }
  }
}

void sendHandshakes() {
  redLedHandshake();
}

void redLedHandshake() {
  StaticJsonDocument<256> doc;
  doc["parentDeviceName"] = THIS_DEVICE_NAME;
  doc["sensorName"] = LED_SENSOR_NAME;
  JsonArray availableActions = doc.createNestedArray("availableActions");
  availableActions.add("TURN_ON");
  availableActions.add("TURN_OFF");

  uint8_t buffer[256];
  unsigned int n = serializeJson(doc, buffer);

  client.publish(handshakeTopic.c_str(), buffer, n, true);
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }

  client.loop();
}
