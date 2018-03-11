package finalcom.javadmorsali.adsfinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.List;

import Entities.Entity_Ads;
import Utility.UtilMethod;


public class AdsAdapter extends BaseAdapter {

    Context context;
    int layoutResourceId;
    Entity_Ads[] data = null;

    public AdsAdapter(Context context, int layoutResourceId, Entity_Ads[] data) {
        //super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new WeatherHolder();
            holder.imgAdsPic = (SmartImageView) row.findViewById(R.id.imgAdsPic);
            holder.lblAdsTitle = (TextView) row.findViewById(R.id.lblAdsTitle);
            holder.lblAdsPersianCreateDate= (TextView) row.findViewById(R.id.lblAdsPersianCreateDate);
            holder.lblAdsPrice = (TextView) row.findViewById(R.id.lblAdsPrice);
            holder.lblAdsAddress = (TextView) row.findViewById(R.id.lblAdsAddress);

            row.setTag(holder);
        } else {
            holder = (WeatherHolder) row.getTag();
        }

        Entity_Ads ads = data[position];
        holder.lblAdsTitle.setText(ads.getId() + " ::: " + ads.getTitle());
        holder.lblAdsPrice.setText(ads.getPrice());
        holder.lblAdsAddress.setText(ads.getAddress());
        holder.lblAdsPersianCreateDate.setText(ads.getPersianCreateDate());
        //new UtilMethod.ImageDownloaderTask(holder.imgAdsPic).execute(ads.getPictureUrl());
        holder.imgAdsPic.setImageUrl(ads.getPictureUrl());
        return row;
    }

    static class WeatherHolder {
        SmartImageView imgAdsPic;
        TextView lblAdsPrice;
        TextView lblAdsTitle;
        TextView lblAdsAddress;
        TextView lblAdsPersianCreateDate;
    }
}