package com.example.android.movie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailMovieActivityFragment extends Fragment {

    private TextView original_title_tv;
    private ImageView poster_path_iv;
    private TextView overview_tv;
    private TextView release_date_tv;
    private TextView vote_average_tv;
    private static final int MAX_WIDTH = 50;
    private static final int MAX_HEIGHT = 500;

    public DetailMovieActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail_movie, container, false);

        Bundle extras = getActivity().getIntent().getExtras();

        String original_title = extras.getString("EXTRA_ORIGINAL_TITLE");
        String poster_path = extras.getString("EXTRA_POSTER_PATH");
        String overview = extras.getString("EXTRA_OVERVIEW");
        String release_date = extras.getString("EXTRA_RELEASE_DATE");
        String vote_average = extras.getString("EXTRA_VOTE_AVG");

        //Log.d("FETECH RELEASE DATE", release_date);
        //Log.d("FETECH RATING", vote_average);

        original_title_tv = (TextView)rootView.findViewById(R.id.original_title);
        poster_path_iv = (ImageView)rootView.findViewById(R.id.poster);
        overview_tv = (TextView)rootView.findViewById(R.id.overview);
        release_date_tv = (TextView)rootView.findViewById(R.id.release_date);
        vote_average_tv = (TextView)rootView.findViewById(R.id.vote_average);

        original_title_tv.setText(original_title);
        //poster_path_iv.setImageDrawable(R.drawable.error);
        int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
        //Picasso.with(getActivity()).setIndicatorsEnabled(true);
        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(getActivity()) //
                .load(poster_path) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.error) //
                .tag(getActivity()) //
                .fit()
                //.transform(new BitmapTransform(MAX_WIDTH, MAX_HEIGHT))
                .into(poster_path_iv);
        //Picasso.with(getActivity()).getSnapshot().dump();
        overview_tv.setText(overview);
        if(release_date.equals("null"))
            release_date_tv.setText("");
        else
            release_date_tv.setText(release_date.substring(0,4));
        vote_average_tv.setText(vote_average+"/10");

        return rootView;
    }
}
