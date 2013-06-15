package net.codeforeurope.amsterdam;

import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ApiConstants;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CheckinDialogFragment extends DialogFragment {

	Waypoint currentTarget;
	
	public CheckinDialogFragment() {
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    
	    currentTarget = getArguments().getParcelable(ApiConstants.CURRENT_TARGET);
	    
//	    Activity a = (NavigateRouteActivity)getActivity();
//	    Log.d("checkindialog",a.currentTarget.getLocalizedName());
	    //not working
	    
	    LayoutInflater inflater = getActivity().getLayoutInflater();

	    LinearLayout waypointLayout = (LinearLayout) inflater.inflate(
				R.layout.dialog_checkin, null);
	    TextView checkinTitle = (TextView) waypointLayout
				.findViewById(R.id.checkin_title);
	    ImageView checkinImage = (ImageView) waypointLayout
				.findViewById(R.id.checkin_image);
	    checkinTitle.setText(currentTarget.getLocalizedName());
	    Bitmap photo = BitmapFactory.decodeFile(currentTarget.image.localPath);
		checkinImage.setImageBitmap(photo);
		
		
	    builder.setView(waypointLayout)
	    .setTitle("You are close!") //R.string.pick_color
	    // Add action buttons
           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   	//user wants to check in
            		//store the current check-in somewhere
            		//get the next
            		//set the navigation to the next
            		((NavigateRouteActivity) getActivity())
					.doNavigateToNextCheckin();
               }
           })
           .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   CheckinDialogFragment.this.getDialog().cancel();
               }
           });    
	    
//	           .setItems((new String[]{getString(R.string.yes),getString(R.string.no)}), 
//	        		   new DialogInterface.OnClickListener() {
//	               public void onClick(DialogInterface dialog, int which) {
//	               // The 'which' argument contains the index position
//	               // of the selected item
//	            	   switch(which)
//	            	   {
//	            	   case 0: //user wants to check in
//	            		   //store the current check-in somewhere
//	            		   //get the next
//	            		   //set the navigation to the next
//	            			((NavigateRouteActivity) getActivity())
//							.doNavigateToNextCheckin();
//	            		   break;
//	            	   case 1: //just dismiss dialog
//	            		   dismiss();
//	            		   
//	            		   break;
//	            	   }
//	           }
//	    });

	    return builder.create();
	}
	
	@Override
	public void onDismiss (DialogInterface dialog)
	{
		((NavigateRouteActivity) getActivity())
		   .checkInWindowOnScreen = false;
	}
	
	
//	/* The activity that creates an instance of this dialog fragment must
//     * implement this interface in order to receive event callbacks.
//     * Each method passes the DialogFragment in case the host needs to query it. */
//    public interface NoticeDialogListener {
//        public void onDialogPositiveClick(DialogFragment dialog);
//        public void onDialogNegativeClick(DialogFragment dialog);
//    }
//    
//    // Use this instance of the interface to deliver action events
//    NoticeDialogListener mListener;
//    
//    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        // Verify that the host activity implements the callback interface
//        try {
//            // Instantiate the NoticeDialogListener so we can send events to the host
//            mListener = (NoticeDialogListener) activity;
//            //TODO : send the event to the host
//        } catch (ClassCastException e) {
//            // The activity doesn't implement the interface, throw exception
//            throw new ClassCastException(activity.toString()
//                    + " must implement NoticeDialogListener");
//        }
//    }
	
	
}


