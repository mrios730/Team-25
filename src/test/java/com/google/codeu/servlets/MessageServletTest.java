package com.google.codeu.servlets;

import static org.junit.Assert.assertEquals;

import com.google.codeu.data.Message;
import java.io.IOException;
import javax.servlet.ServletException;
import org.junit.Before;
import org.junit.Test;

public class MessageServletTest {

  private MessageServlet messageServlet;

  @Before
  public void setup() {
    messageServlet = new MessageServlet();
  }

  private void runPrepareMessageForDisplayTest(String messageText, String expectedResult) {
    Message message = new Message("user1", messageText, "recipient1");
    messageServlet.prepareMessageForDisplay(message);
    assertEquals(expectedResult, message.getText());
  }

  @Test
  public void testPrepareMessageForDisplay() throws IOException, ServletException {
    runPrepareMessageForDisplayTest(
        "This is the Google logo http://www.google.com/images/logo.png!",
        "This is the Google logo <img src=\"http://www.google.com/images/logo.png\" />!");
  }
}
