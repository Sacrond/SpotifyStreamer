package com.spotifystreamer.testing.spotifystreamer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ArrayList<Artist> arrayOfArtist = new ArrayList<>();
        final ArtistAdapter adapter = new ArtistAdapter(getActivity(), arrayOfArtist);

        ListView listView = (ListView) rootView.findViewById(R.id.search_list_view);
        listView.setAdapter(adapter);

        Artist tempArtist;

        for (int i = 0; i < 20; i++) {
            tempArtist = new Artist(Integer.toString(i), "https://i.scdn.co/image/a31006f22ea598522c0e2136c9e6365c62a8a4f0");
            adapter.add(tempArtist);
        }

        Button removeButton = (Button) rootView.findViewById(R.id.remove_data_button);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayOfArtist.size() > 0) {
                    arrayOfArtist.remove(arrayOfArtist.size() - 1);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        return rootView;
    }
}
