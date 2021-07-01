package github.kairusds;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.HttpURLConnection;

// more soon
public class Utils{

	public static boolean pingUrl(String url){
		try{
			URL conn = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) conn.openConnection();
			connection.setRequestMethod("HEAD");
			return connection.getResponseCode() == 200;
		}catch(Exception error){
			error.printStackTrace();
			return false;
		}
	}

	public static BufferedImage getImageFromUrl(String url){
		if(!pingUrl(url)) return null;
		try{
			return ImageIO.read(new URL(url));
		}catch(Exception error){
			error.printStackTrace();
			return null;
		}
	}

	private Utils(){}

}