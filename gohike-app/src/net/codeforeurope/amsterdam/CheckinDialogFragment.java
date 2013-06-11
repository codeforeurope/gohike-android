package net.codeforeurope.amsterdam;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class CheckinDialogFragment extends DialogFragment {

	public CheckinDialogFragment() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Want to check in?") //R.string.pick_color
	           .setItems((new String[]{"Yes","No"}), new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int which) {
	               // The 'which' argument contains the index position
	               // of the selected item
	            	   switch(which)
	            	   {
	            	   case 0: //user wants to check in
	            		   //store the current check-in somewhere
	            		   //get the next
	            		   //set the navigation to the next
	            			((NavigateRouteActivity) getActivity())
							.doNavigateToNextCheckin();
	            		   break;
	            	   case 1: //just dismiss dialog
	            		   dismiss();
	            		   break;
	            	   }
	           }
	    });
	    return builder.create();
	}
}
