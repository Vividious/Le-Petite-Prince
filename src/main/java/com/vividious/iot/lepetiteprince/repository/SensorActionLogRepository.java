package com.vividious.iot.lepetiteprince.repository;

import com.vividious.iot.lepetiteprince.model.SensorActionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorActionLogRepository extends JpaRepository<SensorActionLog, Long> {

}
