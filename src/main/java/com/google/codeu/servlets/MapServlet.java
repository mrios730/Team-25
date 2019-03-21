package com.google.codeu.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.IOException;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns Starbucks locations data as a JSON array. */
@WebServlet("/starbucks-map")
public class MapServlet extends HttpServlet {

  JsonArray starbucksArray;

  @Override
  public void init() {
    starbucksArray = new JsonArray();
    Gson gson = new Gson();
    Scanner scanner =
        new Scanner(getServletContext().getResourceAsStream("/WEB-INF/starbucks-directory.csv"));
    // Skip the first line (Column names)
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      String[] cells = line.split(",");

      // System.out.println(cells[0] + ", " + cells[1] + ", " + cells[2]);

      String storeNumber = cells[0];
      double lat = Double.parseDouble(cells[2]);
      double lng = Double.parseDouble(cells[1]);

      starbucksArray.add(gson.toJsonTree(new Starbucks(storeNumber, lat, lng)));
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getOutputStream().println(starbucksArray.toString());
  }

  private static class Starbucks {
    String storeNumber;
    double lat;
    double lng;

    private Starbucks(String storeNumber, double lat, double lng) {
      this.storeNumber = storeNumber;
      this.lat = lat;
      this.lng = lng;
    }
  }
}
