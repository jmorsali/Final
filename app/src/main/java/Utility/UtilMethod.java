package Utility;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.ghasemkiani.util.icu.PersianCalendar;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.loopj.android.http.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.HttpStatus;
import finalcom.javadmorsali.adsfinder.R;
import finalcom.javadmorsali.adsfinder.ShowAdsList;

/**
 * Created by jmorsali on 2018-02-27.
 */

public class UtilMethod {

    private Date stringToDate(String aDate, String aFormat) {

        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

    }

    public static String PersianToday() {

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        PersianCalendar persianCalendar3 = new PersianCalendar();
        persianCalendar3.setTime(gregorianCalendar.getTime());

        int Year = persianCalendar3.get(Calendar.YEAR);
        int Month = (persianCalendar3.get(Calendar.MONTH) + 1);
        int Day = persianCalendar3.get(Calendar.DAY_OF_MONTH);
        int Hour = persianCalendar3.get(Calendar.HOUR_OF_DAY);
        int Minute = persianCalendar3.get(Calendar.MINUTE);
        int Second = persianCalendar3.get(Calendar.SECOND);

        return String.format("%d/%d/%d %d:%d:%d", Year, Month, Day, Hour, Minute, Second);
    }

    public static String encodeTobase64(byte[] image) {
        String imageEncoded = Base64.encodeToString(image, Base64.DEFAULT);
        return imageEncoded;
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 10, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }


    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight) {
        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = bWidth;
        int nHeight = bHeight;

        if (nWidth > reqWidth) {
            int ratio = bWidth / reqWidth;
            if (ratio > 0) {
                nWidth = reqWidth;
                nHeight = bHeight / ratio;
            }
        }

        if (nHeight > reqHeight) {
            int ratio = bHeight / reqHeight;
            if (ratio > 0) {
                nHeight = reqHeight;
                nWidth = bWidth / ratio;
            }
        }

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, true);

    }

    public static class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.nopic);
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {

            if (url == null || url.trim().equals(""))
                return null;
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
