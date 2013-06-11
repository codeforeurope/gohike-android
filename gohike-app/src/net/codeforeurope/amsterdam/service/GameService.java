package net.codeforeurope.amsterdam.service;

import java.util.Date;

import net.codeforeurope.amsterdam.model.Checkin;
import android.app.IntentService;
import android.content.Intent;

public class GameService extends IntentService {

	private static String NAME = "GameService";
	
	public GameService() {
		super(NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}
	
	public void doCheckin()
	{
		Checkin c = new Checkin( null);
		c.locationId = 1;
		c.routeId = 1;
		c.timestamp = new Date();
//		c.uploaded = false; //TODO: manage upload of check-ins
		
		
				
	}

}
