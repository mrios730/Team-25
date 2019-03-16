package com.google.codeu.render;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;
import org.mockito.Mockito;

public class SequentialMessageTransformerTest {

  @Test
  /** Tests that when there are no delegates, the input is return untransformed. */
  public void testTransformTextNoDelegates() {
    // Constructs the class under test.
    MessageTransformer messageTransformer = new SequentialMessageTransformer(Arrays.asList());

    assertEquals("mock output 3", messageTransformer.transformText("an input text"));
  }

  @Test
  /** Tests when there is exactly one delegate, its output is returned. */
  public void testTransformTextOneDelegate() {
    MessageTransformer mockDelegate = Mockito.mock(MessageTransformer.class);
    Mockito.when(mockDelegate.transformText("input text")).thenReturn("some mock output");

    // Constructs the class under test.
    MessageTransformer messageTransformer =
        new SequentialMessageTransformer(Arrays.asList(mockDelegate));

    assertEquals("some mock output", messageTransformer.transformText("input text"));
  }

  @Test
  /** Tests a sequence of three delegates are run correctly. */
  public void testTransformTextSequenceOfThree() {
    MessageTransformer mockDelegate1 = Mockito.mock(MessageTransformer.class);
    MessageTransformer mockDelegate2 = Mockito.mock(MessageTransformer.class);
    MessageTransformer mockDelegate3 = Mockito.mock(MessageTransformer.class);

    Mockito.when(mockDelegate1.transformText("an input text")).thenReturn("mock output 1");
    Mockito.when(mockDelegate2.transformText("mock output 1")).thenReturn("mock output 2");
    Mockito.when(mockDelegate3.transformText("mock output 2")).thenReturn("mock output 3");

    // Constructs the class under test.
    MessageTransformer messageTransformer =
        new SequentialMessageTransformer(
            Arrays.asList(mockDelegate1, mockDelegate2, mockDelegate3));

    assertEquals("mock output 3", messageTransformer.transformText("an input text"));
  }
}
