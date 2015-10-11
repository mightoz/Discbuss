package teamosqar.discbuss.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;
import android.content.ContextWrapper;

/**
 * Created by Oskar on 2015-10-11.
 */
public class GenderChoiceList extends DialogFragment {

    final CharSequence[] items = {"Male", "Female", "Unspecified"};
    public String selection;

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Specify Gender").setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        selection = (String) items[which];
                        break;
                    case 1:
                        selection = (String) items[which];
                        break;
                    case 2:
                        selection = (String) items[which];
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which){
                Toast.makeText(getActivity(), selection +  " saved.", Toast.LENGTH_SHORT).show();
            }

        });
        return builder.create();
    }
    public String getSelection(){
        if(selection != null){
            return selection;
        }
        else{
            return "noGenderSpecified";
        }

    }

}
