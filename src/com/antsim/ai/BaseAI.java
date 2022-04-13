package com.antsim.ai;

import com.antsim.ant.Ant;

import java.util.Vector;

public abstract class BaseAI extends Thread {

	protected volatile boolean running = true;
	protected volatile boolean paused = false;
	protected final Object pauseLock = new Object();

	protected final Vector<Ant> antsVector;

	public BaseAI(Vector <Ant> antsVector) {
		this.antsVector = antsVector;
	}

	@Override
	public void run() {}

	public void pause() {
		paused = true;
	}

	public void myresume() {
		synchronized (pauseLock) {
			paused = false;
			pauseLock.notifyAll();
		}
	}

	public boolean isPaused() {
		return paused;
	}

}
