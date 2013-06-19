package net.codeforeurope.amsterdam.dialog;

import net.codeforeurope.amsterdam.R;
import net.codeforeurope.amsterdam.SplashActivity;
import net.codeforeurope.amsterdam.R.string;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class UpdateContentDialogFragment extends DialogFragment {
	private float contentSizeMB = 0;

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		((SplashActivity) getActivity()).skipUpdate();
		super.onCancel(dialog);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onDismiss(dialog);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.update_dialog_title)
				.setMessage(
						getString(R.string.update_dialog_message,
								this.contentSizeMB))
				.setPositiveButton(R.string.update_dialog_yes,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((SplashActivity) getActivity())
										.doUpdateContent();
							}
						})
				.setNegativeButton(R.string.update_dialog_no,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((SplashActivity) getActivity())
										.skipUpdate();
							}
						});
		// Create the AlertDialog object and return it
		return builder.create();
	}

	public void setContentSize(int size) {
		this.contentSizeMB = (float) size / ((float) 1024 * 1024);

	}

}
