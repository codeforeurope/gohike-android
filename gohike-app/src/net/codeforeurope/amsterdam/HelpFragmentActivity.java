package net.codeforeurope.amsterdam;

import java.util.ArrayList;
import java.util.List;

import net.codeforeurope.amsterdam.view.MyFragment;
import net.codeforeurope.amsterdam.view.MyPagerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.GetChars;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpFragmentActivity extends FragmentActivity {

	public HelpFragmentActivity() {
		// TODO Auto-generated constructor stub
	}

	MyPagerAdapter pageAdapter;
	
	private List<Fragment> getFragments(){
		  List<Fragment> fList = new ArrayList<Fragment>();
		 
		  fList.add(MyFragment.newInstance("Fragment 1", 1));
		  fList.add(MyFragment.newInstance("Fragment 2", 2)); 
		  fList.add(MyFragment.newInstance("Fragment 3", 3));
		  fList.add(MyFragment.newInstance("Fragment 4", 4));
		 
		  return fList;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dialog_howtoplay);
      List<Fragment> fragments = getFragments();
      pageAdapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
      ViewPager pager = (ViewPager)findViewById(R.id.viewpager);
      pager.setAdapter(pageAdapter);
    }
	
	
//	@Override
//	public Fra onCreateDialog(Bundle savedInstanceState) {
//	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//	    List<Fragment> fragments = getFragments();
//	    pageAdapter = new MyPagerAdapter(getFragmentManager(), fragments);
//	    
//	    LayoutInflater inflater = getActivity().getLayoutInflater();
//	    LinearLayout waypointLayout = (LinearLayout) inflater.inflate(
//				R.layout.dialog_howtoplay, null);
////	    TextView checkinTitle = (TextView) waypointLayout
////				.findViewById(R.id.checkin_title);
////	    ImageView checkinImage = (ImageView) waypointLayout
////				.findViewById(R.id.checkin_image);
////	    checkinTitle.setText(currentTarget.getLocalizedName());
////	    Bitmap photo = BitmapFactory.decodeFile(currentTarget.image.localPath);
////		checkinImage.setImageBitmap(photo);
//		
//		
//	    builder.setView(waypointLayout)
//	    .setTitle(R.string.checkin_close) //R.string.pick_color
//	    // Add action buttons
//           .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//               @Override
//               public void onClick(DialogInterface dialog, int id) {
//					//User wants to close dialog
//               }
//           });    
//	    
//	    return builder.create();
//	}
	
}
