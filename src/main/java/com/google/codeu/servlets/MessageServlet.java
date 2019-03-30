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

package com.google.codeu.servlets;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.codeu.render.EmojiMessageTransformer;
import com.google.codeu.render.MessageTransformer;
import com.google.codeu.render.SequentialMessageTransformer;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;

/** Handles fetching and saving {@link Message} instances. */
@WebServlet("/messages")
public class MessageServlet extends HttpServlet {

  private Datastore datastore;
  private MessageTransformer messageTransformer;

  @Override
  public void init() {
    datastore = new Datastore();
    messageTransformer =
        new SequentialMessageTransformer(Arrays.asList(new EmojiMessageTransformer()));
  }

  public void setDatastore(Datastore datastore) {
    this.datastore = datastore;
  }

  public void setMessageTransformer(MessageTransformer messageTransformer) {
    this.messageTransformer = messageTransformer;
  }

  /**
   * Replaces messages with image links in order to display images properly. TODO: Migrate the code
   * here to MessageTransformer implementations and delete this method.
   */
  public void prepareMessageForDisplay(Message message) {
    // Matches URL of an image file, with an optional caption. For example:
    //     [the google logo] http://www.google.com/images/logo.png
    // Matched URLs must end with one of: .png, .jpg, .gif
    String regex = "(\\[([^\\]]+)\\]\\s?)?(https?://(\\S+\\.\\S+)+/(\\S+\\.?)+\\.(png|jpe?g|gif))";
    // Replaces the URL with the actual image. If a caption is given, it is printed below the image.
    String replacement = "<figure><img src=\"$3\" /> <figcaption>$2</figcaption></figure>";
    String text = message.getText();
    text = text.replaceAll(regex, replacement);
    // makes text bold
    text = text.replace("[b]", "<strong>").replace("[/b]", "</strong>");
    // makes text italic
    text = text.replace("[i]", "<i>").replace("[/i]", "</i>");
    // underlines text
    text = text.replace("[u]", "<ins>").replace("[/u]", "</ins>");
    // creates a strikethrough on text
    text = text.replace("[s]", "<del>").replace("[/s]", "</del>");
    message.setText(text);
  }

  /**
   * Responds with a JSON representation of {@link Message} data for a specific user. Responds with
   * an empty array if the user is not provided.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    response.setContentType("application/json");

    String user = request.getParameter("user");

    if (StringUtils.isEmpty(user)) {
      // Request is invalid, return empty array
      response.getWriter().println("[]");
      return;
    }

    List<Message> messages = datastore.getMessagesByRecipient(user);

    for (Message m : messages) {
      m.setText(messageTransformer.transformText(m.getText()));

      // TODO: Remove this when prepareMessageForDisplay is deleted.
      prepareMessageForDisplay(m);
    }
    Gson gson = new Gson();
    String json = gson.toJson(messages);

    response.getWriter().println(json);
  }

  /** Stores a new {@link Message}. */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    UserService userService = UserServiceFactory.getUserService();
    if (!userService.isUserLoggedIn()) {
      response.sendRedirect("/index.html");
      return;
    }

    String user = userService.getCurrentUser().getEmail();
    String text = request.getParameter("text");
    String recipient = request.getParameter("recipient");

    Message message = new Message(user, text, recipient);
    storeImage(request, message);
    datastore.storeMessage(message);

    response.sendRedirect("/user-page.html?user=" + recipient);
  }

  /** Stores an image file if user has uploaded one. */
  private void storeImage(HttpServletRequest request, Message message) {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
    List<BlobKey> blobKeys = blobs.get("image");
    if (blobKeys != null && !blobKeys.isEmpty()) {
      BlobKey blobKey = blobKeys.get(0);
      ImagesService imagesService = ImagesServiceFactory.getImagesService();
      ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(blobKey);
      String imageUrl = imagesService.getServingUrl(options);
      message.setImageUrl(imageUrl);
    }
  }
}
