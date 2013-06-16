package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FoundDialogFragment extends DialogFragment {

	Waypoint currentTarget;
	
	public FoundDialogFragment() {
		// TODO Auto-generated constructor stub
	}
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
	    currentTarget = getArguments().getParcelable(ApiConstants.CURRENT_TARGET);

	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    LinearLayout waypointLayout = (LinearLayout) inflater.inflate(
				R.layout.dialog_found, null);
	    TextView foundTitle = (TextView) waypointLayout
				.findViewById(R.id.found_title);
	    TextView foundText = (TextView) waypointLayout
				.findViewById(R.id.found_text);
	    foundTitle.setText(currentTarget.getLocalizedName());
	    foundText.setText(currentTarget.getLocalizedDescription());
		
	    builder.setView(waypointLayout)
	    .setTitle(R.string.found_title) //R.string.pick_color
	    // Add action buttons
           .setPositiveButton(R.string.route_continue, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   	//user wants to check in
            		//store the current check-in somewhere
            		//get the next
            		//set the navigation to the next
            		((NavigateRouteActivity) getActivity())
					.doNavigateToNextCheckin();
               }
           });
	    return builder.create();
	}
	
}
