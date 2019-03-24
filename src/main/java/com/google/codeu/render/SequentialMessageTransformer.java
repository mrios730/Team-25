package com.google.codeu.render;

import java.util.List;

/**
 * Chains together multiple MessageTransformer implementations.
 *
 * <p>This is an example of the Composite Pattern https://en.wikipedia.org/wiki/Composite_pattern
 */
public class SequentialMessageTransformer implements MessageTransformer {

  private List<MessageTransformer> delegateMessageTransformers;

  public SequentialMessageTransformer(List<MessageTransformer> delegateMessageTransformers) {
    this.delegateMessageTransformers = delegateMessageTransformers;
  }

  @Override
  public String transformText(String text) {
    for (MessageTransformer messageTransformer : delegateMessageTransformers) {
      text = messageTransformer.transformText(text);
    }
    return text;
  }
}
