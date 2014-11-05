package cl.iic3380.backend;

import android.bluetooth.*;
import android.content.Intent;
import java.lang.Object;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.sax.StartElementListener;
import android.view.ContextThemeWrapper;
import android.widget.Adapter;

public class Bluetooth {
	
	private BluetoothAdapter adapter;
	private List<String> ArrayAdapter= new ArrayList<String>();
	
	
	public Bluetooth(){	
		adapter = BluetoothAdapter.getDefaultAdapter();	
		
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
	        
	 
	}
	

}
