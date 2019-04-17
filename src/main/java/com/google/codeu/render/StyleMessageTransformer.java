package com.google.codeu.render;

/** Replaces style tags into HTML markup. */
public class StyleMessageTransformer implements MessageTransformer {

  @Override
  public String transformText(String text) {
    text = text.replace("[b]", "<strong>").replace("[/b]", "</strong>");
    text = text.replace("[i]", "<i>").replace("[/i]", "</i>");
    text = text.replace("[u]", "<ins>").replace("[/u]", "</ins>");
    text = text.replace("[s]", "<del>").replace("[/s]", "</del>");
    return text;
  }
}
