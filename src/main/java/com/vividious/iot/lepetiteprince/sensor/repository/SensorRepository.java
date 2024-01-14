package com.vividious.iot.lepetiteprince.sensor.repository;

import com.vividious.iot.lepetiteprince.sensor.model.Sensor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {

}
