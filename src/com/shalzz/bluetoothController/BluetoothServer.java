package com.shalzz.bluetoothController;

import java.util.Timer;
import java.util.TimerTask;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer extends Thread {

	UUID uuid = new UUID("0000110100001000800000805F9B34FB", false); // This is a magic number
	String url = "btspp://localhost:" + uuid.toString() + ";name=xxxx";
	Security security = new Security();

	public BluetoothServer() {
	}

	@Override
	public void run() {
		waitForConnection();
	}

	/** Waiting for connection from devices */
	private void waitForConnection() {

		// retrieve the local Bluetooth device object
		LocalDevice local = null;
		StreamConnectionNotifier notifier = null;
		StreamConnection connection = null;

		// setup the server to listen for connection

		try {
			local = LocalDevice.getLocalDevice();			
			local.setDiscoverable(DiscoveryAgent.GIAC); // generally discoverable, discoveryTimeout should be disabled - but isn't.

			notifier = (StreamConnectionNotifier) Connector.open(url);
            // Bluetooth may go undiscoverable after 180sec default discoveryTimeout, so reset it on separate thread every 179sec.
			new Timer().schedule(new TimerTask(){

				@Override
				public void run() {
					try {
						LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
					} catch (BluetoothStateException e) {
						System.out.println("TimerTask exception: " + e.getMessage());
						e.printStackTrace();
					}			
				}				
			}, 0, 179000);
		} 
		catch (Exception e) 
		{
			System.out.println("Server exception: " + e.getMessage());
			e.printStackTrace();
		}

		// waiting for connection

		while (true) {
			try {
				System.out.println("waiting for connection...");
				connection = notifier.acceptAndOpen();

				Thread processThread = new Thread(new ProcessConnectionThread( connection, this));
				processThread.start();
				System.out.println("processThread.started...");
			    // bluetooth may have been set undiscoverable after connection
				local.setDiscoverable(DiscoveryAgent.GIAC);
			} 
			catch (Exception e) 
			{
				System.out.println("Server exception: " + e.getMessage());
				e.printStackTrace();
				 return;
			}
		}
	}

	public Security getSecurity() {
		return security;
	}
}