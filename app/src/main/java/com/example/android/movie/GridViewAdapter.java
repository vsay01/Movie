package com.example.android.movie;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vortana_say on 01/09/15.
 */
final class GridViewAdapter extends BaseAdapter{
    private final String LOG_TAG = GridViewAdapter.class.getSimpleName();
    public static List<Movie> movieList = new ArrayList<>();
    private static final int MAX_WIDTH = 540;
    private static final int MAX_HEIGHT = 839;

    private Context context;

    public GridViewAdapter(Context ctx,ArrayList<Movie> result) {
        this.context = context;

        // Ensure we get a different ordering of images on each run.
        //Collections.addAll(urls, Data.URLS);
        //Collections.shuffle(urls);

        // Triple up the list.
        //ArrayList<Movie> copy = new ArrayList<>(movieList);
        //movieList.addAll(copy);
        //movieList.addAll(copy);
        movieList=result;
        this.context=ctx;
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        PicassoImageView view = (PicassoImageView) convertView;
        if (view == null) {
            view = new PicassoImageView(context);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        //int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

        // Get the image URL for the current position.
        String url = ((Movie)(getItem(position))).getPoster_path();
        //debug purpose
        //Picasso.with(context).setIndicatorsEnabled(true);
        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context) //
                .load(url) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .tag(context) //
                        .fit()
                //.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                //.resize(size, size)
                .into(view);
        //Picasso.with(context).getSnapshot().dump();
        return view;
    }

    @Override public int getCount() {
        return movieList.size();
    }

    @Override public Object getItem(int position) {
        return movieList.get(position);
    }

    @Override public long getItemId(int position) {
        return position;
    }
}
