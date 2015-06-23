package com.spotifystreamer.testing.spotifystreamer;

import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        ArrayList<Artist> arrayOfArtist = new ArrayList<Artist>();

        ArtistAdapter adapter = new ArtistAdapter(getActivity(), arrayOfArtist);

        ListView listView = (ListView) rootView.findViewById(R.id.search_list_view);
        listView.setAdapter(adapter);

        Artist temp = null;

        for (int i = 0; i < 1000; i++) {
            try {
                temp = new Artist(Integer.toString(i), getActivity().getPackageManager().getApplicationIcon(getActivity().getPackageName()));
                adapter.add(temp);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        Log.e("TESTINGGGGGGG", Integer.toString(arrayOfArtist.size()));

        return rootView;
    }
}
