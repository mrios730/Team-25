package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Event;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that will initiate whenever a user goes to the event list page. */
@WebServlet("/event-list")
public class EventListServlet extends HttpServlet {
  private Datastore datastore;

  /** Initializes a new Datastore object. */
  @Override
  public void init() {
    datastore = new Datastore();
  }

  /** Retrieve all events created. */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    String tag = request.getParameter("tags");

    List<Event> events = datastore.getAllEvents();
    if (tag != null) { 
      Iterator it = events.iterator();
      while (it.hasNext()) {
        Event e = (Event) it.next();
        if (!e.getDescription().contains(tag)){
          it.remove();
        }
      }
    }
    Gson gson = new Gson();
    String json = gson.toJson(events);
    response.getWriter().println(json);
  }
}
