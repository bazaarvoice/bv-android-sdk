package com.bazaarvoice.bvandroidsdk;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;

public class BVConversionEventTest extends BvEventPartTest {

  @Test
  public void toRawShouldHaveClassConversion() throws Exception {
    checkMapContains(BVEventKeys.Event.CLASS, BVEventValues.BVEventClass.PII_CONVERSION.toString());
  }

  @Test
  public void toRawNonPiiShouldHaveClassPIIConversion() throws Exception {
    Map<String, Object> subjectMapWithPii = getSubjectMapNonPii();
    BvTestUtil.checkMapContains(
        subjectMapWithPii,
        BVEventKeys.Event.CLASS,
        BVEventValues.BVEventClass.CONVERSION.toString());
  }

  @Test
  public void toRawShouldHaveTypeFromUser() throws Exception {
    checkMapContains(BVEventKeys.Event.TYPE, stubData.getBvConversionType());
  }

  @Test
  public void toRawNonPiiShouldHaveTypeFromUser() throws Exception {
    BvTestUtil.checkMapContains(
        getSubjectMapNonPii(),
        BVEventKeys.Event.TYPE,
        stubData.getBvConversionType());
  }

  @Test
  public void toRawShouldHaveValueFromUser() throws Exception {
    checkMapContains(BVEventKeys.NonCommerceConversionEvent.VALUE, stubData.getBvConversionValue());
  }

  @Test
  public void toRawNonPiiShouldHaveValueFromUser() throws Exception {
    BvTestUtil.checkMapContains(
        getSubjectMapNonPii(),
        BVEventKeys.NonCommerceConversionEvent.VALUE,
        stubData.getBvConversionValue());
  }

  @Test
  public void toRawShouldHaveLabelFromUser() throws Exception {
    checkMapContains(BVEventKeys.NonCommerceConversionEvent.LABEL, stubData.getBvConversionLabel());
  }

  @Test
  public void toRawNonPiiShouldHaveLabelFromUser() throws Exception {
    BvTestUtil.checkMapContains(
        getSubjectMapNonPii(),
        BVEventKeys.NonCommerceConversionEvent.LABEL,
        stubData.getBvConversionLabel());
  }

  @Test
  public void labelShouldBeOptional() {
    new BVConversionEvent(stubData.getBvConversionType(), stubData.getBvConversionValue(), null);
  }

  @Test
  public void toRawShouldHaveNonTrackingAdId() throws Exception {
    checkMapContains(BVEventKeys.MobileEvent.ADVERTISING_ID, BVEventValues.NONTRACKING_TOKEN);
  }

  @Test
  public void toRawNonPiiShouldHaveActualAdId() throws Exception {
    BvTestUtil.checkMapContains(
        getSubjectMapNonPii(),
        BVEventKeys.MobileEvent.ADVERTISING_ID,
        stubData.getAdId());
  }

  @Test
  public void toRawShouldHavePiiParams() throws Exception {
    for (Map.Entry<String, Object> entry : stubData.getPiiParams().entrySet()) {
      checkMapContains(entry.getKey(), entry.getValue());
    }
  }

  @Test
  public void toRawNonPiiShouldNotHavePiiParams() throws Exception {
    Map<String, Object> subjectJsonNonPii = getSubjectMapNonPii();
    for (Map.Entry<String, Object> entry : stubData.getPiiParams().entrySet()) {
      assertFalse(subjectJsonNonPii.containsKey(entry.getKey()));
    }
  }

  @Test
  public void toRawShouldHaveHadPiiBoolField() throws Exception {
    checkMapContains(BVEventKeys.NonCommerceConversionEvent.HAD_PII, String.valueOf(true));
  }

  @Test
  public void toRawNonPiiShouldHaveHadPiiBoolField() throws Exception {
    BvTestUtil.checkMapContains(
        getSubjectMapNonPii(),
        BVEventKeys.NonCommerceConversionEvent.HAD_PII,
        String.valueOf(true));
  }

  @Override
  Map<String, Object> getSubjectMap() throws Exception {
    BVConversionEvent subject = stubData.getConversionEvent();
    subject.setAdditionalParams(stubData.getBothPiiAndNonPiiAdditionalParams());
    return subject.toRaw();
  }

  Map<String, Object> getSubjectMapNonPii() throws Exception {
    BVConversionEvent subject = stubData.getConversionEvent();
    subject.setAdditionalParams(stubData.getBothPiiAndNonPiiAdditionalParams());
    return subject.toRawNonPii();
  }
}
