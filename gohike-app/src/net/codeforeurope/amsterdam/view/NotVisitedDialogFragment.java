package net.codeforeurope.amsterdam.view;

import net.codeforeurope.amsterdam.R;
import net.codeforeurope.amsterdam.RouteDetailActivity;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class NotVisitedDialogFragment extends DialogFragment {

	public NotVisitedDialogFragment() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// 2. Chain together various setter methods to set the dialog
		// characteristics
		builder.setMessage(R.string.not_found_dialog_message).setTitle(R.string.not_found_dialog_title)
				.setNegativeButton(R.string.not_found_dialog_no, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// User cancelled the dialog
						dismiss();
					}
				}).setPositiveButton(R.string.not_found_dialog_yes, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {
						// User clicked Yes
						Waypoint waypoint = (Waypoint) getArguments().get(DataConstants.WAYPOINT);
						((RouteDetailActivity) getActivity()).startHike(waypoint);
					}

				});

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();

		return dialog; // super.onCreateDialog(savedInstanceState);
	}

}
