package net.codeforeurope.amsterdam.view;

import net.codeforeurope.amsterdam.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyFragment extends Fragment {
	 public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";
	 public static final String EXTRA_NUMBER = "EXTRA_FILENAME";
	 
	 public static final MyFragment newInstance(String message, int number)
	 {
	   MyFragment f = new MyFragment();
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
	   LinearLayout v = (LinearLayout)inflater.inflate(R.layout.help_fragment1, null);
	   TextView messageTextView = (TextView)v.findViewById(R.id.text_help1);
	   messageTextView.setText(message);
	   
	   ImageView checkinImage = (ImageView) v.findViewById(R.id.image_help1);

	   switch(number)
	   {
	   case 1:
		   checkinImage.setImageResource(R.drawable.help1);
		   break;
	   case 2:
		   checkinImage.setImageResource(R.drawable.help1);
		   break;
	   case 3:
		   checkinImage.setImageResource(R.drawable.help1);
		   break;
	   case 4:
		   checkinImage.setImageResource(R.drawable.help1);
		   break;
		   
	   }
	    
	   return v;
	 }
	}