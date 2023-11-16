package com.example.loginauth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ResponsesAdapter extends ArrayAdapter<String> {

    public ResponsesAdapter(Context context, List<String> responses) {
        super(context, 0, responses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        // Get the response at the current position
        String response = getItem(position);

        // Set the response text to the TextView in the list item layout
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(response);

        return convertView;
    }
}
