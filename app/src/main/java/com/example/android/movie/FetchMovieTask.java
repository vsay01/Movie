package com.example.android.movie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FetchMovieTask extends AsyncTask<String, Void, List<Movie>> {
    private Context cnt;
    private GridView gv;

    private final String LOG_TAG = FetchMovieTask.class.getSimpleName();


    FetchMovieTask(Context context, GridView gv){
        this.cnt = context;
        this.gv = gv;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    List<Movie> listMovie = new ArrayList<>();
    private List<Movie> getMovieDataFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String BASE_URL = cnt.getString(R.string.img_base_url);
        final String IMAGE_SIZE = cnt.getString(R.string.img_size);
        final String MOVIE_LIST = "results";
        final String MOVIE_ORIGINAL_TITLE = "original_title";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_OVERVIEW = "overview";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_VOTE_AVERAGE = "vote_average";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);

        for(int i = 0; i < movieArray.length(); i++) {

            // Get the JSON object representing the day
            JSONObject objMovie = movieArray.getJSONObject(i);

            listMovie.add(new Movie(
                    objMovie.getString(MOVIE_ORIGINAL_TITLE),
                    BASE_URL+IMAGE_SIZE+ objMovie.getString(MOVIE_POSTER_PATH),
                    objMovie.getString(MOVIE_OVERVIEW),
                    objMovie.getString(MOVIE_VOTE_AVERAGE),
                    objMovie.getString(MOVIE_RELEASE_DATE)
                    ));
        }

        for (Movie s : listMovie) {
            Log.v(LOG_TAG, "Forecast entry: " + s.getPoster_path());
        }
        return listMovie;

    }

    @Override
    protected List<Movie> doInBackground(String... params) {
        //if there is no parameter zip code nothing to look up
        if (params.length == 0){
            return null;
        }

        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String sort = cnt.getString(R.string.sort_param);
        String api_key = cnt.getString(R.string.api_key_param);
        int numDays = 7;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            // Construct the URL for the OpenWeatherMap query
            // Possible parameters are avaiable at OWM's forecast API page, at
            // http://openweathermap.org/API#forecast

            final String FORECAST_BASE_URL = "http://api.themoviedb.org/3/discover/movie?";
            final String SORT_PARAM = sort;
            final String API_KEY_PARAM = api_key;
            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(SORT_PARAM, params[0])
                    .appendQueryParameter(API_KEY_PARAM, params[1])
                    .build();

            URL url = new URL(builtUri.toString());
            Log.v(LOG_TAG, "Built URI " + builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }


        try {
            return getMovieDataFromJson(movieJsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Movie> result) {
        super.onPostExecute(result);
        if (result !=null){
            gv.setAdapter(new GridViewAdapter(cnt, (new ArrayList<>(result))));
            gv.setOnScrollListener(new ScrollListener(cnt));
            final List<Movie>res = new ArrayList<Movie>(result);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String original_title = res.get(position).getOriginal_title();
                    String poster_path = res.get(position).getPoster_path();
                    String overview = res.get(position).getOverview();
                    String release_date = res.get(position).getRelease_date();
                    String vote_average = res.get(position).getVote_average();

                    Bundle extras = new Bundle();
                    extras.putString("EXTRA_ORIGINAL_TITLE",original_title);
                    extras.putString("EXTRA_POSTER_PATH", poster_path);
                    extras.putString("EXTRA_OVERVIEW", overview);
                    extras.putString("EXTRA_RELEASE_DATE", release_date);
                    extras.putString("EXTRA_VOTE_AVG", vote_average);
                    Intent detailIntent = new Intent(cnt, DetailMovieActivity.class)
                            .putExtras(extras);
                    cnt.startActivity(detailIntent);
                }
            });
        }
    }
}