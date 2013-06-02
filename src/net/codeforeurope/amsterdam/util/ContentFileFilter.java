package net.codeforeurope.amsterdam.util;

import java.io.File;
import java.io.FilenameFilter;

public class ContentFileFilter implements FilenameFilter {

	@Override
	public boolean accept(File dir, String filename) {
		return filename.endsWith(".json");
	}

}
