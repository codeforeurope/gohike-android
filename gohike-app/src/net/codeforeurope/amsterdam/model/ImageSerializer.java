package net.codeforeurope.amsterdam.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import net.codeforeurope.amsterdam.util.StringUtils;

import android.util.Base64;
import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ImageSerializer implements JsonSerializer<Image>,
		JsonDeserializer<Image> {

	private File contentDirectory;

	public ImageSerializer(File contentFile) {
		this.contentDirectory = contentFile;
	}

	@Override
	public Image deserialize(JsonElement element, Type type,
			JsonDeserializationContext serializationContext)
			throws JsonParseException {
		long start, end;

		File imagesDirectory = new File(contentDirectory, "images");
		Image image = new Image();
		start = System.currentTimeMillis();
		String imageData = element.getAsString();
		end = System.currentTimeMillis();
		Log.i("ImageSerializer", "Time to getAsString: " + (end - start) + "ms");
		try {
			if (!imagesDirectory.exists()) {
				imagesDirectory.mkdir();
			}
			start = System.currentTimeMillis();
			String checksum = StringUtils.toHexString(MessageDigest
					.getInstance("MD5").digest(imageData.getBytes("UTF-8")));
			end = System.currentTimeMillis();
			Log.i("ImageSerializer", "Time to checksum: " + (end - start)
					+ "ms");

			File imageFile = new File(imagesDirectory, checksum + ".jpg");
			if (!imageFile.exists()) {
				writeOutImageFile(imageFile, imageData);
			}
			image.localPath = imageFile.getPath();
			Log.i("Serializer",image.localPath);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("File not found", e);
		}

		return image;
	}

	private void writeOutImageFile(File imageFile, String imageData)
			throws IOException {
		OutputStream output = new BufferedOutputStream(new FileOutputStream(imageFile));
		byte[] decodedImage = Base64.decode(imageData, Base64.DEFAULT);
		output.write(decodedImage);
		output.close();
	}

	@Override
	public JsonElement serialize(Image image, Type type,
			JsonSerializationContext serializationContext) {
		// TODO Auto-generated method stub
		return null;
	}

}
