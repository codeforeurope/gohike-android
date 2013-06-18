package net.codeforeurope.amsterdam.view;

import net.codeforeurope.amsterdam.HelpFragmentActivity;
import net.codeforeurope.amsterdam.R;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HelpFragment extends Fragment implements OnClickListener {
	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	 public static final String EXTRA_NUMBER = "EXTRA_FILENAME";
	 
	 public static final HelpFragment newInstance(String message, int number)
	 {
	   HelpFragment f = new HelpFragment();
	   Bundle bdl = new Bundle(1);
	   bdl.putString(EXTRA_MESSAGE, message);
	   bdl.putInt(EXTRA_NUMBER, number);
	   f.setArguments(bdl);
	   return f;
	 }
	 
	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	   Bundle savedInstanceState) {
	   String message = getArguments().getString(EXTRA_MESSAGE);
	   int number = getArguments().getInt(EXTRA_NUMBER);
	   View v = inflater.inflate(R.layout.help_fragment, container, false);	  
	   TextView messageTextView = (TextView)v.findViewById(R.id.text_help);
	   Button b = (Button)v.findViewById(R.id.button_help);
	   messageTextView.setText(message);
	   
	   ImageView checkinImage = (ImageView) v.findViewById(R.id.image_help);

	   switch(number)
	   {
	   case 1:
		   checkinImage.setImageResource(R.drawable.help1);
		   break;
	   case 2:
		   checkinImage.setImageResource(R.drawable.help2);
		   break;
	   case 3:
		   checkinImage.setImageResource(R.drawable.help3);
		   break;
	   case 4:
		   checkinImage.setImageResource(R.drawable.help4);
		   b.setVisibility(0); //visible
		   b.setOnClickListener(this);
		   break;
		   
	   }
	    
	   return v;
	 }

	@Override
	public void onClick(View v) {
		((HelpFragmentActivity)getActivity()).finish();
		}
	}