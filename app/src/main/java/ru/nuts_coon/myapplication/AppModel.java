package ru.nuts_coon.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


class AppModel {

    private Context context;

    AppModel(Context ctx){
        this.context = ctx;
    }

    Uri getPhotoPath(){
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + context.getString(R.string.app_name) + "/";
        createDir(path);

        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File photo = new File(path + timeStamp + ".jpeg");

        return Uri.fromFile(photo);
    }

    private void createDir(String path){
        File file = new File(path);
        if (!file.exists()){
            file.mkdir();
        }
    }

    Bitmap getPicture(Uri path, int reqWidth, int reqHeight) throws IOException {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path.getPath(), options);

        int width = options.outWidth;
        int height = options.outHeight;

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bm = BitmapFactory.decodeFile(path.getPath(), options);

        int newWidth;
        int newHeight;
        int orientation = getOrientation(path);

        if (orientation == 90 || orientation == 270){
            newWidth = width / (width / bm.getWidth());
            newHeight = height / (height / bm.getHeight());
        }else {
            newWidth = height / (height / bm.getWidth());
            newHeight = width / (width / bm.getWidth());
        }

        if (orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.preRotate(orientation);
            bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
        }
        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    private int getOrientation(Uri uri) {
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

            if (cursor == null || cursor.getCount() != 1) {
                return -1;
            }

            cursor.moveToFirst();
            int orientation = cursor.getInt(0);
            cursor.close();
            return orientation;
        }
        else {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;
                    case ExifInterface.ORIENTATION_NORMAL:
                        return 0;
                    default:
                        return -1;
                }
            } catch (IOException e) {
                return -1;
            }
        }
    }

    String formatPhoneNumber(String phone){
        String s = "";
        if (phone.length() == 12){
            s = phone.substring(0, 2)
                    + " (" + phone.substring(2,5)
                    + ") " + phone.substring(5, 8)
                    + " " + phone.substring(8, 10)
                    + " " + phone.substring(10);
        }
        return s;
    }
}
