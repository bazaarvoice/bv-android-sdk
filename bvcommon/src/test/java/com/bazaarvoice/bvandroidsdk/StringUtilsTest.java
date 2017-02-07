package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class StringUtilsTest {
  @Test
  public void isEmpty() throws Exception {
    assertTrue(StringUtils.isEmpty(null));
    assertTrue(StringUtils.isEmpty(""));
    assertFalse(StringUtils.isEmpty("stuff"));
  }

  @Test
  public void componentsSeparatedBy() throws Exception {
    List stuff = new ArrayList();
    stuff.add(Boolean.valueOf(true));
    stuff.add(Float.valueOf(1.0f));
    String output = StringUtils.componentsSeparatedBy(stuff, ",");
    assertEquals("true,1.0", output);
  }

  @Test
  public void componentsSeparatedByWithEscapes() throws Exception {
    List stuff = new ArrayList();
    stuff.add("foo,foo");
    stuff.add("bar:bar");
    stuff.add("baz&baz");
    String output = StringUtils.componentsSeparatedByWithEscapes(stuff, ",");
    assertEquals("foo\\,foo,bar\\:bar,baz%26baz", output);
  }

}