package Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;

/**
 * Created by jmorsali on 2018-03-02.
 */

public class GalleryHelper {

    public static int RESULT_LOAD_IMG = 3;

    public static void RequestGallery(Activity v) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        v.startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    public static byte[] HandleResult(Activity v, int reqCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = v.getContentResolver().openInputStream(imageUri);

                ByteArrayOutputStream byteStream = null;
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


                //final Bitmap selectedImage = BitmapFactory.decodeByteArray(buffer,0,buffer.length);
                //imageView.setImageBitmap(selectedImage);
                return null;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(v, "Something went wrong :::  " + e.getMessage(), Toast.LENGTH_LONG).show();
                return null;
            }

        } else {
            Toast.makeText(v, "You haven't picked Image", Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
