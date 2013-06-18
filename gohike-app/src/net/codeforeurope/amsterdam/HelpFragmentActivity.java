package net.codeforeurope.amsterdam;

import java.util.ArrayList;
import java.util.List;

import net.codeforeurope.amsterdam.view.HelpFragment;
import net.codeforeurope.amsterdam.view.HelpPagerAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import android.support.v4.view.ViewPager;


public class HelpFragmentActivity extends FragmentActivity {

	public HelpFragmentActivity() {
		// TODO Auto-generated constructor stub
	}

	HelpPagerAdapter pageAdapter;
	
	private List<Fragment> getFragments(){
		  List<Fragment> fList = new ArrayList<Fragment>();
		 
		  fList.add(HelpFragment.newInstance(getString(R.string.help1), 1));
		  fList.add(HelpFragment.newInstance(getString(R.string.help2), 2)); 
		  fList.add(HelpFragment.newInstance(getString(R.string.help3), 3));
		  fList.add(HelpFragment.newInstance(getString(R.string.help4), 4));
		 
		  return fList;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.dialog_howtoplay);
      List<Fragment> fragments = getFragments();
      pageAdapter = new HelpPagerAdapter(getSupportFragmentManager(), fragments);
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
