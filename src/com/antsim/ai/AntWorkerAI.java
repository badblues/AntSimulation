package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWorker;

import java.util.Vector;


public class AntWorkerAI extends BaseAI{

	int homeX = 400;
	int homeY = 360;

	public AntWorkerAI(Vector<Ant> antsVector) {
		super(antsVector);
	}


	@Override
	public void run() {
		while (true) {
			synchronized (pauseLock) {
				if (paused) {
					try {
						pauseLock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
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
				sleep(50);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	//TODO refactor repeating code

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
		antWorker.setPosX(x);
		antWorker.setPosY(y);
		antWorker.moveImage();
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

}
