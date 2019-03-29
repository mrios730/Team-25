package com.google.codeu.render;

/** Converts valid image URL to display image. */
public class ImageUrlMessageTransformer implements MessageTransformer {
  @Override
  public String transformText(String text) {
    // Matches URL of an image file, with an optional caption. For example:
    //     [the google logo] http://www.google.com/images/logo.png
    // Matched URLs must end with one of: .png, .jpg, .gif
    String regex = "(\\[([^\\]]+)\\]\\s?)?(https?://(\\S+\\.\\S+)+/(\\S+\\.?)+\\.(png|jpe?g|gif))";
    // Replaces the URL with the actual image. If a caption is given, it is printed below the image.
    String replacement = "<figure><img src=\"$3\" /> <figcaption>$2</figcaption></figure>";
    text = text.replaceAll(regex, replacement);
    return text;
  }
}