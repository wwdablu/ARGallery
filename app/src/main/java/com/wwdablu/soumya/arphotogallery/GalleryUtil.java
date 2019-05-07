package com.wwdablu.soumya.arphotogallery;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.LinkedList;

public final class GalleryUtil {

    private GalleryUtil() {
        //
    }

    public static boolean isPermitted(@NonNull Context context) {
        int permission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_EXTERNAL_STORAGE);

        return (permission == PackageManager.PERMISSION_GRANTED);
    }

    public static LinkedList<String> getAllImages(@NonNull Context context) {

        LinkedList<String> paths = new LinkedList<>();

        final String[] columns = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID };
        String selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?";
        String[] selectionArgs = new String[] {"Camera"};
        final String orderBy = MediaStore.Images.Media.DATE_ADDED;

        //Stores all the images from the gallery in Cursor
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, selection, selectionArgs, orderBy);

        //Check if the cursor has valid data
        if(cursor == null) {
            return paths;
        }

        if(!cursor.moveToFirst()) {
            cursor.close();
            return paths;
        }

        do {
            paths.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
        } while (cursor.moveToNext());

        cursor.close();
        return paths;
    }
}
