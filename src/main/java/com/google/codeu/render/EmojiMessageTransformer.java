package com.google.codeu.render;

/** Converts ascii emoticons into emoji. */
public class EmojiMessageTransformer implements MessageTransformer {

  @Override
  public String transformText(String text) {
    text = text.replaceAll(":\\)", "😀");
    text = text.replaceAll(":-\\)", "😀");
    text = text.replaceAll(":D", "😁");
    text = text.replaceAll(":-D", "😁");
    return text;
  }
}
