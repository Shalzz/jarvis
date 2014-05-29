package com.shalzz.bluetoothController;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.xml.bind.DatatypeConverter;

public class Security {

	KeyPair keyPair;

	private String password = "xxxxxxxxxx";

	private ServerStatus status = ServerStatus.UNAVAILABLE;

	private static KeyPair staticKeyPair;

	// hardcoded keys used in development
	private final static String privateString = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDOOcz3TDFnebcxM4F1lOKHMZO0Jai7uK9ZVuXJjFTxerl4"
			+ "Ozwm2DBktZP7wtk/YnGonIjDh4DG+OwT9NyAnhL8kR1n5SBnQUG6Wh5CJsDBItVIbA67hcmUJ466ajkbB0pubeefd+3rIjGm"
			+ "F7OeiErXGGP0wY/OC+1wE8PxGYNZ05JyCD9l0wK8FnW3OLRY3JZjNjE6N4PcVaodTa2dx8mBa3WmvrQliuxWLmVNZIDcB/WM"
			+ "/x0ygNbFHWYJIrCR7BEaf1dCU0VE24o/3U6BcU/SWow5wr81rOa1Yp02M62BL8F8PdhvXb7fkIgBe2ZAbxZlcZ8CYd+j5P/8"
			+ "cGqKBojZAgMBAAECggEAGvRnG/OziWjZTu8CftHlAfKy8ZrygQbUtIKtKjjTaXS3BUYIyLL9MEyjeQawyp4daQJ8xcHSTS9/"
			+ "++PHZnBC3wxAobHLGHPpGWhqCA90pkndQsgM7OnBvUGmYCs+J5A+ZEytN6S/Tjp77s+uRIxf69leR0F9gFsCv+F1fO/uwTCw"
			+ "EZuU98o9+xyAvLKG0hhT5lUmrJjRygZWDQUDfXqN6oEVYUGtxWUqCYYt5eFa6agExbw+fCdHwH/svLT07o3+EkjQkJ43wzGV"
			+ "DqBrUnjxngJMoCmE+ezhrzoJRRPC6P+iUWVNod6lZHb8XAGURVsJKph/B/DFENFKMVgyNsA14QKBgQDxlVrzxbnM2Xxn+y2K"
			+ "Kr7yEizIxLpt4pP/7ZhYDo65gXAdKd1zJFG6ba9XUqPtDdzocV+wuzy55p+y/jf8s9sDVfWoCWRSf0r2k3xfPNl2hBAwqQJb"
			+ "FnhtX/eqmhvDn/1GNjX1a46dY+Prw57SwbVQXH3R0wAWLxtfR48DWUplnQKBgQDaiEpbXS2ouZWTSYtE0ct05fSEjxX4EJXZ"
			+ "qffncjuH/SSIDjY3xJZlK9oaeEtIWAhPs0qpXGBEkFHo7/gaJoqcExxHHjE5I9fvp6wg2fQSt6toOR9krSpxrgHLmwvmY+aI"
			+ "ZHc1tAGZh7yat7ymaiAWFGD0cROTfsI+5uW4RYXJbQKBgQDHK3qLxbT0BU5Cr9uQsMMx8fgIIeENnSKlei4CUyYqHGPhN1XC"
			+ "iuvyhmFr3c8WLjq1TFs26ncbUN7TBDIGXgWvj1vziKK0PQwBlACSyCkFQ8XRSFWP4Uux6M/YUCuZsbpu+1yrFFzEt8j95A55"
			+ "9YfVVBvLNGk64OVxPR7UBPaIJQKBgQCrDXc2YI72vJ0ptq0+ZwDcNYrZn65Qh5rjzzjufum+HhVUMduad1RJSLPleDdlHxMS"
			+ "X4KXYmkwAc87h2op/fUNCaBewnFBCp9hRVtkM3XqmL2gTOYXxbutU8iv+GKuKZAvPoRZEtcVt7uXVy5hDDylS7pAW6o5D0U4"
			+ "Oz+gpzp7WQKBgFVWzs8HtEOcjMOz1XgAtRSziUrpbJAI4SL+YJRTQOtdGVW9d8idaRSoC9+1vCmaajeCuT5p7TiWETf1GF/7"
			+ "wCrjyLBD0anDZSeGY+sFuldkSjc1inTM4W4ire0jhN0yA+FuDJ7sRt9OtlRXWeRs7TQUlj0D9JlpLWiN1XreaFc3";

	private final static String publicString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzjnM90wxZ3m3MTOBdZTihzGTtCWou7ivWVblyYxU8Xq5eDs8Jtgw"
			+ "ZLWT+8LZP2JxqJyIw4eAxvjsE/TcgJ4S/JEdZ+UgZ0FBuloeQibAwSLVSGwOu4XJlCeOumo5GwdKbm3nn3ft6yIxpheznohK"
			+ "1xhj9MGPzgvtcBPD8RmDWdOScgg/ZdMCvBZ1tzi0WNyWYzYxOjeD3FWqHU2tncfJgWt1pr60JYrsVi5lTWSA3Af1jP8dMoDW"
			+ "xR1mCSKwkewRGn9XQlNFRNuKP91OgXFP0lqMOcK/NazmtWKdNjOtgS/BfD3Yb12+35CIAXtmQG8WZXGfAmHfo+T//HBqigaI"
			+ "2QIDAQAB";

	static PrivateKey privateKey;
	static byte[] publicBytes;
	static KeyBundle[] keyBundles = new KeyBundle[5];
	static int keyBundleIndex = 0;

	static void init() {
		// used only in development with hardcoded keys

		PKCS8EncodedKeySpec ks = new PKCS8EncodedKeySpec(
				DatatypeConverter.parseBase64Binary(privateString));
		try {
			privateKey = KeyFactory.getInstance("RSA").generatePrivate(ks);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		publicBytes = DatatypeConverter.parseBase64Binary(publicString);
	}

	public Security() {
		// start populating keyBundles
		new Thread() {
			public void run() {
				for (int i = 0; i < 5; i++) {
					keyBundles[i] = new KeyBundle();
					if (i == 0) {
						privateKey = keyBundles[i].getPrivateKey();
						publicBytes = keyBundles[i].getPublicBytes();
					}
				}
			}
		}.start();

	}

	/**
	 * Challenge is 4 byte status then public key
	 * 
	 * 
	 * @return
	 */
	protected byte[] getChallenge() {

		while (publicBytes == null) {
			System.out.println("Key not ready.");
			try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		byte[] statusBytes = status.getBytes();
		byte[] challengeBytes = new byte[4 + publicBytes.length];
		System.arraycopy(statusBytes, 0, challengeBytes, 0, 4);
		System.arraycopy(publicBytes, 0, challengeBytes, 4, publicBytes.length);
		return challengeBytes;
	}

	/**
	 * Decrypt response Authenticate password Pass back command
	 * 
	 * @param challengeResponseBytes
	 * @return command
	 */
	protected ChallengeResponse authenticateChallengeResponse(
			byte[] challengeResponseBytes) {
		int command = -1;
		boolean ok = true;
		System.out.println("challenge Response length: "
				+ challengeResponseBytes.length);
		Cipher cipher = null;
		byte[] decryptedResponse = null;
		try {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} catch (NoSuchAlgorithmException e) {
			ok = false;
			e.printStackTrace();

		} catch (NoSuchPaddingException e) {
			ok = false;
			e.printStackTrace();
		}
		try {
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			ok = false;
			e.printStackTrace();
		}
		System.out.println("cipher.init");
		try {
			decryptedResponse = cipher.doFinal(challengeResponseBytes);

			System.out.println("response decrypted");
		} catch (IllegalBlockSizeException e) {
			ok = false;
			e.printStackTrace();
		} catch (BadPaddingException e) {
			ok = false;
			e.printStackTrace();
		}
		if (ok) {
			// authenticate password
			String passwordAttempt = new String(decryptedResponse).substring(4);
			System.out.println("password attempt: " + passwordAttempt);
			if (!password.equalsIgnoreCase(passwordAttempt)) {
				System.out.println("Bad password");
				return new ChallengeResponse(ServerStatus.FAILED, command);
			}
			// command is first 4 bytes
			command = ByteBuffer.wrap(decryptedResponse).getInt();

			return new ChallengeResponse(ServerStatus.SUCCEEDED, command);
		} else {
			return new ChallengeResponse(ServerStatus.FAILED, command);
		}
	}

	public synchronized void nextKey() {
		keyBundles[keyBundleIndex].setUsed(true);
		keyBundleIndex = ++keyBundleIndex % 5;
		while (keyBundles[keyBundleIndex] == null
				|| keyBundles[keyBundleIndex].isUsed()) {
			keyBundleIndex = ++keyBundleIndex % 5;
			try {
				Thread.currentThread().sleep(2000);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		privateKey = keyBundles[keyBundleIndex].getPrivateKey();
		publicBytes = keyBundles[keyBundleIndex].getPublicBytes();
	}

	private class KeyBundle {

		PrivateKey privateKey;
		byte[] publicBytes;
		boolean used = false;

		// used to generate new keys
		public KeyBundle() {
			generateKeys();
		}

		private void generateKeys() {
			try {
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
				System.out.println("KeypairGenerator instantiated.");
				SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
				System.out.println("Random generated.");
				keyGen.initialize(2048, random); // hung on pi using OpenJDK, ok with Java 8
				System.out.println("Keygen initialised.");
				staticKeyPair = keyGen.generateKeyPair();
				System.out.println("Keys generated.");
				/*
 				String privateKeyString = DatatypeConverter.printBase64Binary(staticKeyPair.getPrivate().getEncoded());
				System.out.println(privateKeyString);
				System.out.println();
				String publicKeyString = DatatypeConverter.printBase64Binary(staticKeyPair.getPublic().getEncoded());
				System.out.println(publicKeyString);
				 */
				privateKey = staticKeyPair.getPrivate();
				publicBytes = staticKeyPair.getPublic().getEncoded();
				used = false;
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		public PrivateKey getPrivateKey() {
			return privateKey;
		}

		public byte[] getPublicBytes() {
			return publicBytes;
		}

		public boolean isUsed() {
			return used;
		}

		public void setUsed(boolean used) {
			this.used = used;
			new Thread() {
				public void run() {
					generateKeys();
				}
			}.start();
		}
	}
}
