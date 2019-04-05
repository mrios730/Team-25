/** Event class */

package com.google.codeu.data;

import java.util.UUID;

public class Event {
  private UUID id;
  private String eventName;
  private String description;
  private String organizerNames;
  private String eventDate;
  private String eventTime;
  private String location;

  public Event(String eventName, String description, String organizerNames, 
      String eventDate, String eventTime, String location) {
    this.eventName = eventName;
    this.description = description;
    this.organizerNames = organizerNames;
    this.eventDate = eventDate;
    this.eventTime = eventTime;
    this.location = location;
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public String getEventName() {
    return eventName;
  }

  public String getDescription() {
    return description;
  }

  public String getOrganizerNames() {
    return organizerNames;
  }

  public String getEventDate() {
    return eventDate;
  }

  public String getEventTime() {
    return eventTime;
  }

  public String getLocation() {
    return location;
  }
}
