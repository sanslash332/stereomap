package cl.iic3380.backend;

import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;

public class ConnectAsyncTask extends AsyncTask<BluetoothDevice, Integer, BluetoothSocket>{

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

//		if(blue != null)
//		{
//			if (blue.btSocket != null)
//			{
//				blue.run();
//			}
//		}


		//Enable Button


	}
}