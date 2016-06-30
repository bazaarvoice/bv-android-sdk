package com.bazaarvoice.bvandroidsdk;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Bazaarvoice on 3/31/16.
 */
public class BVCurations {

    /**
     * Get curations feed using specified parameters
     * @param request The request is used to specify parameters which will be used in the Curations GET request
     * @param callback Callback is used to handle successful and unsuccessful requests
     * <em>Note:</em> This method keeps a weak reference to the {@link CurationsFeedCallback} instance and will be
     * garbage collected if you do not keep a strong reference to it. }.
     */
    public void getCurationsFeedItems(CurationsFeedRequest request, CurationsFeedCallback callback){
        WeakReference<CurationsFeedCallback> feedCallback = new WeakReference<>(callback);
        GetCurationsUpdateTask task = new GetCurationsUpdateTask(feedCallback);
        task.execute(request);
    }

    /**
     * Post Content to curations
     * @param post The content you would like to post.
     * @param callback Callback is used to handle successful and unsuccessful posts
     * <em>Note:</em> This method keeps a weak reference to the {@link CurationsPostCallback} instance and will be
     * garbage collected if you do not keep a strong reference to it. }.
     */
    public void postContentToCurations(CurationsPostRequest request, CurationsPostCallback callback){
        WeakReference<CurationsPostCallback> postCallback = new WeakReference<>(callback);
        PostCurationsContentTask task = new PostCurationsContentTask(postCallback);
        task.execute(request);
    }

    private static class GetCurationsUpdateTask extends AsyncTask<CurationsFeedRequest, Void, ResponseData> {
        private WeakReference<CurationsFeedCallback> feedCallback;

        GetCurationsUpdateTask(WeakReference<CurationsFeedCallback> feedCallback) {
            this.feedCallback = feedCallback;
        }

        @Override
        protected ResponseData doInBackground(CurationsFeedRequest... params) {
            CurationsFeedRequest request = params[0];
            OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();
            Gson gson = BVSDK.getInstance().getGson();

            Request httpRequest = new Request.Builder()
                    .url(request.toUrlQueryString())
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build();
            Logger.v(getClass().getSimpleName(), httpRequest.url().toString());

            Throwable errorThrowable = null;
            CurationsFeedResponse curationsResponse = null;
            boolean didSucceed = false;
            Response response = null;
            try {
                 response = okHttpClient.newCall(httpRequest).execute();
                if (!response.isSuccessful()) {
                    errorThrowable = new Exception("Unsuccessful response for curations with error code: " + response.code());
                } else {
                    curationsResponse = gson.fromJson(response.body().charStream(), CurationsFeedResponse.class);
                    //curations responses can have an HTTP response code of 200 but they return a parameter, 'code', which should also be checked for a 200
                    if (curationsResponse.code == 200){
                        didSucceed = true;
                    }else {
                        didSucceed = false;
                        curationsResponse = null;
                        errorThrowable = new Exception("Curations 'code' not 200");
                    }
                }

            } catch (IOException e) {
                errorThrowable = new IOException("Request for curations failed", e);
            } catch (JsonIOException | JsonSyntaxException e) {
                errorThrowable = new Exception("Failed to parse curations");
            } catch (Exception e) {
                errorThrowable = new Exception("Exception while getting curations");
            }finally {
                if (response != null && response.body() != null) {
                    response.body().close();
                }
            }

            return new ResponseData(didSucceed, errorThrowable, curationsResponse);
        }

        @Override
        protected void onPostExecute(ResponseData response) {
            super.onPostExecute(response);

            CurationsFeedCallback cb = feedCallback.get();
            feedCallback.clear();

            if (cb == null){
                return;
            }

            if (response.isDidSucceed()) {

                CurationsFeedResponse curationsResponse = (CurationsFeedResponse) response.getResponse();
                cb.onSuccess(curationsResponse.getUpdates());
            } else {
                response.getErrorThrowable().printStackTrace();
                cb.onFailure(response.getErrorThrowable());
            }
        }
    }

    private static class PostCurationsContentTask extends AsyncTask<CurationsPostRequest, Void, ResponseData> {
        private final WeakReference<CurationsPostCallback> postCallback;

        PostCurationsContentTask(WeakReference<CurationsPostCallback> postCallback) {
            this.postCallback = postCallback;
        }

        @Override
        protected ResponseData doInBackground(CurationsPostRequest... params) {
            CurationsPostRequest request = params[0];

            OkHttpClient okHttpClient = BVSDK.getInstance().getOkHttpClient();

            Gson gson = BVSDK.getInstance().getGson();

            String jsonPayload = request.getJsonPayload();
            MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("jsonpayload", jsonPayload);

            if (request.builder.bitmap != null && (request.builder.photos == null || request.builder.photos.size() == 0)){
                String contentBase64 = "data:image/jpeg;base64," + toBase64(request.builder.bitmap);
                bodyBuilder.addFormDataPart("image", contentBase64);
            }

            Request httpRequest = new Request.Builder()
                    .url(request.toUrlQueryString())
                    .post(bodyBuilder.build())
                    .build();
            Logger.v(getClass().getSimpleName(), "POST: " + request.toUrlQueryString());
            Logger.v(getClass().getSimpleName(), "MultiPart: " + jsonPayload);

            Throwable errorThrowable = null;
            CurationsPostResponse curationsResponse = null;
            boolean didSucceed = false;

            Response response = null;
            try {
                response = okHttpClient.newCall(httpRequest).execute();
                if (!response.isSuccessful()) {
                    errorThrowable = new Exception("Unsuccessful response for curations with error code: " + response.code());
                } else {
                    curationsResponse = gson.fromJson(response.body().charStream(), CurationsPostResponse.class);
                    //curations responses can have an HTTP response code of 200 but they return a parameter, 'code', which should also be checked for a 200
                    if (curationsResponse.status.equals(201.0)){
                        didSucceed = true;
                    }else {
                        didSucceed = false;
                        errorThrowable = new Exception("Curations error: " + curationsResponse.status);
                        curationsResponse = null;
                    }
                }

            } catch (IOException e) {
                errorThrowable = new IOException("Request for curations failed", e);
            } catch (JsonIOException | JsonSyntaxException e) {
                errorThrowable = new Exception("Failed to parse curations", e);
            } catch (Exception e) {
                errorThrowable = new Exception("Exception while getting curations", e);
            }finally {
                if (response != null && response.body() != null) {
                    response.body().close();
                }
            }

            if (errorThrowable != null) {
                errorThrowable.printStackTrace();
            }

            return new ResponseData(didSucceed, errorThrowable, curationsResponse);
        }

        @Override
        protected void onPostExecute(ResponseData response) {
            CurationsPostCallback cb = postCallback.get();
            postCallback.clear();

            if (cb == null){
                return;
            }

            if (response.isDidSucceed()){
                cb.onSuccess((CurationsPostResponse) response.getResponse());
            }else{
                cb.onFailure(response.getErrorThrowable());
            }
        }

        private String toBase64(Bitmap bitmap){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(bytes, Base64.NO_WRAP);
        }
    }
}
