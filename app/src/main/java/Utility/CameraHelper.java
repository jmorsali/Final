package Utility;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jmorsali on 2018-03-02.
 */

public class CameraHelper {

    static String mCurrentPhotoPath;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_EXTERNAL_STORAGE = 2;
    private static final int RESULT_OK = 1;
    // Storage Permissions
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public static byte[] HandleResult(Activity v, int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                byte[] res = galleryAddPic(v);
                return res;
            } catch (FileNotFoundException e) {
                Toast.makeText(v, e.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        return null;
    }

    private static File createImageFile() throws IOException {

        final String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/picFolder/";
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File newdir = new File(dir);
        newdir.mkdirs();


        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                newdir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    public static void RequestCaptureImage(Activity v) {

        verifyStoragePermissions(v);
        if(!GetStoragePermissions(v))
        {
            Toast.makeText(v, "دسترسی های لازم به دوربین و حافظه ذخیره سازی اعطا نگردیده است", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(v.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Uri photoURI = FileProvider.getUriForFile(v,
                    "com.final.android.fileprovider",
                    photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            v.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

        }

    }

    private static byte[] galleryAddPic(Activity v) throws FileNotFoundException {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);
        v.sendBroadcast(mediaScanIntent);

        final InputStream imageStream = v.getContentResolver().openInputStream(contentUri);
        //final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
        //return new Pair<String, Bitmap>(mCurrentPhotoPath, selectedImage);

        ByteArrayOutputStream byteStream=null;
        try {

            byte[] buffer = new byte[1024];
            int count;
            byteStream = new ByteArrayOutputStream(imageStream.available());

            while (true) {
                count = imageStream.read(buffer);
                if (count <= 0) break;
                byteStream.write(buffer, 0, count);
            }
            return byteStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(v, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    public byte[] ReadFile(Activity v, String path) {
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            return bytes;
        } catch (FileNotFoundException e) {
            Toast.makeText(v, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        } catch (IOException e) {
            Toast.makeText(v, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static boolean GetStoragePermissions(Activity activity) {
        int permission_Storage = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_Camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        return (permission_Storage == PackageManager.PERMISSION_GRANTED) && (permission_Camera == PackageManager.PERMISSION_GRANTED) ;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission_Storage = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_Camera = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA);

        if (permission_Storage != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }

        if (permission_Camera != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_IMAGE_CAPTURE
            );
        }
    }

}
