package com.vividious.iot.lepetiteprince.sensor.model;


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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sensor {

  public enum Status {REGISTERED, NOT_REGISTERED, DISABLED}

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private String description;

  private String baseUrl;

  @Enumerated(EnumType.STRING)
  private Status status;

  private String macAddress;

  @ElementCollection
  @Enumerated(EnumType.STRING)
  private Set<Action> actions;
}
