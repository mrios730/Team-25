package com.google.codeu.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Message;
import com.google.gson.Gson;

/**
 * Handles fetching all messages for the public feed.
 */
@WebServlet("/feed")
public class MessageFeedServlet extends HttpServlet{

	private Datastore datastore;

	@Override
	public void init() {
		datastore = new Datastore();
	}

	/**
	 * Responds with a JSON representation of Message data for users already logged in.
	 * Otherwise, you are redirected to the homepage
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws IOException {

		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn()) {
			response.sendRedirect("/index.html");
    } else {
      response.setContentType("application/json");

      List<Message> messages = datastore.getAllMessages();
      Gson gson = new Gson();
      String json = gson.toJson(messages);

      response.getOutputStream().println(json);
		}
	}

}