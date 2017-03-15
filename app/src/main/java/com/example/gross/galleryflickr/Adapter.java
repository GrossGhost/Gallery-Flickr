package com.example.gross.galleryflickr;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.gross.galleryflickr.model.Photo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Adapter extends ArrayAdapter<Photo> {

    List<Photo> mPhotoList;
    Context mContext;
    Picasso mPicasso;

    public Adapter(Context mContext, List<Photo> objects) {
        super(mContext, 0, objects);
        this.mContext = mContext;
        mPhotoList = objects;
        mPicasso = Picasso.with(mContext);
    }

    @Nullable
    @Override
    public Photo getItem(int position) {
        return mPhotoList.get(position);
    }

    @Override
    public int getCount() {
        return mPhotoList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,375));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(3,3,3,3);
        } else {
            imageView = (ImageView) convertView;
        }
        Photo photo = getItem(position);
        mPicasso.load(photo.getUrl()).into(imageView);
        return imageView;
    }
}
