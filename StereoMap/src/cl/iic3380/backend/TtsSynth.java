package cl.iic3380.backend;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.speech.tts.TextToSpeech;

public class TtsSynth {


	private TextToSpeech ttobj;
	final Locale spanish = new Locale("es", "ES");

	public TtsSynth(Context c){
		ttobj=new TextToSpeech(c, 
				new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if(status != TextToSpeech.ERROR){
					ttobj.setLanguage(spanish);
				}				
			}
		});			
	}	


	public String[] speakText(String toSpeak){
		toSpeak = "hola";
		HashMap<String, String> myHashRender = new HashMap();
		myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, toSpeak);
		String destFileName;
		String fileName;
		
		File file = Environment.getExternalStorageDirectory();
		
		File file2 = new File(file.getAbsolutePath()+"/hola.wav");
		try {
			file2.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
		
		while(ttobj.isSpeaking()){
			
		}
		
		ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
		ttobj.synthesizeToFile(toSpeak, myHashRender, file2.getPath());
		
		
		String[] resultArray = {fileName,"/storage/sdcard0/"};
		return resultArray;
		
		
	}



}
