package cl.iic3380.utils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import android.location.Criteria;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

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
	
	public static String[] SpeakText(TextToSpeech tts,String toSpeak)
	{
		toSpeak = "hola";
		HashMap<String, String> myHashRender = new HashMap<String,String>();
		myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, toSpeak);
		String destFileName;
		String fileName;
		
		File file = Environment.getExternalStorageDirectory();
		
//		File file2 = new File(file.getAbsolutePath()+"/hola.wav");
//		try {
//			file2.createNewFile();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		String route = file.getAbsolutePath()+"/";
		
		if(toSpeak.length()>20)
		{
			destFileName = route+toSpeak.substring(0, 20)+".wav";
			fileName = toSpeak.substring(0,20);
		}
		else 
		{
			destFileName = route+toSpeak+".wav";
			fileName = toSpeak;
		}
		
//		while(tts.isSpeaking()){
//			
//		}
		
		//tts.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
		tts.synthesizeToFile(toSpeak, myHashRender, fileName+".wav");
		
		
		String[] resultArray = {fileName,route};
		return resultArray;
		
		
	}

}
