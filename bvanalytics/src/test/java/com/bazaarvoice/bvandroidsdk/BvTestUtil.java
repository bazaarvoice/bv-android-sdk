package com.bazaarvoice.bvandroidsdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BvTestUtil {

  public static void checkMapContains(Map<String, Object> map, String expectedKey, Object expectedValue) {
    assertTrue("Map should contain key: " + expectedKey, map.containsKey(expectedKey));
    assertEquals("Value of map.get(" + expectedKey + ") should be " + expectedValue.toString(),
        expectedValue, map.get(expectedKey));
  }

  public static void checkMapContains(Map<String, Object> map, String expectedKey, String expectedValue) {
    assertTrue("Map should contain key: " + expectedKey, map.containsKey(expectedKey));
    assertEquals("Value of map.get(" + expectedKey + ") should be " + expectedValue,
        expectedValue, map.get(expectedKey));
  }

  public static void checkJsonContains(JsonObject jsonObject, String expectedKey, JsonElement expectedValue) {
    assertTrue("JsonObject should contain key: " + expectedKey, jsonObject.has(expectedKey));
    assertEquals("Value of jsonObj.get(" + expectedKey + ") should be " + expectedValue.toString(),
        expectedValue, jsonObject.get(expectedKey));
  }

  public static void checkJsonContains(JsonObject jsonObject, String expectedKey, String expectedValue) {
    assertTrue("JsonObject should contain key: " + expectedKey, jsonObject.has(expectedKey));
    assertEquals("Value of jsonObj.get(" + expectedKey + ") should be " + expectedValue,
        expectedValue, jsonObject.get(expectedKey).getAsString());
  }
}
