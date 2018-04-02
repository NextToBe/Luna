package me.zen.luna.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class StringUtilTest {

  @Test
  public void testIsEmpty() {
    Assert.assertEquals(StringUtils.isEmpty(null), true);
    Assert.assertEquals(StringUtils.isEmpty(""), true);
    Assert.assertEquals(StringUtils.isEmpty("abc"), false);
  }


  @Test
  public void testIsBlank() {
    Assert.assertEquals(StringUtils.isBlank(null), true);
    Assert.assertEquals(StringUtils.isBlank(""), true);
    Assert.assertEquals(StringUtils.isEmpty("abc "), false);
    Assert.assertEquals(StringUtils.isBlank("abc"), false);
  }


  @Test
  public void testSplit() {
    String str = "a,b,c";
    Assert.assertArrayEquals(StringUtils.split(str, ",").toArray(), new String[] {"a", "b", "c"});
  }

  @Test
  public void testJoin() {
    List<String> strs = Arrays.asList("a", "b", "c");
    Assert.assertEquals(StringUtils.join(strs, ","), "a,b,c");
  }


  @Test
  public void testCamelToUnderscore() {
    Assert.assertEquals(StringUtils.camelToUnderscore("aCamelString", false), "a_camel_string");
  }


  @Test
  public void testUnderscoreToCamel() {
    Assert.assertEquals(StringUtils.underscoreToCamel("a_underscore_string"), "aUnderscoreString");
  }


}
