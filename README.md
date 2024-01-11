

## Postgresql scripts
TODO: prepare docker compose file

Start docker container
```shell
docker run -itd -e POSTGRES_USER=vividious -e POSTGRES_PASSWORD=devTest1234 -p 5432:5432 -v E:\docker_storage\postgres\data:/var/lib/postgresql/data --name the_fox postgres
```

## Mosquitto scripts

Start docker container
```shell
docker run -it -p 1883:1883 --name insect -v E:\docker_storage\mosquitto\mosquitto.conf:/mosquitto/config/mosquitto.conf -v E:\docker_storage\mosquitto\data:/mosquitto/data -v E:\docker_storage\mosquitto\logs:/mosquitto/log -v E:\docker_storage\mosquitto\passwd_file:/mosquitto/config/passwd_file eclipse-mosquitto 
```

Use shell on Mosquitto container as a dedicated user

```shell
docker exec -it  -u 1883 insect sh
```

generate password file with the following
```shell
mosquitto_passwd -c /mosquitto/passwd_file vividious
```


## mosquitto-clients

Installation

```shell
sudo apt install mosquitto-clients
```

Testing MQTT with Spring Boot Integration
```shell
mosquitto_pub -L mqtt://vividious:devTest1234@localhost/test -m 'hello MQTT'
```

Webclient with pretty UI
```shell
sudo docker run -d --name mqttx-web -p 80:80 emqx/mqttx-web
```