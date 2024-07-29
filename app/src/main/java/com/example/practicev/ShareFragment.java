package com.example.practicev;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ShareFragment extends Fragment {

    // Declare a Button variable for the showMap button
    Button showMap;

    // This method is called when the Fragment's view is being created
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment from the fragment_share.xml file
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        // Initialize views by finding them from the inflated layout
        showMap = view.findViewById(R.id.showMap);

        // Set an onClick listener for the showMap button
        showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an Intent to start the MapsActivity
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                // Start the MapsActivity
                startActivity(intent);
            }
        });

        // Return the created view
        return view;
    }
}
