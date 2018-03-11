package finalcom.javadmorsali.adsfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import Entities.Entity_Ads;
import Utility.CameraHelper;
import Utility.GalleryHelper;
import Utility.UtilMethod;

public class ShowAds extends AppCompatActivity {
    ImageView imgAdsPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ads);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        if (i.getExtras() != null) {
            Entity_Ads ads = (Entity_Ads) i.getExtras().getSerializable("Ads");

            imgAdsPic = (ImageView) findViewById(R.id.imgAdsPic);
            new UtilMethod.ImageDownloaderTask(imgAdsPic).execute(ads.getPictureUrl());
            //Bitmap b = BitmapFactory.decodeByteArray(ads.getPicture_bytes(), 0, ads.getPicture().length());
            //imgAdsPic.setImageBitmap(b);

            TextView lblAdsTitle = (TextView) findViewById(R.id.lblAdsTitle);
            lblAdsTitle.setText(ads.getTitle());

            TextView lblAdsPrice = (TextView) findViewById(R.id.lblAdsPrice);
            lblAdsPrice.setText(ads.getPrice());

            TextView lblAdsAddress = (TextView) findViewById(R.id.lblAdsAddress);
            lblAdsAddress.setText(ads.getAddress());

            TextView lblAdsDescription = (TextView) findViewById(R.id.lblAdsDescription);
            lblAdsDescription.setText(ads.getDescription());

            TextView lblAdsMobileNo = (TextView) findViewById(R.id.lblAdsMobileNo);
            lblAdsMobileNo.setText(ads.getMobileNo());

            TextView lblAdsTel = (TextView) findViewById(R.id.lblAdsTel);
            lblAdsTel.setText(ads.getTel());

            TextView lblAdsPersianCreateDate = (TextView) findViewById(R.id.lblAdsPersianCreateDate);
            lblAdsPersianCreateDate.setText(ads.getPersianCreateDate());


        } else
            finish();
    }


}
