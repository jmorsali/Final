package finalcom.javadmorsali.adsfinder;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ghasemkiani.util.icu.PersianCalendar;
import com.google.gson.Gson;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.ResponseHandlerInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import Entities.Entity_Ads;
import Utility.HttpUtils;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpResponse;

public class ShowAdsList extends AppCompatActivity {

    SwipeRefreshLayout swipeLayout;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show_ads_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        setSupportActionBar(toolbar);

        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setColorScheme(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);


        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(ShowAdsList.this, AddAds.class);
                startActivity(i);
            }
        });


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ShowAds();

            }
        });
        ShowAds();
    }


    private void ShowAds() {
        Intent i = getIntent();
        if (i.getBooleanExtra("MyAds", false)) {
            setTitle("آگهی های من");
            BindMyAds();

        } else {
            BindAllAds();
            setTitle("آگهی جو");

        }
    }

    private void BindAllAds() {
        RequestParams rp = new RequestParams();


        HttpUtils.get("/api/Ads/GetAllAds", rp, new JsonHttpResponseHandler() {


            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                double Percent = (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1;

                String Message = String.format("در حال دریافت اطلاعات : (%2.0f%%)", Percent);
                Toast.makeText(ShowAdsList.this, Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ShowAdsList.this, "خطا در دریافت اطلاعات" + statusCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                FillListView(response);
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

            }
        });
    }

    private void BindMyAds() {
        RequestParams rp = new RequestParams();
        String UserId = getResources().getString(R.string.AdsAppCurrentUserId);
        rp.add("UserId", UserId);

        HttpUtils.get("/api/Ads/GetMyAds", rp, new JsonHttpResponseHandler() {

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                double Percent = (totalSize > 0) ? (bytesWritten * 1.0 / totalSize) * 100 : -1;

                String Message = String.format("در حال دریافت اطلاعات : (%2.0f%%)", Percent);
                Toast.makeText(ShowAdsList.this, Message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(ShowAdsList.this, "خطا در دریافت اطلاعات" + statusCode, Toast.LENGTH_SHORT).show();
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

            }
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ShowAdsList.this, "خطا در دریافت اطلاعات" + statusCode, Toast.LENGTH_SHORT).show();
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ShowAdsList.this, "خطا در دریافت اطلاعات" + statusCode, Toast.LENGTH_SHORT).show();
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);

            }



            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                FillListView(response);
                if (swipeLayout.isRefreshing())
                    swipeLayout.setRefreshing(false);
            }
        });
    }

    private void FillListView(JSONArray response) {
        String responseString = response.toString();
        Log.d("asd", "---------------- this is response : " + responseString);
        try {
            Gson g = new Gson();
            final Entity_Ads[] adsList = g.fromJson(responseString, Entity_Ads[].class);
            ListAdapter adapter = new AdsAdapter(ShowAdsList.this, R.layout.ads_list_layout, adsList);

            final ListView listView = (ListView) findViewById(R.id.contactlist);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(ShowAdsList.this, "pos:" + position, Toast.LENGTH_SHORT).show();
                    Entity_Ads ads = (Entity_Ads) listView.getItemAtPosition(position);
                    Intent i = new Intent(ShowAdsList.this, ShowAds.class);
                    Bundle b = new Bundle();
                    b.putSerializable("Ads", ads);
                    i.putExtras(b);
                    startActivity(i);
                }
            });

        } catch (Exception e) {
            Toast.makeText(ShowAdsList.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show_ads_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.MyAds) {
            Intent i = new Intent(ShowAdsList.this, ShowAdsList.class);
            i.putExtra("MyAds", true);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    static int ExitCounter = 0;

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        if (!i.getBooleanExtra("MyAds", false)) {
            if (ExitCounter == 0) {
                ExitCounter++;
                Toast.makeText(this, "برای خروج یکبار دیگر بر روی دکمه برگشت کلیک کنید", Toast.LENGTH_SHORT).show();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ExitCounter = 0;
                    }
                }, 3000);
            } else
                finish();
        } else
            finish();

    }

}
