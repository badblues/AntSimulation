package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWarrior;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntWarriorAI extends BaseAI{

	public AntWarriorAI(Vector<Ant> antsVector) {
		super(antsVector);
	}

	@Override
	public void run() {
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.scheduleAtFixedRate(() -> {
			try {
				synchronized(antsVector) {
					for (Ant ant : antsVector) {
						if (ant instanceof  AntWarrior) {
							move((AntWarrior) ant);
						}
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}, 0, 35, TimeUnit.MILLISECONDS);
	}

	private void move(AntWarrior antWarrior) {
		antWarrior.setMovementAngle(antWarrior.getMovementAngle() + Math.PI/50 * antWarrior.getMovementDirection());
		antWarrior.setPosX(antWarrior.getSpawnX() + (int)(AntWarrior.movementRadius * Math.cos(antWarrior.getMovementAngle())));
		antWarrior.setPosY(antWarrior.getSpawnY() + (int)(AntWarrior.movementRadius * Math.sin(antWarrior.getMovementAngle())));
		antWarrior.moveImage(antWarrior.getPosX(), antWarrior.getPosY());
	}
}
