package com.bazaarvoice.bvandroidsdk;

public enum SubmissionErrorCode {
  ERROR_FORM_DUPLICATE("ERROR_FORM_DUPLICATE"),
  ERROR_FORM_DUPLICATE_NICKNAME("ERROR_FORM_DUPLICATE_NICKNAME"),
  ERROR_FORM_INVALID("ERROR_FORM_INVALID"),
  ERROR_FORM_INVALID_EMAILADDRESS("ERROR_FORM_INVALID_EMAILADDRESS"),
  ERROR_FORM_INVALID_IPADDRESS("ERROR_FORM_INVALID_IPADDRESS"),
  ERROR_FORM_INVALID_OPTION("ERROR_FORM_INVALID_OPTION"),
  ERROR_FORM_PATTERN_MISMATCH("ERROR_FORM_PATTERN_MISMATCH"),
  ERROR_FORM_PROFANITY("ERROR_FORM_PROFANITY"),
  ERROR_FORM_REJECTED("ERROR_FORM_REJECTED"),
  ERROR_FORM_REQUIRED("ERROR_FORM_REQUIRED"),
  ERROR_FORM_REQUIRED_EITHER("ERROR_FORM_REQUIRED_EITHER"),
  ERROR_FORM_REQUIRED_NICKNAME("ERROR_FORM_REQUIRED_NICKNAME"),
  ERROR_FORM_REQUIRES_TRUE("ERROR_FORM_REQUIRES_TRUE"),
  ERROR_FORM_RESTRICTED("ERROR_FORM_RESTRICTED"),
  ERROR_FORM_STORAGE_PROVIDER_FAILED("ERROR_FORM_STORAGE_PROVIDER_FAILED"),
  ERROR_FORM_SUBMITTED_NICKNAME("ERROR_FORM_SUBMITTED_NICKNAME"),
  ERROR_FORM_TOO_FEW("ERROR_FORM_TOO_FEW"),
  ERROR_FORM_TOO_HIGH("ERROR_FORM_TOO_HIGH"),
  ERROR_FORM_TOO_LONG("ERROR_FORM_TOO_LONG"),
  ERROR_FORM_TOO_LOW("ERROR_FORM_TOO_LOW"),
  ERROR_FORM_TOO_SHORT("ERROR_FORM_TOO_SHORT"),
  ERROR_FORM_UPLOAD_IO("ERROR_FORM_UPLOAD_IO"),
  ERROR_PARAM_DUPLICATE_SUBMISSION("ERROR_PARAM_DUPLICATE_SUBMISSION"),
  ERROR_PARAM_INVALID_SUBJECT_ID("ERROR_PARAM_INVALID_SUBJECT_ID"),
  ERROR_PARAM_MISSING_SUBJECT_ID("ERROR_PARAM_MISSING_SUBJECT_ID"),
  ERROR_UNKNOWN("ERROR_UNKNOWN");

  private final String code;

  SubmissionErrorCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
