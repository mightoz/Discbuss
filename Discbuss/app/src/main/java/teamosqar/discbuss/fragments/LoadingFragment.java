package teamosqar.discbuss.fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import teamosqar.discbuss.activities.R;

/**
 * Created by joakim on 2015-10-07.
 *
 * Fragment class which displays a loading animation
 */
public class LoadingFragment extends DialogFragment {

    public LoadingFragment(){

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

}
