package net.codeforeurope.amsterdam.model;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class CheckinExclusionStrategy implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		// TODO Auto-generated method stub

		return (f.getDeclaringClass() == Checkin.class && f.getName().equals(
				"id"));
	}

}
