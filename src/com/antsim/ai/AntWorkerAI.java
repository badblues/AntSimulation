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
				for (Ant antWorker : antsVector) {
					if (antWorker instanceof AntWorker) {
						//			int x = antWorker.getPosX();
						//			int y = antWorker.getPosY();
						//			if (antWorker.getDestination() == Destination.HOME) {
						//				x += (homeX - x) / 5;
						//				y += (homeY - y) / 5;
						//			} else {
						//				x += (antWorker.getSpawnX() - x) / 5;
						//				y += (antWorker.getPosY() - y) / 5;
						//			}
						int x = antWorker.getPosX();
						int y = antWorker.getPosY() + 10;
						antWorker.moveImage(x, y);
						antWorker.setPosX(x);
						antWorker.setPosY(y);
					}
				}
			}
		}, 0, 1000, TimeUnit.MILLISECONDS);
	}
}
