package cl.iic3380.backend;

import android.bluetooth.*;
import android.content.Intent;

import java.io.IOException;
import java.lang.Object;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

public class Bluetooth {
	
	private BluetoothAdapter adapter;
	private List<String> ArrayAdapter= new ArrayList<String>();
	private ArrayList<BluetoothDevice> btDevices = new ArrayList<BluetoothDevice>();
	private BluetoothSocket btSocket;
private BluetoothServerSocket serverSocket;
private ConnectAsyncTask cntTask;
	

	public Bluetooth(){	
		adapter = BluetoothAdapter.getDefaultAdapter();
		is_paired();
		cntTask = new ConnectAsyncTask();
		
		 
	}
	public BluetoothAdapter get_adapter(){
		return adapter;
	}
	public List <String> get_paired(){
		is_paired();
		return ArrayAdapter;
	}
	
	public void is_paired(){		
		Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        ArrayAdapter.add( device.getName() + "\n" + device.getAddress() );
		        btDevices.add(device);
		        
		    }
		}
			
	}
	
	public void connectToJockey()
	{
		//Metodo que efectua la coneccion
		//Buscar el Jockey por id, mac o UID (lo que se tenga) entre la lista de devices paireados, y instansear los sockets. ademas instansear listener para cuando se reciban datos.
		adapter.cancelDiscovery();
		
		cntTask.execute(dev);
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
		            ArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		        }
		    }
		};
		
		
	}
	
	public void pair(){
		 BluetoothDevice device = adapter.getRemoteDevice("00:18:a1:12:0d:32");
		 
	     BluetoothSocket tmp = null;
	     BluetoothSocket mmSocket = null;

	        // Get a BluetoothSocket for a connection with the
	        // given BluetoothDevice
	     //Obtener UUID del telefono
	     
	     Method getUuidsMethod = BluetoothAdapter.class.getDeclaredMethod("getUuids", null);
	     ParcelUuid[] uuids = (ParcelUuid[]) getUuidsMethod.invoke(adapter, null);
	     UUID my_uuid = uuids[0].getUuid();
	     for (ParcelUuid uuid: uuids) {
	    	    Log.d("TAG", "UUID: " + uuid.getUuid().toString());
	    	}
	     
	        try {
	            tmp = device.createRfcommSocketToServiceRecord(my_uuid);
	            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	            tmp = (BluetoothSocket) m.invoke(device, 1);
	        } catch (IOException e) {
	            
	        }
	        mmSocket = tmp;
	}
	

}



private class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket>{

	private BluetoothSocket mmSocket;
	private BluetoothDevice mmDevice;
	
	@Override
	protected BluetoothSocket doInBackground(BluetoothDevice... device) {
						
		mmDevice = device[0];
		
		try {
			
			String mmUUID = "00001101-0000-1000-8000-00805F9B34FB";
			mmSocket = mmDevice.createInsecureRfcommSocketToServiceRecord(UUID.fromString(mmUUID));
			mmSocket.connect();
			
		} catch (Exception e) { }
		
		return mmSocket;
	}

	@Override
	protected void onPostExecute(BluetoothSocket result) {
		
		btSocket = result;
		//Enable Button
		
		
	}
	
	
		
	
}
}
