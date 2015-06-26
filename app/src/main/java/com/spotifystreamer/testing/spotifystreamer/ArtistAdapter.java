package com.spotifystreamer.testing.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sacrond on 6/23/2015.
 */

public class ArtistAdapter extends ArrayAdapter<Artist> {

    private static class ViewHolder {
        TextView name;
        ImageView thumbnail;
    }

    public ArtistAdapter(Context context, ArrayList<Artist> artists) {
        super(context, R.layout.search_list_item, artists);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        Artist artist = getItem(position);

        // View lookup stored in cache
        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.search_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.search_list_item_artist_name);
            viewHolder.thumbnail = (ImageView) convertView.findViewById(R.id.search_list_item_artist_thumbnail);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.name.setText(artist.name);

        Picasso.with(getContext())
                .load(artist.thumbnailUrl)
                .into(viewHolder.thumbnail);

        // Return the completed view to render on screen
        return convertView;
    }
}