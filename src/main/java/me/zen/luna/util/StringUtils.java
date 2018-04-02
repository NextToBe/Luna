package me.zen.luna.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

  public static String EMPTY_STRING = "";

  /**
   *
   * @param str str
   * @return true if the <code>str</code> is null or empty string
   */
  public static boolean isEmpty(String str) {
    return str == null || str.length() == 0;
  }

  public static boolean isNotEmpty(String str) {
    return !isEmpty(str);
  }


  /**
   *
   * @param str str
   * @return if <code>str</code> {@link #isEmpty} or it contains " "
   */
  public static boolean isBlank(String str) {
    int length;
    if (str == null || (length = str.length()) == 0) {
      return true;
    }
    for (int i = 0; i < length; i++) {
      if ((!Character.isWhitespace(str.charAt(i)))) {
        return false;
      }
    }
    return true;
  }


  public static boolean isNotBlank(String str) {
    return !isBlank(str);
  }


  /**
   *
   * @param str a,b,c
   * @param regex  the delimiting regular expression: ,
   * @return List[a, b, c]
   */
  public static List<String> split(String str, String regex) {
    return Arrays.asList(str.split(regex));
  }

  /**
   *
   * @param strs the strings to be joined : [a, b, c]
   * @param separator the delimiting string: ,
   * @return a,b,c
   */
  public static String join(Iterable<String> strs, String separator) {
    if (strs == null) {
      return EMPTY_STRING;
    }

    StringBuilder builder = new StringBuilder();
    Iterator<String> iterator = strs.iterator();
    if (iterator.hasNext()) {
      builder.append(iterator.next());
      while (iterator.hasNext()) {
        builder.append(separator);
        builder.append(iterator.next());
      }
    }

    return builder.toString();
  }


  /**
   *
   * @param camel aStringLikeThis
   * @param needUpperCase if we need to cast the case
   * @return a_string_like_this
   */
  public static String camelToUnderscore(String camel, boolean needUpperCase) {
    String underscore = camel.replaceAll("([A-Z]+)", "_$1");
    if (needUpperCase) {
      return underscore.toUpperCase();
    }

    return underscore.toLowerCase();
  }


  /**
   *
   * @param underscore a_string_like_this
   * @return aStringLikeThis
   */
  public static String underscoreToCamel(String underscore) {
    underscore = underscore.toLowerCase();
    Pattern pattern = Pattern.compile("_([a-z])");
    Matcher matcher = pattern.matcher(underscore);

    StringBuffer result = new StringBuffer();
    while (matcher.find()) {
      matcher.appendReplacement(result, matcher.group(1).toUpperCase());
    }
    matcher.appendTail(result);

    return result.toString();
  }

}
