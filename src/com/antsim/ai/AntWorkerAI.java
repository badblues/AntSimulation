package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWorker;

import java.util.Vector;


public class AntWorkerAI extends BaseAI{

	private volatile boolean running = true;
	private volatile boolean paused = false;
	private final Object pauseLock = new Object();

	int homeX = 400;
	int homeY = 360;

	public AntWorkerAI(Vector<Ant> antsVector) {
		super(antsVector);
		setName("Workers movement thread");
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
					if (ant instanceof AntWorker) {
						move((AntWorker) ant);
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

	private void move(AntWorker antWorker) {
		int x = antWorker.getPosX();
		int y = antWorker.getPosY();
		if (x == homeX && y == homeY || x == antWorker.getSpawnX() && y == antWorker.getSpawnY())
			antWorker.changeDestination();
		if (antWorker.getDestination() == Destination.HOME) {
			double dx = (double)(homeX - x) / 10;
			if (Math.abs(dx) < 1)
				dx = 1 * Math.signum(dx);
			double dy = (double)(homeY - y) / 10;
			if (Math.abs(dy) < 1)
				dy = 1 * Math.signum(dy);
			x += dx;
			y += dy;
		} else {
			double dx = (double)(antWorker.getSpawnX() - x) / 10;
			if (Math.abs(dx) < 1)
				dx = 1 * Math.signum(dx);
			double dy = (double)(antWorker.getSpawnY() - y) / 10;
			if (Math.abs(dy) < 1)
				dy = 1 * Math.signum(dy);
			x += dx;
			y += dy;
		}
		antWorker.moveImage(x, y);
		antWorker.setPosX(x);
		antWorker.setPosY(y);
	}

	public void mystop() {
		running = false;
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
