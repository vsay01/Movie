package com.example.android.movie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity", "OnStart");
        updatesMovies(this);
    }

    public void updatesMovies(Context context) {
        if (MainActivityFragment.gv != null){
            Log.d("MainActivity", "updateMoveis");
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            String sortBy = prefs.getString(getString(R.string.pref_sort_param), getString(R.string.sort_param_value_popular));
            if (sortBy.contains("pop"))
                getSupportActionBar().setTitle(getString(R.string.sort_param_popular)+" "+getString(R.string.app_name));
            else
                getSupportActionBar().setTitle(getString(R.string.sort_param_high) + " " + getString(R.string.app_name));
            new FetchMovieTask(context, MainActivityFragment.gv).execute(sortBy, getString(R.string.api_key_param_value));
        }
    }
}
