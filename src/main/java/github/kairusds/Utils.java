package github.kairusds;

import cn.nukkit.level.Location;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.Base64;

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

	public static BufferedImage getImageFromBase64(String data){
		byte[] imageBytes = Base64.getDecoder().decode(data);
		InputStream inputStream = new ByteArrayInputStream(imageBytes);
		return ImageIO.read(inputStream);
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

	public static String getCardinalDirection(Location location){
		double rotation = (location.getYaw() - 180) % 360;
		if(rotation < 0) rotation += 360.0;

		if(0 <= rotation && rotation < 22.5){
			return "N";
		}else if(22.5 <= rotation && rotation < 67.5){
			return "NE";
		}else if(67.5 <= rotation && rotation < 112.5){
			return "E";
		}else if(112.5 <= rotation && rotation < 157.5){
			return "SE";
		}else if(157.5 <= rotation && rotation < 202.5){
			return "S";
		}else if(202.5 <= rotation && rotation < 247.5){
			return "SW";
		}else if(247.5 <= rotation && rotation < 292.5){
			return "W";
		}else if(292.5 <= rotation && rotation < 337.5){
			return "NW";
		}else if(337.5 <= rotation && rotation < 360.0){
			return "N";
		}else{
			return null;
		}
	}

	private Utils(){}

}