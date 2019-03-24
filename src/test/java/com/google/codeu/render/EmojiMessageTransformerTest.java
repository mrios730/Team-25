package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class EmojiMessageTransformerTest {

  private MessageTransformer messageTransformer;

  @Before
  public void setup() {
    messageTransformer = new EmojiMessageTransformer();
  }

  private void runTransformTextTest(String input, String expectedResult) {
    String actualResult = messageTransformer.transformText(input);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testTransformText() {
    runTransformTextTest("", "");
    runTransformTextTest(":-)", "😀");
    runTransformTextTest(
        "Checking that this smiley :) and this one :-D are transformed!",
        "Checking that this smiley 😀 and this one 😁 are transformed!");
    runTransformTextTest("This has no smiley faces.", "This has no smiley faces.");
  }
}
