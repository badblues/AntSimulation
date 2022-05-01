package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.Worker;

import java.util.Vector;


public class WorkerAI extends BaseAI{

	int homeX = 400;
	int homeY = 360;

	public WorkerAI(Vector<Ant> antsVector) {
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
					if (ant instanceof Worker) {
						move((Worker) ant);
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

	private void move(Worker worker) {
		int x = worker.getPosX();
		int y = worker.getPosY();
		if (x == homeX && y == homeY || x == worker.getSpawnX() && y == worker.getSpawnY())
			worker.changeDestination();
		if (worker.getDestination() == Destination.HOME) {
			double dx = (double)(homeX - x) / 10;
			if (Math.abs(dx) < 1)
				dx = 1 * Math.signum(dx);
			double dy = (double)(homeY - y) / 10;
			if (Math.abs(dy) < 1)
				dy = 1 * Math.signum(dy);
			x += dx;
			y += dy;
		} else {
			double dx = (double)(worker.getSpawnX() - x) / 10;
			if (Math.abs(dx) < 1)
				dx = 1 * Math.signum(dx);
			double dy = (double)(worker.getSpawnY() - y) / 10;
			if (Math.abs(dy) < 1)
				dy = 1 * Math.signum(dy);
			x += dx;
			y += dy;
		}
		worker.setPosX(x);
		worker.setPosY(y);
		worker.moveImage();
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
