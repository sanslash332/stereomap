package cl.iic3380.backend;

import android.bluetooth.*;
import android.content.Intent;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Object;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.sax.StartElementListener;
import android.support.v4.net.ConnectivityManagerCompat;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Adapter;
import android.os.AsyncTask;
import android.os.Bundle;

public class Bluetooth extends Thread {

	private BluetoothAdapter adapter;

	private ArrayList<BluetoothDevice> btDevices = new ArrayList<BluetoothDevice>();
	private BluetoothSocket btSocket;
	private BluetoothServerSocket serverSocket;
	private ConnectAsyncTask cntTask;
	private jockeyData jockey;	

	public Bluetooth(){	
		adapter = BluetoothAdapter.getDefaultAdapter();
		is_paired();
		cntTask = new ConnectAsyncTask();


	}
	public BluetoothAdapter get_adapter(){
		return adapter;
	}

	public jockeyData getJockey()
	{
		return(this.jockey);
	}

	@SuppressLint("NewApi") 
	public boolean isConected()
	{
		if(btSocket!= null)
		{
			return(btSocket.isConnected());
		}
		else {
			return(false);
		}

	}
	@Override
	public void run() {
		try {
			receiptData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}


	public ArrayList<BluetoothDevice> get_paired(){
		is_paired();
		return btDevices;
	}

	public void is_paired(){		
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a ListView

				btDevices.add(device);

			}
		}

	}

	public void connectToJockey() throws InterruptedException, ExecutionException
	{
		//Metodo que efectua la coneccion
		//Buscar el Jockey por id, mac o UID (lo que se tenga) entre la lista de devices paireados, y instansear los sockets. ademas instansear listener para cuando se reciban datos.
		adapter.cancelDiscovery();
		BluetoothDevice jockey = null;
		for(BluetoothDevice dev : btDevices)
		{
			if(dev.getName() == "stereoJockeyMap")
			{
				jockey=dev;

			}

		}
		if(jockey!=null )
		{
			btSocket = cntTask.execute(jockey).get();
			

		}


	}

	@SuppressLint("NewApi") 
	public void receiptData() throws IOException
	{
		InputStream mms=null;
		InputStreamReader str =null;
		BufferedReader reed =null;
		while(btSocket.isConnected())
		{
			if(mms == null)
			{
				mms = btSocket.getInputStream();
				str = new InputStreamReader(mms, Charset.defaultCharset());
				reed = new BufferedReader(str);

			}

			if(reed != null && reed.ready())
			{
				String jkContent = reed.readLine();
				jockeyData jk = new jockeyData(jkContent);

				if(!jk.isInvalid())
				{
					this.jockey=jk;
				}
			}

		}
	}

	public void discover(){
		adapter.startDiscovery();

		// Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver receiver = new BroadcastReceiver() {
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				// When discovery finds a device
				if (BluetoothDevice.ACTION_FOUND.equals(action)) {
					// Get the BluetoothDevice object from the Intent
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					// Add the name and address to an array adapter to show in a ListView

				}
			}
		};


	}

//	public void pair(){
//		BluetoothDevice device = adapter.getRemoteDevice("00:18:a1:12:0d:32");
//
//		BluetoothSocket tmp = null;
//		BluetoothSocket mmSocket = null;
//
//		// Get a BluetoothSocket for a connection with the
//		// given BluetoothDevice
//		//Obtener UUID del telefono
//
//		
//		Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
//		ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
//		UUID my_uuid = uuids[0].getUuid();
//		for (ParcelUuid uuid: uuids) {
//			Log.d("TAG", "UUID: " + uuid.getUuid().toString());
//		}
//
//		try {
//			tmp = device.createRfcommSocketToServiceRecord(my_uuid);
//			Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
//			tmp = (BluetoothSocket) m.invoke(device, 1);
//		} catch (IOException e) {
//
//		}
//		mmSocket = tmp;
//	}


}










