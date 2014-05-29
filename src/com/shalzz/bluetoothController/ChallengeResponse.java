package com.shalzz.bluetoothController;

public class ChallengeResponse {
	
	private ServerStatus serverStatus;
	
	private int command;
		

	public ChallengeResponse(ServerStatus status, int command) {
		setStatus(status);
		setCommand(command);
	}

	public ServerStatus getStatus() {
		return serverStatus;
	}

	private void setStatus(ServerStatus status) {
		this.serverStatus = status;
	}

	public int getCommand() {
		return command;
	}

	private void setCommand(int command) {
		this.command = command;
	}	
}