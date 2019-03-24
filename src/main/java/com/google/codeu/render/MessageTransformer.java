package com.google.codeu.render;

/** Modifies a Message. */
public interface MessageTransformer {

  /** Returns a transformed message text. */
  public String transformText(String text);
}
