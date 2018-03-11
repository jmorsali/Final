package finalcom.javadmorsali.adsfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64InputStream;
import android.util.Log;
import android.util.Pair;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ibm.icu.impl.Utility;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

import Entities.Entity_Ads;
import Utility.CameraHelper;
import Utility.GalleryHelper;
import Utility.HttpUtils;
import Utility.UtilMethod;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.entity.mime.MultipartEntityBuilder;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;

public class AddAds extends AppCompatActivity {

    ImageView imgAdsPic;
    //Bitmap SelectedImage;
    byte[] SelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ads);

        imgAdsPic = (ImageView) findViewById(R.id.imgAdsPic);
    }

    public void getPicFromGallery(View view) {
        GalleryHelper.RequestGallery(this);
    }

    public void getPicFromCamera(View view) {
        CameraHelper.RequestCaptureImage(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        byte[] buffer = null;
        if (requestCode == GalleryHelper.RESULT_LOAD_IMG)
            buffer = GalleryHelper.HandleResult(this, requestCode, resultCode, data);
        else if (requestCode == CameraHelper.REQUEST_IMAGE_CAPTURE)
            buffer = CameraHelper.HandleResult(this, requestCode, requestCode, data);

        if (buffer != null) {
            Bitmap b = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
            //Bitmap newb = UtilMethod.getScaledBitmap(b, 450, 350);
            //String A= UtilMethod.encodeTobase64(b);
            SelectedImage = buffer;//;
            imgAdsPic.setImageBitmap(b);
        } else
            imgAdsPic.setImageResource(R.drawable.nopic);

    }


    public void SaveAds(View view) {

        Entity_Ads ads = new Entity_Ads();

        EditText txtAdsTitle = (EditText) findViewById(R.id.txtAdsTitle);
        ads.setTitle(txtAdsTitle.getText());

        EditText txtAdsPrice = (EditText) findViewById(R.id.txtAdsPrice);
        ads.setPrice(txtAdsPrice.getText());

        EditText txtAdsAddress = (EditText) findViewById(R.id.txtAdsAddress);
        ads.setAddress(txtAdsAddress.getText());

        EditText txtAdsDescription = (EditText) findViewById(R.id.txtAdsDescription);
        ads.setDescription(txtAdsDescription.getText());

        EditText txtAdsMobileNo = (EditText) findViewById(R.id.txtAdsMobileNo);
        ads.setMobileNo(txtAdsMobileNo.getText());


        EditText txtAdsTel = (EditText) findViewById(R.id.txtAdsTel);
        ads.setTel(txtAdsTel.getText());

        ads.setPersianCreateDate(UtilMethod.PersianToday());
        ads.setCreateDateTime(Calendar.getInstance().getTime().toString());
        if (SelectedImage != null)
            ads.setPictureUrl(ads.getId() + ".jpg");
        else
            ads.setPictureUrl("");

        ads.setUserId(Integer.valueOf(getResources().getString(R.string.AdsAppCurrentUserId)));

        SaveAdsApi(ads);

    }

    private void SaveAdsApi(Entity_Ads e) {

        Gson gson = new Gson();
        String Ads_json = gson.toJson(e);
        StringEntity entity;
        entity = new StringEntity(Ads_json, "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        HttpUtils.post(getApplicationContext(), "/api/Ads/SaveAds", entity, "application/json;charset=UTF-8",
                new JsonHttpResponseHandler() {
                    //HttpUtils.post("/api/Ads/SaveAds", rp, new JsonHttpResponseHandler() {

                    ProgressDialog progressDialog;

                    @Override
                    public void onStart() {
                        super.onStart();
                        progressDialog = new ProgressDialog(AddAds.this);
                        progressDialog.setMessage("در حال ذخیره سازی اطلاعات آگهی، لطفا منتظر بمانید...");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("asd", "---------------- this is response : " + errorResponse);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(AddAds.this, "خطا در دریافت اطلاعات" + statusCode, Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        Log.d("asd", "---------------- this is response : " + responseString);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(AddAds.this, "خطا در دریافت اطلاعات" + statusCode, Toast.LENGTH_SHORT).show();
                    }


                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        Log.d("asd", "---------------- this is response : " + response);
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                        Gson gson = new Gson();
                        SaveAds_Result res = gson.fromJson(response.toString(), SaveAds_Result.class);
                        if (res.ResultCode >= 0) {
                            Toast.makeText(AddAds.this, "اطلاعات با موفقیت ثبت شد", Toast.LENGTH_LONG).show();
                            SendFile(res.ResultCode, SelectedImage);
                        } else
                            Toast.makeText(AddAds.this, "خطا در دریافت اطلاعات" + res.ResultMessage, Toast.LENGTH_SHORT).show();
                    }

                });
    }

    private void SendFile(int resultCode, byte[] selectedImage) {

        byte[] myByteArray = selectedImage;
        RequestParams params = new RequestParams();
        params.put("adspic", new ByteArrayInputStream(myByteArray));
        params.put("FileName", resultCode);
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("FileName", resultCode);
            jobj.put("File", selectedImage);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringEntity entity;
        entity = new StringEntity(jobj.toString(), "UTF-8");
        entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

        HttpUtils.put( "/api/Ads/SavePic", params, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                finish();

            }
        });


     //   HttpUtils.post(getApplicationContext(), "/api/Ads/SavePic", entity, "application/json;charset=UTF-8",
       //         new JsonHttpResponseHandler() {

         //       });
    }

    public class SaveAds_Result {
        public int ResultCode;
        public String ResultMessage;
    }
}