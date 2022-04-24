package com.antsim.ai;

import com.antsim.ant.Ant;

import java.util.Vector;

public abstract class BaseAI extends Thread {

	protected volatile boolean paused = false;
	protected final Object pauseLock = new Object();

	protected final Vector<Ant> antsVector;

	public BaseAI(Vector <Ant> antsVector) {
		this.antsVector = antsVector;
	}

	public void pause() {
		paused = true;
	}

	public void unpause() {
		synchronized (pauseLock) {
			paused = false;
			pauseLock.notifyAll();
		}
	}

	public boolean isPaused() {
		return paused;
	}

}
