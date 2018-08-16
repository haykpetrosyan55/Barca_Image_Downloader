package com.BarcaImageDownloader.BarcaImageDownloader.BarcaImageDownloader;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class ImageProvider {
    private static List<ImageModel> list = new ArrayList<>();
    public static int position;

    public static List<ImageModel> getList(Context context) {
        if (!list.isEmpty()) {
            list.clear();
        }
        for (int i = 0; i <context.getResources().getStringArray(R.array.imageUrls).length; i++) {
            list.add(new ImageModel (context.getResources().getStringArray(R.array.imageName)[i],
                    context.getResources().getStringArray(R.array.imageUrls)[i], false));
        }
        return list;
    }
    public static ImageModel getPosition() {
        return list.get(position);
    }
}
