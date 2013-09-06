package net.codeforeurope.amsterdam.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

import net.codeforeurope.amsterdam.model.BaseModel;
import net.codeforeurope.amsterdam.model.Image;
import net.codeforeurope.amsterdam.model.Profile;
import net.codeforeurope.amsterdam.model.Reward;
import net.codeforeurope.amsterdam.model.Route;
import net.codeforeurope.amsterdam.model.Waypoint;
import net.codeforeurope.amsterdam.util.ActionConstants;
import net.codeforeurope.amsterdam.util.ContentServicesHelper;
import net.codeforeurope.amsterdam.util.DataConstants;
import android.app.IntentService;
import android.content.Intent;

public class ImageDownloadService extends IntentService {
	private static String NAME = "ImageDownloadService";

	private HashMap<String, File> directories = new HashMap<String, File>();

	private long imagesToDownload = 0;

	private long imagesDownloaded = 0;

	/**
	 * Sets up references for 4 directories where the images would be stored
	 * under.
	 */
	public ImageDownloadService() {
		super(NAME);

	}

	@Override
	protected void onHandleIntent(Intent intent) {
		ensureDownloadDirectories();
		Intent broadcastIntent = new Intent(ActionConstants.IMAGE_DOWNLOAD_COMPLETE);

		try {
			if (ActionConstants.CATALOG_DOWNLOAD_COMPLETE.equals(intent.getAction())) {
				ArrayList<Profile> profiles = intent.getParcelableArrayListExtra(DataConstants.CATALOG_PROFILES);
				setNumberOfImagesToDownload(profiles);
				downloadImagesForProfiles(profiles);
				broadcastIntent.putExtra(DataConstants.CATALOG_PROFILES, profiles);
			} else {
				if (intent.hasExtra(DataConstants.DOWNLOADED_ROUTE)) {
					Route route = intent.getParcelableExtra(DataConstants.DOWNLOADED_ROUTE);
					setNumberOfImagesToDownload(route);
					downloadImages(route);
					broadcastIntent.putExtra(DataConstants.DOWNLOADED_ROUTE, route);
				} else {
					ArrayList<Route> routes = intent.getParcelableArrayListExtra(DataConstants.LOCAL_ROUTES);
					setNumberOfImagesToDownload(routes);
					downloadImagesForRoutes(routes);
					broadcastIntent.putExtra(DataConstants.LOCAL_ROUTES, routes);
				}
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sendBroadcast(broadcastIntent);
	}

	private void setNumberOfImagesToDownload(Route route) {
		this.imagesToDownload += route.getNumberOfImages();
	}

	private void setNumberOfImagesToDownload(ArrayList<? extends BaseModel> models) {
		for (BaseModel model : models) {
			this.imagesToDownload += model.getNumberOfImages();
		}

	}

	private void ensureDownloadDirectories() {
		directories.put(Profile.class.getSimpleName().toLowerCase(),
				ContentServicesHelper.ensureImagesDirectory(getBaseContext(), Profile.class));
		directories.put(Route.class.getSimpleName().toLowerCase(),
				ContentServicesHelper.ensureImagesDirectory(getBaseContext(), Route.class));
		directories.put(Waypoint.class.getSimpleName().toLowerCase(),
				ContentServicesHelper.ensureImagesDirectory(getBaseContext(), Waypoint.class));
		directories.put(Reward.class.getSimpleName().toLowerCase(),
				ContentServicesHelper.ensureImagesDirectory(getBaseContext(), Reward.class));
	}

	/**
	 * Downloads the image for Profile and kicks off download for routes
	 * 
	 * @param profile
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImages(Profile profile) throws MalformedURLException, IOException {
		File imagesDirectory = ensureImagesDirectory(profile);
		downloadImage(imagesDirectory, profile.image);
		downloadImagesForRoutes(profile.routes);
	}

	/**
	 * Downloads the image for Route and kicks off download for Waypoints and
	 * Reward if available. Also the entry point for downloading images for a
	 * route.
	 * 
	 * @param route
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImages(Route route) throws MalformedURLException, IOException {
		File imagesDirectory = ensureImagesDirectory(route);
		downloadImage(imagesDirectory, route.image);
		downloadImage(imagesDirectory, route.icon);
		downloadImagesForWaypoints(route.waypoints);
		downloadImages(route.reward);

	}

	/**
	 * Downloads the image for Reward if available
	 * 
	 * @param reward
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImages(Reward reward) throws MalformedURLException, IOException {
		if (reward != null) {
			File imagesDirectory = ensureImagesDirectory(reward);
			downloadImage(imagesDirectory, reward.image);
		}

	}

	/**
	 * Downloads the image for Waypoint
	 * 
	 * @param reward
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImages(Waypoint waypoint) throws MalformedURLException, IOException {
		File imagesDirectory = ensureImagesDirectory(waypoint);
		downloadImage(imagesDirectory, waypoint.image);

	}

	/**
	 * Entry point for downloading images for catalog response
	 * 
	 * @param profiles
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImagesForProfiles(ArrayList<Profile> profiles) throws MalformedURLException, IOException {

		for (Profile profile : profiles) {
			downloadImages(profile);

		}
	}

	/**
	 * Entry point for downloading images for routes
	 * 
	 * @param profiles
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImagesForRoutes(ArrayList<Route> routes) throws MalformedURLException, IOException {
		for (Route route : routes) {
			downloadImages(route);
		}
	}

	/**
	 * @param waypoints
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImagesForWaypoints(ArrayList<Waypoint> waypoints) throws MalformedURLException, IOException {
		for (Waypoint waypoint : waypoints) {
			downloadImages(waypoint);
		}
	}

	/**
	 * Downloads the specified image into the correct directory
	 * 
	 * @param imagesDirectory
	 * @param image
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private void downloadImage(File imagesDirectory, Image image) throws MalformedURLException, IOException {

		File imageFile = new File(imagesDirectory, image.md5 + ".jpg");
		if (!imageFile.exists()) {
			image.localPath = ContentServicesHelper.writeOutImageFile(image, imageFile);
		} else {
			image.localPath = imageFile.getPath();
		}
		notifyImageDownloaded();

	}

	/**
	 * handles sending out progress notifications to activity
	 */
	private void notifyImageDownloaded() {
		imagesDownloaded++;
		Intent intent = new Intent(ActionConstants.IMAGE_DOWNLOAD_PROGRESS);
		intent.putExtra(DataConstants.IMAGE_DOWNLOAD_PROGRESS, (int) imagesDownloaded);
		intent.putExtra(DataConstants.IMAGE_DOWNLOAD_TARGET, (int) imagesToDownload);
		sendBroadcast(intent);
	}

	/**
	 * Handles all the setup to make sure there is a directory for the images
	 * 
	 * @param model
	 * @return
	 */
	private File ensureImagesDirectory(BaseModel model) {
		String className = model.getClass().getSimpleName().toLowerCase();
		File imagesDirectory = ContentServicesHelper.ensureSubDirectory(String.valueOf(model.id),
				directories.get(className));
		return imagesDirectory;
	}
}
