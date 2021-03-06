/*
 * Copyright 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.codeu.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/** Provides access to the data stored in Datastore. */
public class Datastore {

  private DatastoreService datastore;

  public Datastore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /** Stores the Message in Datastore. */
  public void storeMessage(Message message) {
    Entity messageEntity = new Entity("Message", message.getId().toString());
    messageEntity.setProperty("user", message.getUser());
    messageEntity.setProperty("text", message.getText());
    messageEntity.setProperty("timestamp", message.getTimestamp());
    messageEntity.setProperty("recipient", message.getRecipient());
    if (message.getImageUrl() != null) {
      messageEntity.setProperty("imageUrl", message.getImageUrl());
    }

    datastore.put(messageEntity);
  }

  /** Will retrieve every message ever sent. */
  public List<Message> getAllMessages() {
    Query query = new Query("Message").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    return getMessages(results);
  }

  /**
   * Gets messages received by a specific user.
   *
   * @return a list of messages received by the user, or empty list if user has never received a
   *     message. List is sorted by time descending.
   */
  public List<Message> getMessagesByRecipient(String recipient) {
    Query query =
        new Query("Message")
            .setFilter(new Query.FilterPredicate("recipient", FilterOperator.EQUAL, recipient))
            .addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    return getMessages(results);
  }

  /** Retrieve all messages given a query. */
  public List<Message> getMessages(PreparedQuery results) {
    List<Message> messages = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
      try {
        String idString = entity.getKey().getName();
        UUID id = UUID.fromString(idString);
        String user = (String) entity.getProperty("user");
        String text = (String) entity.getProperty("text");
        long timestamp = (long) entity.getProperty("timestamp");
        String recipient = (String) entity.getProperty("recipient");
        String imageUrl = (String) entity.getProperty("imageUrl");
        Message message = new Message(id, user, text, timestamp, recipient, imageUrl);
        messages.add(message);
      } catch (Exception e) {
        System.err.println("Error reading message.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return messages;
  }

  /** Returns the total number of messages for all users. */
  public int getTotalMessageCount() {
    Query query = new Query("Message");
    PreparedQuery results = datastore.prepare(query);
    return results.countEntities(FetchOptions.Builder.withLimit(1000));
  }

  /** Stores the User in Datastore. */
  public void storeUser(User user) {
    Entity userEntity = new Entity("User", user.getEmail());
    userEntity.setProperty("email", user.getEmail());
    userEntity.setProperty("aboutMe", user.getAboutMe());
    datastore.put(userEntity);
  }

  /** Returns the User owned by the email address, or null if no matching User was found. */
  public User getUser(String email) {
    Query query =
        new Query("User")
            .setFilter(new Query.FilterPredicate("email", FilterOperator.EQUAL, email));
    PreparedQuery results = datastore.prepare(query);
    Entity userEntity = results.asSingleEntity();
    if (userEntity == null) {
      return null;
    }

    String aboutMe = (String) userEntity.getProperty("aboutMe");
    User user = new User(email, aboutMe);

    return user;
  }

  /** Returns the total number of users. */
  public int getTotalUserCount() {
    Set<String> allUsers = new HashSet<String>();

    // Gets every user who has written an "about me" profile.
    Query userQuery = new Query("User");
    for (Entity entity : datastore.prepare(userQuery).asIterable()) {
      allUsers.add((String) entity.getProperty("email"));
    }

    // Gets every user who has sent a message.
    Query messageQuery = new Query("Message");
    for (Entity entity : datastore.prepare(messageQuery).asIterable()) {
      allUsers.add((String) entity.getProperty("user"));
    }

    return allUsers.size();
  }

  /** Retrieve markers that users have created on 'user map' page. */
  public List<UserMarker> getMarkers() {
    List<UserMarker> markers = new ArrayList<>();
    Query query = new Query("UserMarker");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        double lat = (double) entity.getProperty("lat");
        double lng = (double) entity.getProperty("lng");
        String content = (String) entity.getProperty("content");
        UserMarker marker = new UserMarker(lat, lng, content);
        markers.add(marker);
      } catch (Exception e) {
        System.err.println("Error reading marker.");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return markers;
  }

  /** Store any markers that users create on the 'User Map' page. */
  public void storeMarker(UserMarker marker) {
    Entity markerEntity = new Entity("UserMarker");
    markerEntity.setProperty("lat", marker.getLat());
    markerEntity.setProperty("lng", marker.getLng());
    markerEntity.setProperty("content", marker.getContent());
    datastore.put(markerEntity);
  }

  /** Retrieve all events created by users. */
  public List<Event> getAllEvents() {
    List<Event> events = new ArrayList<>();
    Query query = new Query("Event");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        String eventName = (String) entity.getProperty("eventName");
        String description = (String) entity.getProperty("description");
        String organizerName = (String) entity.getProperty("organizerNames");
        String date = (String) entity.getProperty("eventDate");
        String time = (String) entity.getProperty("eventTime");
        String location = (String) entity.getProperty("location");
        Event event = new Event(eventName, description, organizerName, date, time, location);
        events.add(event);
      } catch (Exception e) {
        System.err.println("Error retrieving event");
        System.err.println(entity.toString());
        e.printStackTrace();
      }
    }
    return events;
  }

  /** Stores Event in Datastore. */
  public void storeEvent(Event event) {
    Entity eventEntity = new Entity("Event", event.getId().toString());
    eventEntity.setProperty("eventName", event.getEventName());
    eventEntity.setProperty("organizerNames", event.getOrganizerNames());
    eventEntity.setProperty("eventDate", event.getEventDate());
    eventEntity.setProperty("eventTime", event.getEventTime());
    eventEntity.setProperty("location", event.getLocation());
    eventEntity.setProperty("description", event.getDescription());

    datastore.put(eventEntity);
  }
}
