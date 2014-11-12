package cl.iic3380.utils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;

import android.location.Criteria;
import android.media.MediaPlayer;

public class Utils {
	
	public static Criteria GetFineCriteria() {

		Criteria c = new Criteria();
		c.setAccuracy(Criteria.ACCURACY_FINE);
		c.setAltitudeRequired(false);
		c.setBearingRequired(false);
		c.setSpeedRequired(false);
		c.setCostAllowed(true);
		c.setPowerRequirement(Criteria.POWER_HIGH);
		return c;

	}
	
	
	@SuppressWarnings("resource")
	public static int GetDuration(File file)
	{
		int length = -1;
		try
		{
			MediaPlayer mp = new MediaPlayer();
			FileInputStream fs = new FileInputStream(file);
			FileDescriptor fd = fs.getFD();
			mp.setDataSource(fd);
			mp.prepare();
			length = mp.getDuration();
			mp.release();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return length;

	}

}
