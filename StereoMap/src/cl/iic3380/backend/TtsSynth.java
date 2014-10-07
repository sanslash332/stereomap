package cl.iic3380.backend;

import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
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


	public String speakText(String toSpeak){
		  HashMap<String, String> myHashRender = new HashMap<String, String>();
	      myHashRender.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, toSpeak);
	      String destFileName;
	      if(toSpeak.length()>20)
	    	  destFileName = "/storage/extSdCard/StereoMap/"+toSpeak.substring(0, 20)+".wav";
	      else 
	    	  destFileName = "/storage/extSdCard/StereoMap/"+toSpeak+".wav";
	      //ttobj.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);
	      ttobj.synthesizeToFile(toSpeak, myHashRender, destFileName);
	      return destFileName;
	   }
	
	

}
