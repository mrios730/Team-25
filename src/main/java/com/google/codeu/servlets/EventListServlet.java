package com.google.codeu.servlets;

import com.google.codeu.data.Datastore;
import com.google.codeu.data.Event;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
      // Split query into separate words using a comma as a delimiter.
      String[] tags = tag.split(",");
      for (int i = 0; i < tags.length; i++) {
        String currTag = tags[i]; 
        Iterator it = events.iterator();
        while (it.hasNext()) {
          Event e = (Event) it.next();
          String description = e.getDescription();
          String regex = "#(\\w+)";

          // Retrieve all hashtags in the description
          Pattern p = Pattern.compile(regex);
          Matcher m = p.matcher(description);
          boolean hashtagExists = false;

          // Iterate through all hashtag terms and only remove
          // events that do not have the hashtag in its description.
          while (m.find()) {
            String hashtag = m.group(1);
            if (hashtag.substring(1).equals(currTag)) {
              hashtagExists = true;
              break;
            }
          }
          if (!hashtagExists) {
            it.remove();
          }
        }
      }
    }
    
    Gson gson = new Gson();
    String json = gson.toJson(events);
    response.getWriter().println(json);
  }
}
