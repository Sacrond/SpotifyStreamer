package com.spotifystreamer.testing.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sacrond on 6/23/2015.
 */

public class ArtistAdapter extends ArrayAdapter<Artist> {

    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        super(context, 0, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Artist artist = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
        }

        // Lookup view for data population
        ImageView artistThumbnail = (ImageView) convertView.findViewById(R.id.search_list_item_artist_thumbnail);
        TextView artistName = (TextView) convertView.findViewById(R.id.search_list_item_artist_name);

        // Populate the data into the template view using the data object
        artistThumbnail.setImageDrawable(artist.thumbnail);
        artistName.setText(artist.name);

        return convertView;
    }
}