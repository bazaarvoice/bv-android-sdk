package com.bazaarvoice.bvandroidsdk;

final class ConversationAnalyticsUtil {

    private ConversationAnalyticsUtil() {
    }


    static BVEventValues.BVProductType getProductTypeFromRequest(ConversationsRequest request) {

        if (request instanceof QuestionSubmissionRequest ||
                request instanceof QuestionAndAnswerRequest ||
                request instanceof AnswerSubmissionRequest) {
            return BVEventValues.BVProductType.CONVERSATIONS_QANDA;
        }

        return BVEventValues.BVProductType.CONVERSATIONS_REVIEWS;

    }
}
