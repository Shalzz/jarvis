package com.shalzz.bluetoothController;

public enum ServerStatus {

	UNAVAILABLE(0), AVAILABLE(1), BUSY(2), FAULTY(3), SUCCEEDED(4), FAILED(5);

	private int code;

	private ServerStatus(int c) {
		setCode(c);
	}

	@Override
	public String toString() { // only capitalise the first letter
		String s = super.toString();
		return s.substring(0, 1) + s.substring(1).toLowerCase();
	}

	public int getCode() {
		return code;
	}

	private void setCode(int code) {
		this.code = code;
	}
	
	public byte[] getBytes(){
		return   new byte[] { 
	            (byte)(code >> 24), 
	            (byte)(code >> 16), 
	            (byte)(code >> 8), 
	            (byte)code}; 
	}
}
