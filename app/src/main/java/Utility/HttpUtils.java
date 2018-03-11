package Utility;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.entity.StringEntity;

public class HttpUtils {
    private static final String BASE_URL ="http://my.born-prc.ir";
    //private static final String BASE_URL = "http://192.168.43.176";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(5000);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(Context Context, String url, StringEntity entity, String contentType, AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(5000);
        client.post(Context,getAbsoluteUrl(url),entity,contentType, responseHandler);
    }

    public static void put(String url,RequestParams params,  AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(5000);
        client.put(getAbsoluteUrl(url),params, responseHandler);
    }
    public static void post(String url,RequestParams params,  AsyncHttpResponseHandler responseHandler) {
        client.setTimeout(5000);
        client.post(getAbsoluteUrl(url),params, responseHandler);
    }

    public static void getByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(url, params, responseHandler);
    }

    public static void postByUrl(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(url, params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


}