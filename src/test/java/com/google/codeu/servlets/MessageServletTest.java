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
        "This is the Google logo <figure><img src=\"http://www.google.com/images/logo.png\" /> <figcaption></figcaption></figure>!");
    runPrepareMessageForDisplayTest(
        "[This is the Google logo] http://www.google.com/images/logo.png!",
        "<figure><img src=\"http://www.google.com/images/logo.png\" /> <figcaption>This is the Google logo</figcaption></figure>!");
    runPrepareMessageForDisplayTest("", "");
    runPrepareMessageForDisplayTest("This is plain text.", "This is plain text.");
    runPrepareMessageForDisplayTest("Check out this cool art. [Link.] https://pbs.twimg.com/media/D1ux_clUkAABe_P.png",
        "Check out this cool art. <figure><img src=\"https://pbs.twimg.com/media/D1ux_clUkAABe_P.png\" /> <figcaption>Link.</figcaption></figure>");
    runPrepareMessageForDisplayTest("[Fake caption 1.] [Peanut butter.] https://interfaithsanctuary.org/wp-content/uploads/2017/12/peanut-butter.jpg",
        "[Fake caption 1.] <figure><img src=\"https://interfaithsanctuary.org/wp-content/uploads/2017/12/peanut-butter.jpg\" /> <figcaption>Peanut butter.</figcaption></figure>");
    runPrepareMessageForDisplayTest("[Fake caption.] [Peanut butter.] https://interfaithsanctuary.org/wp-content/uploads/2017/12/peanut-butter.jpg "
        + "https://pbs.twimg.com/media/D1ux_clUkAABe_P.png", "[Fake caption.] <figure><img src=\"https://interfaithsanctuary.org/wp-content/uploads/2017/12/peanut-butter.jpg\" /> <figcaption>Peanut butter.</figcaption></figure>"
        + " <figure><img src=\"https://pbs.twimg.com/media/D1ux_clUkAABe_P.png\" /> <figcaption></figcaption></figure>");
  }
}
