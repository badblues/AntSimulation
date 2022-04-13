package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWarrior;

import java.util.Vector;

public class AntWarriorAI extends BaseAI{

	private volatile boolean running = true;
	private volatile boolean paused = false;
	private final Object pauseLock = new Object();


	public AntWarriorAI(Vector<Ant> antsVector) {
		super(antsVector);
		setName("Warriors movement name");
	}

	@Override
	public void run() {
		while (running) {
			synchronized (pauseLock) {
				if (!running) {
					break;
				}
				if (paused) {
					try {
						pauseLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
						break;
					}
					if (!running) {
						break;
					}
				}
			}
			synchronized(antsVector) {
				for (Ant ant : antsVector) {
					if (ant instanceof  AntWarrior) {
						move((AntWarrior) ant);
					}
				}
			}
			try {
				sleep(35);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void move(AntWarrior antWarrior) {
		antWarrior.setMovementAngle(antWarrior.getMovementAngle() + Math.PI/50 * antWarrior.getMovementDirection());
		antWarrior.setPosX(antWarrior.getSpawnX() + (int)(AntWarrior.movementRadius * Math.cos(antWarrior.getMovementAngle())));
		antWarrior.setPosY(antWarrior.getSpawnY() + (int)(AntWarrior.movementRadius * Math.sin(antWarrior.getMovementAngle())));
		antWarrior.moveImage(antWarrior.getPosX(), antWarrior.getPosY());
	}


	public void mystop() {
		running = false;
		myresume();
	}

	public void pause() {
		paused = true;
	}

	public void myresume() {
		synchronized (pauseLock) {
			paused = false;
			pauseLock.notifyAll();
		}
	}

}
