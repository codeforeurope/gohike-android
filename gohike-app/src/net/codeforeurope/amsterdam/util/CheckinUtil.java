package net.codeforeurope.amsterdam.util;

import java.util.Date;

import net.codeforeurope.amsterdam.model.Checkin;
import net.codeforeurope.amsterdam.model.Waypoint;

public class CheckinUtil {

	public static Checkin fromWaypoint(Waypoint waypoint) {
		Checkin checkin = new Checkin();
		checkin.routeId = waypoint.routeId;
		checkin.locationId = waypoint.getId();
		checkin.timestamp = new Date();
		return checkin;
	}

}
