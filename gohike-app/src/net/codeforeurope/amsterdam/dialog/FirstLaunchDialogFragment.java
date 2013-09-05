package net.codeforeurope.amsterdam.dialog;

import net.codeforeurope.amsterdam.R;
import net.codeforeurope.amsterdam.SplashActivity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class FirstLaunchDialogFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.first_launch_title)
				.setMessage(R.string.first_launch_message)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((SplashActivity) getActivity())
										.gotoCityList(true);
							}
						});

		// Create the AlertDialog object and return it
		return builder.create();
	}
}
