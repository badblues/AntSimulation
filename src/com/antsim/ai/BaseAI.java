package com.antsim.ai;

import com.antsim.ant.Ant;

import java.util.Vector;

public abstract class BaseAI extends Thread {

	protected final Vector<Ant> antsVector;

	public BaseAI(Vector <Ant> antsVector) {
		this.antsVector = antsVector;
	}

	@Override
	public void run() {}

}
