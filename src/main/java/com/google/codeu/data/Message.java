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

import java.util.UUID;

/** A single message posted by a user. */
public class Message {

  private UUID id;
  private String user;
  private String text;
  private long timestamp;
  private String recipient;
  private String imageUrl;

  /**
   * Constructs a new {@link Message} posted by {@code user} with {@code text} content and received
   * by {@code recipient}. Generates a random ID and uses the current system time for the creation
   * time.
   */
  public Message(String user, String text, String recipient) {
    this(UUID.randomUUID(), user, text, System.currentTimeMillis(), recipient, null);
  }

  public Message(UUID id, String user, String text, long timestamp, String recipient) {
    this(id, user, text, timestamp, recipient, null);
  }

  /** Constructs a new Message written by the user, if they uploaded an image. */
  public Message(UUID id, String user, String text, long timestamp, String recipient,
      String imageUrl) {
    this.id = id;
    this.user = user;
    this.text = text;
    this.timestamp = timestamp;
    this.recipient = recipient;
    this.imageUrl = imageUrl;
  }

  public UUID getId() {
    return id;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getUser() {
    return user;
  }

  public String getText() {
    return text;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public String getRecipient() {
    return recipient;
  }

  public void setText(String text) {
    this.text = text;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }
}
