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

	private List<Fragment> getFragments() {
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
		pageAdapter = new HelpPagerAdapter(getSupportFragmentManager(),
				fragments);
		ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
		pager.setAdapter(pageAdapter);
	}

}
