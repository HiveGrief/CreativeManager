package fr.k0bus.creativemanager.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Utility class for text and list operations. */
public class TextUtils {

  /**
   * Replaces placeholders in a template with values from a map.
   *
   * @param template the template string.
   * @param values the placeholder values map.
   * @return the formatted string.
   */
  public static String replacePlaceholders(String template, Map<String, String> values) {
    Pattern pattern = Pattern.compile("\\{(\\w+)}");
    Matcher matcher = pattern.matcher(template);
    StringBuilder sb = new StringBuilder();

    while (matcher.find()) {
      String key = matcher.group(1);
      String replacement = values.getOrDefault(key, matcher.group(0));
      matcher.appendReplacement(sb, replacement);
    }
    matcher.appendTail(sb);
    return sb.toString();
  }

  /**
   * Converts a list of strings to lowercase.
   *
   * @param list the list of strings.
   * @return the lowercase list.
   */
  public static List<String> listToLowerCase(List<String> list) {
    List<String> lowerCaseList = new java.util.ArrayList<>();
    if (list != null) {
      for (String s : list) {
        if (s != null) {
          lowerCaseList.add(s.toLowerCase());
        }
      }
    }
    return lowerCaseList;
  }
}
