package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StyleMessageTransformerTest {

  private MessageTransformer messageTransformer;

  @Before
  public void setup() {
    messageTransformer = new StyleMessageTransformer();
  }

  private void runTransformTextTest(String input, String expectedResult) {
    String actualResult = messageTransformer.transformText(input);
    assertEquals(expectedResult, actualResult);
  }

  @Test
  public void testTransformText() {
    runTransformTextTest("[b]", "<strong>");
    runTransformTextTest("", "");
    runTransformTextTest(
        "Checking that [i]this[/i] is italicized",
        "Checking that <i>this</i> is italicized");
    runTransformTextTest("This has no markup.", "This has no markup.");
  }
}
