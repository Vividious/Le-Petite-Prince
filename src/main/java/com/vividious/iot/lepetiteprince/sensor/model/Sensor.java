package com.vividious.iot.lepetiteprince.sensor.model;


import com.vividious.iot.lepetiteprince.sensor.event.Handshake;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Set;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Sensor {

  public enum Status {ACTIVE, INACTIVE, ERROR}

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private String description;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String parentDevice;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Set<Action> actions;

  public static Sensor createFrom(Handshake handshake, UUID existingId) {
    return Sensor.builder()
        .id(existingId)
        .name(handshake.sensorName())
        .actions(handshake.availableActions())
        .parentDevice(handshake.parentDeviceName())
        .build();
  }

  public boolean canActivate() {
    return actions.contains(Action.TURN_ON);
  }

  public boolean canDeactivate() {
    return actions.contains(Action.TURN_OFF);
  }
}
