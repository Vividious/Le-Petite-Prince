package com.vividious.iot.lepetiteprince.repository;

import com.vividious.iot.lepetiteprince.model.Action;
import com.vividious.iot.lepetiteprince.model.Sensor;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {

  @Query("SELECT s.id from Sensor s where s.name = :name")
  UUID findIdByName(@Param("name") String name);

  @Query("SELECT s from Sensor s where s.name = :name and :action member of s.actions")
  Sensor findSensorSupportingActionByName(@Param("name") String name, @Param("action") Action action);

  @Query("SELECT s.actions from Sensor s where s.name = :name")
  Set<Action> findSupportedActionsByName(@Param("name") String name);
}
