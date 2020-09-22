package com.bazaarvoice.bvandroidsdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bazaarvoice.bvandroidsdk.internal.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Converts user created Request Objects into OkHttp Request Objects.
 * Each user created Request Object will only have the information that
 * the user entered. This Factory will manage adding anything else
 * required including Headers and Query Parameters, as well as taking
 * user inputted data, and formatting it correctly such as POST body
 * media content.
 */
class BasicRequestFactory implements RequestFactory {
    // region Keys
    // region Generic Request Keys
    private static final String kAPI_VERSION = "apiversion";
    private static final String kPASS_KEY = "passkey";
    private static final String kAPP_ID = "_appId";
    private static final String kAPP_VERSION = "_appVersion";
    private static final String kBUILD_NUM = "_buildNumber";
    private static final String kSDK_VERSION = "_bvAndroidSdkVersion";
    private static final String API_VERSION = "5.4";
    private static final String KEY_USER_AGENT = "User-Agent";
    // endregion

    // region Generic Display Request Keys
    private static final String kFILTER = "Filter";
    private static final String kSORT = "Sort";
    private static final String kSORT_REVIEW = "Sort_Reviews";
    private static final String kSORT_QUESTIONS = "Sort_Questions";
    private static final String kSORT_ANSWERS = "Sort_Answers";
    private static final String kINCLUDE = "Include";
    private static final String kSTATS = "Stats";
    private static final String kLIMIT = "Limit";
    private static final String kOFFSET = "Offset";
    private static final String kSEARCH = "Search";
    private static  final String kINCENTIVIZED_STATS = "incentivizedstats";
    // endregion

    // region Display Reviews Request Keys
    private static final String INCLUDE_ANSWERS = "Answers";
    private static final String REVIEWS_ENDPOINT = "data/reviews.json";
    private static final String KEY_STATS = "Stats";
    private static final String STATS_REVIEWS = "Reviews";
    // endregion

    // region Display Q&A Request Keys
    private static final String QUESTIONS_AND_ANSWERS_ENDPOINT = "data/questions.json";
    // endregion

    // region Display Q&A Request Keys
    private static final String REVIEW_HIGHLIGHTS_ENDPOINT = "highlights/v3/1";
    // endregion

    // region Display Statistics Request Keys
    private static final String STATS_ENDPOINT = "data/statistics.json";
    // endregion

    // region Display Comments Request Keys
    private static final String REVIEW_COMMENTS_ENDPOINT = "data/reviewcomments.json";
    // endregion

    // region Display Authors Request Keys
    private static final String AUTHORS_ENDPOINT = "data/authors.json";
    // endregion

    // region Display Products Request Keys
    private static final String PRODUCTS_ENDPOINT = "data/products.json";
    // endregion

    // region Generic Submit Request Keys
    private static final String kCAMPAIGN_ID = "campaignid";
    private static final String kFINGER_PRINT = "fp";
    private static final String kHOSTED_AUTH_EMAIL = "hostedauthentication_authenticationemail";
    private static final String kHOST_AUTH_CALLBACK = "hostedauthentication_callbackurl";
    private static final String kLOCALE = "locale";
    private static final String kUSER = "User";
    private static final String kUSER_EMAIL = "UserEmail";
    private static final String kUSER_LOCATION = "UserLocation";
    private static final String kUSER_NICKNAME = "UserNickname";
    private static final String kSEND_EMAIL_PUBLISHED = "sendemailalertwhenpublished";
    private static final String kAGREE_TERMS = "agreedToTermsAndConditions";
    private static final String kACTION = "action";
    private static final String KEY_PHOTO_URL_TEMPLATE = "photourl_%d";
    private static final String KEY_PHOTO_CAPTION_TEMPLATE = "photocaption_%d";
    private static final String kPRODUCT_ID = "ProductId";
    // endregion

    // region Submit Feedback Request Keys
    private static final String FEEDBACK_SUBMIT_ENDPOINT = "data/submitfeedback.json";
    private static final String kCONTENT_ID = "ContentId";
    private static final String kCONTENT_TYPE = "ContentType";
    private static final String kFEEDBACK_TYPE = "FeedbackType";
    private static final String kUSER_ID = "UserId";
    private static final String kVOTE = "Vote";
    private static final String kREASON_TEXT = "ReasonText";
    // endregion

    // region Submit Review Request Keys
    private static final String REVIEW_SUBMIT_ENDPOINT = "data/submitreview.json";
    private static final String KEY_ADDITIONAL_PARAM_TEMPLATE = "additionalfield_%s";
    private static final String kIS_RECOMMENDED = "IsRecommended";
    private static final String kSEND_EMAIL_COMMENTED = "SendEmailAlertWhenCommented";
    private static final String kRATING = "Rating";
    private static final String kNET_PROMOTER_SCORE = "NetPromoterScore";
    private static final String kTITLE = "Title";
    private static final String kREVIEW_TEXT = "ReviewText";
    private static final String kNET_PROMOTER_COMMENT = "NetPromoterComment";
    private static final String VIDEO_URL_TEMPLATE = "VideoUrl_%d";
    private static final String VIDEO_CAPTION_TEMPLATE = "VideoCaption_%d";
    private static final String KEY_RATING_TEMPLATE = "rating_%s";
    private static final String KEY_CDV_TEMPLATE = "contextdatavalue_%s";
    private static final String KEY_FREEFORM_TAG_TEMPLATE = "tag_%s_%d";
    // endregion

    // region Submit Question Request Keys
    private static final String QUESTION_SUBMIT_ENDPOINT = "data/submitquestion.json";
    private static final String kQUESTION_SUMMARY = "QuestionSummary";
    private static final String kQUESTION_DETAILS = "QuestionDetails";
    private static final String kIS_ANONUSER = "IsUserAnonymous";
    private static final String kSEND_EMAIL_ANSWERED = "SendEmailAlertWhenAnswered";
    // endregion

    // region Submit Answer Request Keys
    private static final String ANSWER_SUBMIT_ENDPOINT = "data/submitanswer.json";
    private static final String kQUESTIONID = "QuestionId";
    private static final String kANSWERTEXT = "AnswerText";
    // endregion

    // region Submit Comment Request Keys
    private static final String COMMENT_SUBMIT_ENDPOINT = "data/submitreviewcomment.json";
    private static final String KEY_REVIEW_ID = "ReviewId";
    private static final String KEY_COMMENT_TEXT = "CommentText";
    private static final String KEY_TITLE = "Title";
    // endregion

    // region Submit Photo Request Keys
    private static final String PHOTO_SUBMIT_ENDPOINT = "data/uploadphoto.json";
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    // endregion

    // region Get UAS Keys
    private static final String AUTHENTICATE_USER_ENDPOINT = "data/authenticateuser.json";
    private static final String KEY_AUTH_TOKEN = "authtoken";
    public static final String kDEVICE_FINGERPRINT = "deviceFingerprint";
    public static final String kEMAIL_USER = "userEmail";
    // endregion
    // endregion

    // region Instance Fields
    private final BVMobileInfo bvMobileInfo;
    private final String bvRootApiUrl;
    private final String bvReviewHighlightsUrl;
    private final String convApiKey;
    private final String storeApiKey;
    private final String progressiveSubmissionApiKey;
    private final String bvSdkUserAgent;
    private final FingerprintProvider fingerprintProvider;
    // endregion

    // region ProgressiveSubmission Fields
    private final String kSITENAME = "siteName";
    private final String kUSERTOKEN = "userToken";
    private final String PROGRESSIVE_SUBMIT_ENDPOINT = "data/progressiveSubmit.json";
    private final String PROGRESSIVE_INITIATE_ENDPOINT = "data/initiateSubmit.json";
    private final String kPRODUCT_IDS = "productIds";
    private final String kSUBMISSION_FIELDS = "submissionFields";
    private final String kSUBMISSION_SESSION_TOKEN = "submissionSessionToken";
    private final String kEXTENDED = "extended";
    private final String kPROGRESSIVE_SUBMISSION_AGREE_TERMS = "agreedtotermsandconditions";
    private BVConfig.Builder bvConfigBuilder;
    // endregion

    // region Constructor
    BasicRequestFactory(BVMobileInfo bvMobileInfo, BVRootApiUrls bvRootApiUrls, BVConfig bvConfig, String bvSdkUserAgent, FingerprintProvider fingerprintProvider) {
        this.bvMobileInfo = bvMobileInfo;
        this.bvRootApiUrl = bvRootApiUrls.getBazaarvoiceApiRootUrl();
        this.bvReviewHighlightsUrl = bvRootApiUrls.getBazaarvoiceReviewHighlightApiUrl();
        this.convApiKey = bvConfig.getApiKeyConversations();
        this.storeApiKey = bvConfig.getApiKeyConversationsStores();
        this.progressiveSubmissionApiKey = bvConfig.getApiKeyProgressiveSubmission();
        this.bvSdkUserAgent = bvSdkUserAgent;
        this.fingerprintProvider = fingerprintProvider;
    }
    // endregion

    // region API
    public <RequestType extends ConversationsRequest> Request create(RequestType request) {
        if (request instanceof ReviewsRequest) {
            return createFromReviewRequest((ReviewsRequest) request);
        } else if (request instanceof QuestionAndAnswerRequest) {
            return createFromQuestionAndAnswerRequest((QuestionAndAnswerRequest) request);
        }else if (request instanceof ReviewHighlightsRequest) {
            return createFromReviewHighlightsRequest((ReviewHighlightsRequest) request);
        } else if (request instanceof CommentsRequest) {
            return createFromCommentsRequest((CommentsRequest) request);
        } else if (request instanceof AuthorsRequest) {
            return createFromAuthorsRequest((AuthorsRequest) request);
        } else if (request instanceof BulkStoreRequest) {
            return createFromBulkStoreRequest((BulkStoreRequest) request);
        } else if (request instanceof BulkRatingsRequest) {
            return createFromBulkRatingsRequest((BulkRatingsRequest) request);
        } else if (request instanceof StoreReviewsRequest) {
            return createFromStoreReviewsRequest((StoreReviewsRequest) request);
        } else if (request instanceof BulkProductRequest) {
            return createFromBulkProductRequest((BulkProductRequest) request);
        } else if (request instanceof ProductDisplayPageRequest) {
            return createFromProductDisplayPageRequest((ProductDisplayPageRequest) request);
        } else if (request instanceof FeedbackSubmissionRequest) {
            return createFromFeedbackSubmissionRequest((FeedbackSubmissionRequest) request);
        } else if (request instanceof ReviewSubmissionRequest) {
            return createFromReviewSubmissionRequest((ReviewSubmissionRequest) request);
        } else if (request instanceof StoreReviewSubmissionRequest) {
            return createFromStoreReviewSubmissionRequest((StoreReviewSubmissionRequest) request);
        } else if (request instanceof QuestionSubmissionRequest) {
            return createFromQuestionSubmissionRequest((QuestionSubmissionRequest) request);
        } else if (request instanceof AnswerSubmissionRequest) {
            return createFromAnswerSubmissionRequest((AnswerSubmissionRequest) request);
        } else if (request instanceof CommentSubmissionRequest) {
            return createFromCommentSubmissionRequest((CommentSubmissionRequest) request);
        } else if (request instanceof PhotoUploadRequest) {
            return createFromPhotoUploadRequest((PhotoUploadRequest) request);
        } else if (request instanceof UserAuthenticationStringRequest) {
            return createFromUserAuthenticationStringRequest((UserAuthenticationStringRequest) request);
        } else if (request instanceof InitiateSubmitRequest) {
            return createFromInitiateSubmitRequest((InitiateSubmitRequest) request);
        } else if(request instanceof ProgressiveSubmitRequest) {
            return createFromProgressiveSubmitRequest((ProgressiveSubmitRequest) request);
        }
        throw new IllegalStateException("Unknown request type: " + request.getClass().getCanonicalName());
    }

    private Request createFromProgressiveSubmitRequest(ProgressiveSubmitRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(PROGRESSIVE_SUBMIT_ENDPOINT);

        if(request.isPreview()) {
            httpUrlBuilder.addQueryParameter("preview", "true");
        }

        if(request.includeFields()) {
            httpUrlBuilder.addEncodedQueryParameter("fields", "true");
        }

        MediaType type = MediaType.parse("application/json; charset=utf-8");
        JsonObject json = new JsonObject();

        Map<String, Object> submissionFields = request.getSubmissionFields();

        if (!submissionFields.containsKey(kPROGRESSIVE_SUBMISSION_AGREE_TERMS)) {
            Utils.mapPutSafe(submissionFields, kPROGRESSIVE_SUBMISSION_AGREE_TERMS, request.getAgreedToTermsAndConditions());
        }

        JsonObject submissionFieldsJson = new JsonObject();
        for (String s : request.getSubmissionFields().keySet()) {
            Object value = submissionFields.get(s);
            if(value != null)
            submissionFieldsJson.addProperty(s, value.toString());
        }

        if(request.getSubmissionSessionToken() != null && !request.getSubmissionSessionToken().isEmpty()){
            json.addProperty(kSUBMISSION_SESSION_TOKEN, request.getSubmissionSessionToken());
        }

        json.add(kSUBMISSION_FIELDS, submissionFieldsJson);
        json.addProperty(kPRODUCT_ID, request.getProductId());
        json.addProperty(kLOCALE, request.getLocale());

        Headers.Builder headersBuilder = new Headers.Builder();

        addCommonProgressiveSubmissionJsonParams(json, request, convApiKey, bvMobileInfo, fingerprintProvider);
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        addCommonQueryParams(httpUrlBuilder, progressiveSubmissionApiKey, bvMobileInfo);

        Headers headers = headersBuilder.build();
        HttpUrl url = httpUrlBuilder.build();
        RequestBody body = RequestBody.create(type, json.toString());
        return okRequestBuilder
                .url(url)
                .headers(headers)
                .post(body)
                .build();
    }

    private Request createFromInitiateSubmitRequest(InitiateSubmitRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(PROGRESSIVE_INITIATE_ENDPOINT);

        MediaType type = MediaType.parse("application/json; charset=utf-8");
        JsonObject json = new JsonObject();

        JsonArray productArray = new JsonArray();
        for (String productId : request.getProductIds()) {
            productArray.add(productId);
        }
        json.add(kPRODUCT_IDS, productArray);

        Headers.Builder headersBuilder = new Headers.Builder();

        addCommonProgressiveSubmissionJsonParams(json, request, convApiKey, bvMobileInfo, fingerprintProvider);
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        addCommonQueryParams(httpUrlBuilder, progressiveSubmissionApiKey, bvMobileInfo);

        if (request.isExtended()) {
            httpUrlBuilder.addQueryParameter(kEXTENDED, "true");
        }

        headersBuilder.add("Content-Type", "application/json");
        Headers headers = headersBuilder.build();
        HttpUrl url = httpUrlBuilder.build();
        RequestBody body = RequestBody.create(type, json.toString());
        return okRequestBuilder
                .url(url)
                .headers(headers)
                .post(body)
                .build();
    }

    // region Mapping Implementation
    private Request createFromReviewRequest(ReviewsRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(REVIEWS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addCommonPagingQueryParams(httpUrlBuilder, request.getLimit(), request.getOffset());

        String statsParam = "";

        if (!request.getReviewIncludeTypes().isEmpty()) {
            if (request.getReviewIncludeTypes().contains(ReviewIncludeType.PRODUCTS)) {
                // "Stats=Reviews must be used in conjunction with Include=Products"
                // - https://developer.bazaarvoice.com/conversations-api/reference/v5.4/reviews/review-display#requesting-all-reviews-for-a-particular-product-with-review-statistics-(inc.-average-rating)
                statsParam = STATS_REVIEWS + ",";
            }
            String includeStr = StringUtils.componentsSeparatedBy(request.getReviewIncludeTypes(), ",");
            httpUrlBuilder.addQueryParameter(kINCLUDE, includeStr);
        }

        if (request.getIncentivizedStats()) {
            httpUrlBuilder.addQueryParameter(kINCENTIVIZED_STATS, request.getIncentivizedStats().toString());
        }

        if (!request.getSorts().isEmpty()) {
            httpUrlBuilder
                    .addQueryParameter(kSORT, StringUtils.componentsSeparatedBy(request.getSorts(), ","));
        }

        if (request.getSearchPhrase() != null) {
            httpUrlBuilder
                    .addQueryParameter(kSEARCH, request.getSearchPhrase());
        }

        if (!request.getStatistics().isEmpty()) {
            statsParam += StringUtils.componentsSeparatedBy(request.getStatistics(), ",");
        }

        if (!statsParam.isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSTATS, statsParam);
        }

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromQuestionAndAnswerRequest(QuestionAndAnswerRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(QUESTIONS_AND_ANSWERS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addCommonPagingQueryParams(httpUrlBuilder, request.getLimit(), request.getOffset());

        httpUrlBuilder.addQueryParameter(kINCLUDE, INCLUDE_ANSWERS);

        if (!request.getQuestionSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT, StringUtils.componentsSeparatedBy(request.getQuestionSorts(), ","));
        }

        if (!request.getAnswerSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(request.getAnswerSorts(), ","));
        }

        if (request.getSearchPhrase() != null) {
            httpUrlBuilder.addQueryParameter(kSEARCH, request.getSearchPhrase());
        }

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromReviewHighlightsRequest(ReviewHighlightsRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();
        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvReviewHighlightsUrl)
                .newBuilder()
                .addPathSegments(REVIEW_HIGHLIGHTS_ENDPOINT);
        httpUrlBuilder.addPathSegment(BVSDK.getInstance().getBvUserProvidedData().getBvConfig().getClientId());
        httpUrlBuilder.addPathSegment(request.getProductId());
        HttpUrl httpUrl = httpUrlBuilder.build();
        return okRequestBuilder
                .url(httpUrl)
                .build();
    }

    private Request createFromCommentsRequest(CommentsRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(REVIEW_COMMENTS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addCommonPagingQueryParams(httpUrlBuilder, request.getLimit(), request.getOffset());

        if (!request.getIncludeTypes().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kINCLUDE, StringUtils.componentsSeparatedBy(request.getIncludeTypes(), ","));
        }

        if (!request.getIncludeTypeLimitMap().isEmpty()) {
            for (CommentIncludeType commentIncludeType : request.getIncludeTypeLimitMap().keySet()) {
                String formattedKey = String.format("Limit_%s", commentIncludeType.toString());
                int limit = request.getIncludeTypeLimitMap().get(commentIncludeType);
                String formattedValue = String.valueOf(limit);
                httpUrlBuilder.addEncodedQueryParameter(formattedKey, formattedValue);
            }
        }

        if (!request.getSorts().isEmpty()) {
            httpUrlBuilder
                    .addQueryParameter(kSORT, StringUtils.componentsSeparatedBy(request.getSorts(), ","));
        }

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromAuthorsRequest(AuthorsRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(AUTHORS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);

        if (!request.getReviewSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_REVIEW, StringUtils.componentsSeparatedBy(request.getReviewSorts(), ","));
        }

        if (!request.getQuestionSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_QUESTIONS, StringUtils.componentsSeparatedBy(request.getQuestionSorts(), ","));
        }

        if (!request.getAnswerSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(request.getAnswerSorts(), ","));
        }

        if (!request.getIncludes().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kINCLUDE, StringUtils.componentsSeparatedBy(request.getIncludes(), ","));
        }

        for (Include include : request.getIncludes()) {
            httpUrlBuilder.addQueryParameter(include.getLimitParamKey(), String.valueOf(include.getLimit()));
        }

        if (!request.getStatistics().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSTATS, StringUtils.componentsSeparatedBy(request.getStatistics(), ","));
        }

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromBulkStoreRequest(BulkStoreRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(PRODUCTS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, storeApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addCommonPagingQueryParams(httpUrlBuilder, request.getLimit(), request.getOffset());

        httpUrlBuilder.addQueryParameter(kSTATS, request.getStatsType().getKey());

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromBulkRatingsRequest(BulkRatingsRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(STATS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);

        httpUrlBuilder.addQueryParameter(kSTATS, request.getStatsType().getKey());
        if(request.getIncentivizedStats()){
            httpUrlBuilder.addQueryParameter(kINCENTIVIZED_STATS, request.getIncentivizedStats().toString());
        }
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromStoreReviewsRequest(StoreReviewsRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(REVIEWS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, storeApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addCommonPagingQueryParams(httpUrlBuilder, request.getLimit(), request.getOffset());

        if (!request.getReviewIncludeTypes().isEmpty()) {
            if (request.getReviewIncludeTypes().contains(ReviewIncludeType.PRODUCTS)) {
                // "Stats=Reviews must be used in conjunction with Include=Products"
                // - https://developer.bazaarvoice.com/conversations-api/reference/v5.4/reviews/review-display#requesting-all-reviews-for-a-particular-product-with-review-statistics-(inc.-average-rating)
                httpUrlBuilder.addQueryParameter(KEY_STATS, STATS_REVIEWS);
            }
            String includeStr = StringUtils.componentsSeparatedBy(request.getReviewIncludeTypes(), ",");
            httpUrlBuilder.addQueryParameter(kINCLUDE, includeStr);
        }

        if (!request.getSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT, StringUtils.componentsSeparatedBy(request.getSorts(), ","));
        }

        // TODO: All of these repeated query params are begging for some better composability. Composite or Decorator pattern?
        if (request.getSearchPhrase() != null) {
            httpUrlBuilder.addQueryParameter(kSEARCH, request.getSearchPhrase());
        }

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromBulkProductRequest(BulkProductRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(PRODUCTS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addSortableProductParams(httpUrlBuilder, request);

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromProductDisplayPageRequest(ProductDisplayPageRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(PRODUCTS_ENDPOINT);

        addCommonQueryParams(httpUrlBuilder, convApiKey, bvMobileInfo);
        addCommonDisplayQueryParams(httpUrlBuilder, request);
        addSortableProductParams(httpUrlBuilder, request);

        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .build();
    }

    private Request createFromFeedbackSubmissionRequest(FeedbackSubmissionRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(FEEDBACK_SUBMIT_ENDPOINT);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        addCommonSubmissionFormParams(formBodyBuilder, request, convApiKey, bvMobileInfo, fingerprintProvider);

        formPutSafe(formBodyBuilder, kCONTENT_ID, request.getContentId());
        formPutSafe(formBodyBuilder, kCONTENT_TYPE, request.getContentType());
        formPutSafe(formBodyBuilder, kFEEDBACK_TYPE, request.getFeedbackType());
        formPutSafe(formBodyBuilder, kVOTE, request.getFeedbackVote());
        formPutSafe(formBodyBuilder, kREASON_TEXT, request.getReasonFlaggedText());

        RequestBody requestBody = formBodyBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromReviewSubmissionRequest(ReviewSubmissionRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(REVIEW_SUBMIT_ENDPOINT);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        addCommonSubmissionFormParams(formBodyBuilder, request, convApiKey, bvMobileInfo, fingerprintProvider);
        addCommonReviewSubmissionFormParams(formBodyBuilder, request);

        RequestBody requestBody = formBodyBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromStoreReviewSubmissionRequest(StoreReviewSubmissionRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(REVIEW_SUBMIT_ENDPOINT);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        addCommonSubmissionFormParams(formBodyBuilder, request, storeApiKey, bvMobileInfo, fingerprintProvider);
        addCommonReviewSubmissionFormParams(formBodyBuilder, request);

        RequestBody requestBody = formBodyBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromQuestionSubmissionRequest(QuestionSubmissionRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(QUESTION_SUBMIT_ENDPOINT);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        addCommonSubmissionFormParams(formBodyBuilder, request, convApiKey, bvMobileInfo, fingerprintProvider);
        formPutSafe(formBodyBuilder, kPRODUCT_ID, request.getProductId());
        formPutSafe(formBodyBuilder, kQUESTION_SUMMARY, request.getQuestionSummary());
        formPutSafe(formBodyBuilder, kQUESTION_DETAILS, request.getQuestionDetails());
        formPutSafe(formBodyBuilder, kIS_ANONUSER, request.getUserAnonymous());
        formPutSafe(formBodyBuilder, kSEND_EMAIL_ANSWERED, request.getSendEmailAlertWhenAnswered());

        RequestBody requestBody = formBodyBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromAnswerSubmissionRequest(AnswerSubmissionRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(ANSWER_SUBMIT_ENDPOINT);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        addCommonSubmissionFormParams(formBodyBuilder, request, convApiKey, bvMobileInfo, fingerprintProvider);
        formPutSafe(formBodyBuilder, kQUESTIONID, request.getQuestionId());
        formPutSafe(formBodyBuilder, kANSWERTEXT, request.getAnswerText());

        RequestBody requestBody = formBodyBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromCommentSubmissionRequest(CommentSubmissionRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(COMMENT_SUBMIT_ENDPOINT);

        FormBody.Builder formBodyBuilder = new FormBody.Builder();

        addCommonSubmissionFormParams(formBodyBuilder, request, convApiKey, bvMobileInfo, fingerprintProvider);
        formPutSafe(formBodyBuilder, KEY_REVIEW_ID, request.getReviewId());
        formPutSafe(formBodyBuilder, KEY_COMMENT_TEXT, request.getCommentText());
        formPutSafe(formBodyBuilder, KEY_TITLE, request.getTitle());

        RequestBody requestBody = formBodyBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromPhotoUploadRequest(PhotoUploadRequest request) {
        Request.Builder okRequestBuilder = new Request.Builder();

        HttpUrl.Builder httpUrlBuilder = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(PHOTO_SUBMIT_ENDPOINT);

        MultipartBody.Builder multiPartBuilder = new MultipartBody.Builder();

        PhotoUpload upload = request.getPhotoUpload();

        multiPartBuilder
                .setType(MultipartBody.FORM)
                .addFormDataPart(ConversationsRequest.kAPI_VERSION, API_VERSION)
                .addFormDataPart(ConversationsRequest.kPASS_KEY, convApiKey)
                .addFormDataPart(PhotoUpload.kCONTENT_TYPE, upload.getContentType().getKey())
                .addFormDataPart("photo", "photo.jpg", RequestBody.create(MEDIA_TYPE_JPG, upload.getPhotoFile()));

        RequestBody requestBody = multiPartBuilder.build();
        HttpUrl httpUrl = httpUrlBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }

    private Request createFromUserAuthenticationStringRequest(UserAuthenticationStringRequest request) {
        final Request.Builder okRequestBuilder = new Request.Builder();
        final HttpUrl httpUrl = HttpUrl.parse(bvRootApiUrl)
                .newBuilder()
                .addPathSegments(AUTHENTICATE_USER_ENDPOINT)
                .build();

        final FormBody.Builder formBodyBuilder = new FormBody.Builder();

        // Don't want all of the common submission params. They should be broken up to be more composable.
        // Unit tests for this class do a good job of showing how they could be broken up.
        formPutSafe(formBodyBuilder, kAPI_VERSION, API_VERSION);
        formPutSafe(formBodyBuilder, kPASS_KEY, convApiKey);
        formPutSafe(formBodyBuilder, KEY_AUTH_TOKEN, request.getAuthToken());
        formPutSafe(formBodyBuilder, kAPP_ID, bvMobileInfo.getMobileAppIdentifier());
        formPutSafe(formBodyBuilder, kAPP_VERSION, bvMobileInfo.getMobileAppVersion());
        formPutSafe(formBodyBuilder, kBUILD_NUM, bvMobileInfo.getMobileAppCode());
        formPutSafe(formBodyBuilder, kSDK_VERSION, bvMobileInfo.getBvSdkVersion());

        final RequestBody requestBody = formBodyBuilder.build();

        Headers.Builder headersBuilder = new Headers.Builder();
        addCommonHeaders(headersBuilder, bvSdkUserAgent);
        Headers headers = headersBuilder.build();

        return okRequestBuilder
                .url(httpUrl)
                .headers(headers)
                .post(requestBody)
                .build();
    }
    // endregion

    // region Static Helpers
    private static void addCommonQueryParams(HttpUrl.Builder httpUrlBuilder, String apiKey, BVMobileInfo bvMobileInfo) {
        httpUrlBuilder.addQueryParameter(kAPI_VERSION, API_VERSION)
                .addQueryParameter(kPASS_KEY, apiKey)
                .addQueryParameter(kAPP_ID, bvMobileInfo.getMobileAppIdentifier())
                .addQueryParameter(kAPP_VERSION, bvMobileInfo.getMobileAppVersion())
                .addQueryParameter(kBUILD_NUM, bvMobileInfo.getMobileAppCode())
                .addQueryParameter(kSDK_VERSION, bvMobileInfo.getBvSdkVersion());
    }

    private static void addCommonPagingQueryParams(HttpUrl.Builder httpUrlBuilder, int limit, int offset) {
        httpUrlBuilder.addQueryParameter(kLIMIT, String.valueOf(limit));
        httpUrlBuilder.addQueryParameter(kOFFSET, String.valueOf(offset));
    }

    private static void addCommonDisplayQueryParams(HttpUrl.Builder httpUrlBuilder, ConversationsDisplayRequest request) {
        addFilterQueryParams(httpUrlBuilder, request.getFilters());
        addExtraQueryParams(httpUrlBuilder, request.getExtraParams());
        addProductSortParam(httpUrlBuilder, request.getSorts());
    }

    private static void addFilterQueryParams(HttpUrl.Builder httpUrlBuilder, List<Filter> filters) {
        for (Filter filter : filters) {
            httpUrlBuilder
                    .addEncodedQueryParameter(kFILTER, filter.toString());
        }
    }

    private static void addExtraQueryParams(HttpUrl.Builder httpUrlBuilder, List<ConversationsDisplayRequest.QueryPair> extraParams) {
        for (ConversationsDisplayRequest.QueryPair queryPair : extraParams) {
            if (queryPair.getKey() == null || queryPair.getValue() == null) {
                continue;
            }
            httpUrlBuilder
                    .addQueryParameter(queryPair.getKey(), queryPair.getValue());
        }
    }

    private static void addCommonHeaders(Headers.Builder headersBuilder, String bvSdkUserAgent) {
        headersBuilder.add(KEY_USER_AGENT, bvSdkUserAgent);
    }

    private static void addSortableProductParams(HttpUrl.Builder httpUrlBuilder, SortableProductRequest request) {
        if (!request.getReviewSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_REVIEW, StringUtils.componentsSeparatedBy(request.getReviewSorts(), ","));
        }

        if (!request.getQuestionSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_QUESTIONS, StringUtils.componentsSeparatedBy(request.getQuestionSorts(), ","));
        }

        if (!request.getAnswerSorts().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSORT_ANSWERS, StringUtils.componentsSeparatedBy(request.getAnswerSorts(), ","));
        }

        if (!request.getIncludes().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kINCLUDE, StringUtils.componentsSeparatedBy(request.getIncludes(), ","));
        }

        for (Include include : request.getIncludes()) {
            httpUrlBuilder.addQueryParameter(include.getLimitParamKey(), String.valueOf(include.getLimit()));
        }

        if (!request.getStatistics().isEmpty()) {
            httpUrlBuilder.addQueryParameter(kSTATS, StringUtils.componentsSeparatedBy(request.getStatistics(), ","));
        }
    }

    private static void addProductSortParam(HttpUrl.Builder httpUrlBuilder, List<Sort> sorts) {
        for (Sort sort : sorts) {
            httpUrlBuilder.addEncodedQueryParameter(kSORT, sort.toString());
        }
    }

    private static void jsonPutSafe(JsonObject json, @NonNull String key, @Nullable Object value){
        if (value != null && !value.toString().isEmpty()) {
            String valueStr = String.valueOf(value);
            json.addProperty(key, valueStr);
        }
    }


    private static void formPutSafe(FormBody.Builder formBodyBuilder, @NonNull String key, @Nullable Object value) {
        if (value != null && !value.toString().isEmpty()) {
            String valueStr = String.valueOf(value);
            formBodyBuilder.add(key, valueStr);
        }
    }

    private static void addCommonSubmissionFormParams(FormBody.Builder formBodyBuilder, ConversationsSubmissionRequest request, String apiKey, BVMobileInfo bvMobileInfo, FingerprintProvider fingerprintProvider) {
        formPutSafe(formBodyBuilder, kAPI_VERSION, API_VERSION);
        formPutSafe(formBodyBuilder, kPASS_KEY, apiKey);
        formPutSafe(formBodyBuilder, kAPP_ID, bvMobileInfo.getMobileAppIdentifier());
        formPutSafe(formBodyBuilder, kAPP_VERSION, bvMobileInfo.getMobileAppVersion());
        formPutSafe(formBodyBuilder, kBUILD_NUM, bvMobileInfo.getMobileAppCode());
        formPutSafe(formBodyBuilder, kSDK_VERSION, bvMobileInfo.getBvSdkVersion());
        formPutSafe(formBodyBuilder, kCAMPAIGN_ID, request.getCampaignId());
        formPutSafe(formBodyBuilder, kFINGER_PRINT, fingerprintProvider.getFingerprint());

        // Authentication Provider field should always override any other authentication piecemeal settings
        if (request.getAuthenticationProvider() != null) {
            final AuthenticationProvider authenticationProvider = request.getAuthenticationProvider();
            if (authenticationProvider instanceof BVHostedAuthenticationProvider) {
                final BVHostedAuthenticationProvider bvHostedAuthenticationProvider = (BVHostedAuthenticationProvider) authenticationProvider;
                formPutSafe(formBodyBuilder, kHOSTED_AUTH_EMAIL, bvHostedAuthenticationProvider.getUserEmailAddress());
                formPutSafe(formBodyBuilder, kHOST_AUTH_CALLBACK, bvHostedAuthenticationProvider.getCallbackUrl());
                formPutSafe(formBodyBuilder, kUSER, bvHostedAuthenticationProvider.getUas());
            } else if (authenticationProvider instanceof SiteAuthenticationProvider) {
                final SiteAuthenticationProvider siteAuthenticationProvider = (SiteAuthenticationProvider) authenticationProvider;
                formPutSafe(formBodyBuilder, kUSER, siteAuthenticationProvider.getUas());
            }
        } else {
            formPutSafe(formBodyBuilder, kHOSTED_AUTH_EMAIL, request.getHostedAuthenticationEmail());
            formPutSafe(formBodyBuilder, kHOST_AUTH_CALLBACK, request.getHostedAuthenticationCallback());
            formPutSafe(formBodyBuilder, kUSER, request.getUser());
        }

        formPutSafe(formBodyBuilder, kLOCALE, request.getLocale());
        formPutSafe(formBodyBuilder, kUSER_EMAIL, request.getUserEmail());
        formPutSafe(formBodyBuilder, kUSER_ID, request.getUserId());
        formPutSafe(formBodyBuilder, kUSER_LOCATION, request.getUserLocation());
        formPutSafe(formBodyBuilder, kUSER_NICKNAME, request.getUserNickname());
        formPutSafe(formBodyBuilder, kSEND_EMAIL_PUBLISHED, request.getSendEmailAlertWhenPublished());
        formPutSafe(formBodyBuilder, kAGREE_TERMS, request.getAgreedToTermsAndConditions());
        formPutSafe(formBodyBuilder, kACTION, getAction(request).getKey());

        for (ConversationsSubmissionRequest.FormPair formPair : request.getFormPairs()) {
            formPutSafe(formBodyBuilder, formPair.getKey(), formPair.getValue());
        }

        addSubmissionPhotosFormParams(formBodyBuilder, request);
    }

    private void addCommonProgressiveSubmissionJsonParams(JsonObject json, ConversationsSubmissionRequest request, String apiKey, BVMobileInfo bvMobileInfo, FingerprintProvider fingerprintProvider) {
        jsonPutSafe(json, kAPI_VERSION, API_VERSION);
        jsonPutSafe(json, kPASS_KEY, apiKey);
        jsonPutSafe(json, kAPP_ID, bvMobileInfo.getMobileAppIdentifier());
        jsonPutSafe(json, kAPP_VERSION, bvMobileInfo.getMobileAppVersion());
        jsonPutSafe(json, kBUILD_NUM, bvMobileInfo.getMobileAppCode());
        jsonPutSafe(json, kSDK_VERSION, bvMobileInfo.getBvSdkVersion());
        jsonPutSafe(json, kCAMPAIGN_ID, request.getCampaignId());
        jsonPutSafe(json, kDEVICE_FINGERPRINT, fingerprintProvider.getFingerprint());
        jsonPutSafe(json, kEMAIL_USER, request.getUserEmail());

        // Authentication Provider field should always override any other authentication piecemeal settings
        if (request.getAuthenticationProvider() != null) {
            final AuthenticationProvider authenticationProvider = request.getAuthenticationProvider();
            if (authenticationProvider instanceof BVHostedAuthenticationProvider) {
                final BVHostedAuthenticationProvider bvHostedAuthenticationProvider = (BVHostedAuthenticationProvider) authenticationProvider;
                jsonPutSafe(json, kHOSTED_AUTH_EMAIL, bvHostedAuthenticationProvider.getUserEmailAddress());
                jsonPutSafe(json, kHOST_AUTH_CALLBACK, bvHostedAuthenticationProvider.getCallbackUrl());
                jsonPutSafe(json, kUSERTOKEN, bvHostedAuthenticationProvider.getUas());
            } else if (authenticationProvider instanceof SiteAuthenticationProvider) {
                final SiteAuthenticationProvider siteAuthenticationProvider = (SiteAuthenticationProvider) authenticationProvider;
                jsonPutSafe(json, kUSERTOKEN, siteAuthenticationProvider.getUas());
            }
        } else {
            jsonPutSafe(json, kEMAIL_USER, request.getHostedAuthenticationEmail());
            jsonPutSafe(json, kHOST_AUTH_CALLBACK, request.getHostedAuthenticationCallback());
            jsonPutSafe(json, kUSER.toLowerCase(), request.getUser());
        }

        jsonPutSafe(json, kLOCALE, request.getLocale());
        jsonPutSafe(json, kUSER_EMAIL, request.getUserEmail());
        jsonPutSafe(json, "userId", request.getUserId());
        jsonPutSafe(json, kUSER_LOCATION, request.getUserLocation());
        jsonPutSafe(json, kUSER_NICKNAME, request.getUserNickname());
        jsonPutSafe(json, kSEND_EMAIL_PUBLISHED, request.getSendEmailAlertWhenPublished());
        jsonPutSafe(json, kAGREE_TERMS, request.getAgreedToTermsAndConditions());
        jsonPutSafe(json, kACTION, getAction(request).getKey());

        for (ConversationsSubmissionRequest.FormPair formPair : request.getFormPairs()) {
            jsonPutSafe(json, formPair.getKey(), formPair.getValue());
        }

    }


    private static void addSubmissionPhotosFormParams(FormBody.Builder formBodyBuilder, ConversationsSubmissionRequest request) {
        final List<Photo> photos = request.getPhotos();
        if (photos != null) {
            int idx = 0;
            for (Photo photo : photos) {
                final String keyPhotoUrl = String.format(Locale.US, KEY_PHOTO_URL_TEMPLATE, idx);
                final String keyPhotoCaption = String.format(Locale.US, KEY_PHOTO_CAPTION_TEMPLATE, idx);
                formPutSafe(formBodyBuilder, keyPhotoUrl, photo.getContent().getNormalUrl());
                formPutSafe(formBodyBuilder, keyPhotoCaption, photo.getCaption());
                idx++;
            }
        }
    }

    private static void addCommonReviewSubmissionFormParams(FormBody.Builder formBodyBuilder, BaseReviewSubmissionRequest request) {
        formPutSafe(formBodyBuilder, kPRODUCT_ID, request.getProductId());
        formPutSafe(formBodyBuilder, kIS_RECOMMENDED, request.getRecommended());
        formPutSafe(formBodyBuilder, kSEND_EMAIL_COMMENTED, request.getSendEmailAlertWhenCommented());
        formPutSafe(formBodyBuilder, kRATING, request.getRating());
        formPutSafe(formBodyBuilder, kNET_PROMOTER_SCORE, request.getNetPromoterScore());
        formPutSafe(formBodyBuilder, kNET_PROMOTER_COMMENT, request.getNetPromoterComment());
        formPutSafe(formBodyBuilder, kTITLE, request.getTitle());
        formPutSafe(formBodyBuilder, kREVIEW_TEXT, request.getReviewText());

        final List<BaseReviewSubmissionRequest.PredefinedTag> predefinedTags = request.getPredefinedTags();
        for (int i = 0; i < predefinedTags.size(); i++) {
            final BaseReviewSubmissionRequest.PredefinedTag predefinedTag = predefinedTags.get(i);
            final String keyFormatted = String.format(Locale.US, "tagid_%s/%s", predefinedTag.getQuestionId(), predefinedTag.getTagId());
            formPutSafe(formBodyBuilder, keyFormatted, predefinedTag.getValue());
        }

        final Map<String, String> freeFormTags = request.getFreeFormTags();
        final List<String> freeFormTagKeys = new ArrayList<>(freeFormTags.keySet());
        for (int i = 0; i < freeFormTagKeys.size(); i++) {
            final String key = freeFormTagKeys.get(i);
            final String value = freeFormTags.get(key);
            final String keyFormatted = String.format(Locale.US, KEY_FREEFORM_TAG_TEMPLATE, key, i);
            formPutSafe(formBodyBuilder, keyFormatted, value);
        }

        final Set<String> ratingSliderKeys = request.getRatingSliders().keySet();
        for (String key : ratingSliderKeys) {
            final String keyRating = String.format(Locale.US, KEY_RATING_TEMPLATE, key);
            formPutSafe(formBodyBuilder, keyRating, request.getRatingSliders().get(key));
        }

        final Set<String> ratingQuestionsKeys = request.getRatingQuestions().keySet();
        for (String key : ratingQuestionsKeys) {
            final String keyRating = String.format(Locale.US, KEY_RATING_TEMPLATE, key);
            formPutSafe(formBodyBuilder, keyRating, request.getRatingQuestions().get(key));
        }

        final Set<String> contextDataValueKeys = request.getContextDataValues().keySet();
        for (String key : contextDataValueKeys) {
            final String keyRating = String.format(Locale.US, KEY_CDV_TEMPLATE, key);
            formPutSafe(formBodyBuilder, keyRating, request.getContextDataValues().get(key));
        }

        final Map<String, String> additionalFields = request.getAdditionalFields();
        for (String key : additionalFields.keySet()) {
            final String value = additionalFields.get(key);
            final String keyAdditionalField = String.format(Locale.US, KEY_ADDITIONAL_PARAM_TEMPLATE, key);
            formPutSafe(formBodyBuilder, keyAdditionalField, value);
        }

        final List<VideoSubmissionData> videoDataList = request.getVideoSubmissionData();
        for (int i = 0; i < videoDataList.size(); i++) {
            VideoSubmissionData videoData = videoDataList.get(i);
            int submitIndex = i + 1;
            formPutSafe(formBodyBuilder, String.format(Locale.US, VIDEO_URL_TEMPLATE, submitIndex), videoData.getVideoUrl());
            if (videoData.getVideoCaption() != null) {
                formPutSafe(formBodyBuilder, String.format(Locale.US, VIDEO_CAPTION_TEMPLATE, submitIndex), videoData.getVideoCaption());
            }
        }
    }

    /**
     * This performs the necessary chaining of call since Action=Preview gives
     * us all of the form errors, when making a submit call with Action=Submit
     * we must make a first one with Action=Preview behind the scenes for the
     * user.
     *
     * @param request The built OkHttp Request object
     * @return The action to perform for this specific request, based on the
     * state of the {@link ConversationsSubmissionRequest#isForcePreview()}
     */
    private static Action getAction(ConversationsSubmissionRequest request) {
        return request.isForcePreview() ? Action.Preview : request.getAction();
    }
    // endregion
}
