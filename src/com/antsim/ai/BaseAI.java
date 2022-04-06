package com.antsim.ai;

public abstract class BaseAI extends Thread {

	protected boolean exit = false;

	@Override
	public void run() {}

	public void kill() {
		exit = true;
	}
}
