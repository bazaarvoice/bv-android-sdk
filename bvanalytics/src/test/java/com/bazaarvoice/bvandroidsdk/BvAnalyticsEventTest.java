package com.bazaarvoice.bvandroidsdk;

import org.json.JSONException;
import org.junit.Test;

import java.util.Map;

public class BvAnalyticsEventTest extends BvEventPartTest {
  @Test
  public void shouldAddAdditionalFieldsTopLevel() throws Exception {
    for (Map.Entry<String, Object> additionalParam : stubData.getAdditionalParams().entrySet()) {
      checkMapContains(additionalParam.getKey(), String.valueOf(additionalParam.getValue()));
    }
  }

  @Test
  public void shouldHaveUserAgent() throws Exception {
    checkMapContains(BVEventKeys.CommonAnalyticsParams.USER_AGENT, stubData.getUserAgent());
  }

  @Test
  public void shouldHaveIp() throws Exception {
    checkMapContains(BVEventKeys.CommonAnalyticsParams.HASHED_IP, stubData.getHashedIp());
  }

  @Override
  Map<String, Object> getSubjectMap() throws JSONException {
    final Map<String, Object> additionalParams = stubData.getAdditionalParams();
    final BVAnalyticsEvent subject = stubData.getSomeEvent(additionalParams);
    return subject.toRaw();
  }
}
