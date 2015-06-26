package com.spotifystreamer.testing.spotifystreamer;

import android.app.Fragment;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

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
import java.util.Timer;
import java.util.TimerTask;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArtistAdapter adapter = null;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ArrayList<Artist> arrayOfArtist = new ArrayList<>();
        adapter = new ArtistAdapter(getActivity(), arrayOfArtist);

        ListView listView = (ListView) rootView.findViewById(R.id.search_list_view);
        listView.setAdapter(adapter);

        final EditText searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            private Timer timer = new Timer();
            private final long DELAY = 500; // in ms

            @Override
            public void afterTextChanged(final Editable s) {

                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        new FetchArtistTask().execute(s.toString());
                    }
                }, DELAY);


            }
        });


//        Artist tempArtist;
//
//        for (int i = 0; i < 20; i++) {
//            tempArtist = new Artist(Integer.toString(i), "https://i.scdn.co/image/a31006f22ea598522c0e2136c9e6365c62a8a4f0");
//            adapter.add(tempArtist);
//        }

//        Button removeButton = (Button) rootView.findViewById(R.id.remove_data_button);
//
//        removeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FetchArtistTask artistTask = new FetchArtistTask();
//
//                artistTask.execute(searchEditText.getText().toString());
//
//                if (arrayOfArtist.size() > 0) {
//                    arrayOfArtist.remove(arrayOfArtist.size() - 1);
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });

        return rootView;
    }

    public class FetchArtistTask extends AsyncTask<String, Void, Artist[]> {

        private final String LOG_TAG = FetchArtistTask.class.getSimpleName();

        @Override
        protected void onPostExecute(Artist[] result) {

            if (result != null) {
                adapter.clear();
                adapter.addAll(result);
            }

        }

        private Artist[] getArtistFromJson (String artistJsonStr)
            throws JSONException {

            int numOfArtists;

            final String JSON_ARISTS = "artists";
            final String JSON_ITEMS = "items";
            final String JSON_IMAGES = "images";
            final String JSON_NAME = "name";
            final String JSON_TOTAL = "total";
            final String JSON_IMAGE_URL = "url";

            JSONObject mainArtistObject= new JSONObject(artistJsonStr);
            JSONObject artistObject = mainArtistObject.getJSONObject(JSON_ARISTS);
            JSONArray artistsArray = artistObject.getJSONArray(JSON_ITEMS);

            numOfArtists = artistObject.getInt("total");

            if (numOfArtists > 20) { numOfArtists = 20; }

            Artist[] resultArtists = new Artist[numOfArtists];

            if (artistsArray.length() == 0) { return null; }

            for (int i = 0, x = artistsArray.length(); i < x; i++) {

                String name;
                String imageUrl = null;

                JSONObject individualArtistObject = artistsArray.getJSONObject(i);
                name = individualArtistObject.getString(JSON_NAME);

                JSONArray artistImage = individualArtistObject.getJSONArray(JSON_IMAGES);

                if (artistImage.length() != 0) {
                    JSONObject imageObject = artistImage.getJSONObject(artistImage.length() - 1);

                    imageUrl = imageObject.getString(JSON_IMAGE_URL);
                }

                Artist artist = new Artist(name, imageUrl);
                resultArtists[i] = artist;
            }

            return resultArtists;
        }

        @Override
        protected Artist[] doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            // These two are declared outside the try/catch block so
            // they can be closed at the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as string
            String artistJsonStr = null;

            String artistName = params[0];
            String type = "artist";
            String limit = "20";

            if (artistName.isEmpty()) {
                return null;
            }

            try {
                final String ARTIST_BASE_URL = "https://api.spotify.com/v1/search?";
                final String QUERY_PARAM = "q";
                final String LIMIT_PARAM = "limit";
                final String TYPE_PARAM = "type";

                Uri builtUri = Uri.parse(ARTIST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, artistName)
                        .appendQueryParameter(LIMIT_PARAM, limit)
                        .appendQueryParameter(TYPE_PARAM, type)
                        .build();

                URL url = new URL(builtUri.toString());

                // Create the request to Spotify API, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream from Spotify API into string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                artistJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error: ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                if (reader != null) {

                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error closing reader: ", e);
                    }
                }
            }

            try {
                return getArtistFromJson(artistJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error: ", e);
                e.printStackTrace();
            }

            return null;
        }
    }
}
