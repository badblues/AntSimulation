package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWorker;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntWorkerAI extends BaseAI{

	int homeX = 400;
	int homeY = 360;

	public AntWorkerAI(Vector<Ant> antsVector) {
		super(antsVector);
	}


	@Override
	public void run() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			synchronized(antsVector) {
				for (Ant ant : antsVector) {
					if (ant instanceof AntWorker) {
						move((AntWorker) ant);
					}
				}
			}
		}, 0, 35, TimeUnit.MILLISECONDS);
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
}
