package com.shalzz.bluetoothController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.StreamConnection;

public class ProcessConnectionThread implements Runnable {

	private StreamConnection mConnection;
	private OutputStream outputStream = null;

	// Constants that indicate commands from client devices

	public static final int RESPONSE_CODE_OK = 99;
	public static final int RESPONSE_CODE_BAD = 66;
	public static final int NO_RESPONSE_CODE = -1;
	public static final int ERROR_RESPONSE_CODE = -33;
	public final static int OPEN_COMMAND_CODE = 0xff;
	public final static int CLOSE_COMMAND_CODE = 0xee;
	private Security security = null;
	BluetoothServer bluetoothServer;

	public ProcessConnectionThread(StreamConnection connection,
			BluetoothServer bluetoothServer)

	{

		this.bluetoothServer = bluetoothServer;
		mConnection = connection;

	}

	@Override
	public void run() {

		System.out.println("Connected: ProcessConnectionThread running.");

		try {
			// send public key
			outputStream = mConnection.openOutputStream();
			System.out.println("Connected: outputstream opened.");
			security = bluetoothServer.getSecurity();

			outputStream.write(security.getChallenge());
			System.out.println("Connected: challenge issued");
			// prepare to receive data

			InputStream inputStream = mConnection.openInputStream();

			System.out.println("Connected: waiting for input");

			// Listen for 100 sec
			long startTime = new Date().getTime();
			while (inputStream.available() == 0 && (new Date().getTime() - startTime) < 100000) {
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			if (inputStream.available() > 0) {

				byte[] tmp = new byte[1];
				String hexString = "";
				int count = 0;
				while (true) {
					count++;
					int r = inputStream.read(tmp);
					hexString += Integer.toString((tmp[0] & 0xff) + 0x100, 16).substring(1);
					if (r == -1 || count > 256) { // Can't get end of stream on pi
						break;
					}
					baos.write(tmp, 0, r);
				}

				System.out.println("Received: " + hexString);
				byte[] challengeResponse = baos.toByteArray();

				ChallengeResponse authenticatedChallengeResponse = security
						.authenticateChallengeResponse(challengeResponse);
				processCommand(authenticatedChallengeResponse);

			} else {
				System.out.println("Challenge issued timeout");
			}
			mConnection.close();
			mConnection = null;

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			if (mConnection != null) {
				try {
					mConnection.close();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			security.nextKey(); // change key
		}

	}

	/**
	 * 
	 * Process the authenticated command from client
	 * 
	 * @param authenticatedChallengeResponse
	 *            the command code and status
	 */

	private void processCommand(ChallengeResponse authenticatedChallengeResponse) {		

		try {

			if (authenticatedChallengeResponse.getStatus() == ServerStatus.SUCCEEDED) {

				System.out.println("Command received: "
						+ authenticatedChallengeResponse.getCommand());
				ServerStatus serverStatus = ServerStatus.UNAVAILABLE;
				switch (authenticatedChallengeResponse.getCommand()) {

				// TODO do something with the data.
				
				}

			} else { // authentication failed

				outputStream.write(ServerStatus.FAILED.getCode());

				System.out.println("Response written: "
						+ ServerStatus.FAILED.getCode());
				System.out.println(" *****************************");
				System.out.println(" ******* COMMAND FAILED ******");
				System.out.println(" *****************************");

			}
			outputStream.flush();
			outputStream.close();
			outputStream = null;

		} catch (Exception e) {
			System.out.println("Outputstream caused exception: "
					+ e.getMessage());
			e.printStackTrace();

		} finally {
			if (outputStream != null) {
				try {

					outputStream.close();
				} catch (IOException e) {
					System.out.println("Can't close outputstream after exception: " + e.getMessage());
					e.printStackTrace();
				}
			}

		}

	}
}