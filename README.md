# Le Petit Prince

The application was created with the aim of exploring new technologies, including MQTT, Spring Integration, and the configuration and programming of microcontrollers. In my case, these were: Raspberry Pi 4 Model B and ESP32.

Mini IoT Setup
![MiniIotSetup](docs/pictures/esp32_circuit.jpeg)

## Introduction

Below, I present a small architecture of the entire IoT system. Currently, it allows the connection of sensors supporting actions: **TURN_ON**, **TURN_OFF**, and **MEASURE**.

![Architecture](docs/pictures/architecture_preview.png)

Under the path `/esp32/esp32_1.ino`, you can find my primitive C/C++ code. It can certainly be done better, but in this project, I wanted to focus on developing a high-level abstract application.

## Supported Actions

- TURN_ON - sends a message on the MQTT topic that triggers the activation of a specific sensor, for example, an LED light.
- TURN_OFF - sends a message on the MQTT topic that triggers the deactivation of a specific sensor, for example, an LED light.
- MEASURE - sends a message on the MQTT topic that triggers the sending of measurements at a specified interval to a dedicated MQTT topic for that particular measurement.

## Features
![FeaturesInUse](/docs/pictures/features_in_use.png)

## MQTT topics

The naming convention is as follows: `<generic_name>/<sensor_control_device>`

`<generic_name>` = `vividious_iot`

`<sensor_control_device>` = `esp32_1`

For simplicity, let **BASE** = `vividious_iot/esp32_1`

| topic                                          | desc                                                                                                                                                                                                                                                                          |
| ---------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `{BASE}/howdy`                                 | After booting up the device that controls the sensors, each sensor sends a handshake-greeting containing information such as: the name of the controlling device, the name of the sensor, and the set of supported actions as well as additional information for given action |
| `<generic_name>/good_to_see_you/<sensor_name>` | Contains a response to the handshake-greeting from a given sensor. For now, it is only the string "Hello" + sensorName. In the future, this will be a place for additional default sensor configuration.                                                                      |
| `{BASE}/measurement/{measurement_type}`        | Contains data from the performed measurements as well as key settings of the measuring device                                                                                                                                                                                 |
| `{BASE}/action/<ACTION_NAME>`                  | Commands to execute specific actions (MEASURE, TURN_ON, TURN_OFF, etc.) are sent to this topic.                                                                                                                                                                               |
| `{BASE}/sensors_assemble`                      | Just like the Avengers, the sensors unite and resend handshake-greeting messages after being triggered here                                                                                                                                                                   |

## Prerequisites

### Raspberry OS INSTALLATION

1. Download Imager from: https://www.raspberrypi.com/software/
2. Install Pi OS (64-bit)
3. EDIT SETTINGS!
4. General -> Setup Wi-Fi connection, locale and credentials
5. Services -> Enable ssh
6. Proceed with installation

### Raspberry Docker

Basically follow instroctions from docker's website: https://docs.docker.com/engine/install/debian/

To run docker-compose from `/raspberrypi/docker` path execute the following:

```
 docker compose up -d
```

### ESP32

1. Install required software: https://esp32io.com/tutorials/esp32-software-installization
2. Download pubsubclient library: https://github.com/knolleary/pubsubclient

## Testing MQTT

Testing with mosquitto CLI client

```shell
# Installation
sudo apt install mosquitto-clients
# Subscribing to given topic
mosquitto_sub -v -L mqtt://vividious:devTest1234@192.168.0.188/vividious-iot/#

# Publishing message to given topic
mosquitto_pub -L mqtt://vividious:devTest1234@192.168.0.188/vividious-iot/test -m 'hello MQTT'
```

Second option -> Webclient with pretty UI

```shell
sudo docker run -d --name mqttx-web -p 80:80 emqx/mqttx-web
```
