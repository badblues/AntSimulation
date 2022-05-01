package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWarrior;

import java.util.Vector;

public class AntWarriorAI extends BaseAI{

	public AntWarriorAI(Vector<Ant> antsVector) {
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
					if (ant instanceof  AntWarrior) {
						move((AntWarrior) ant);
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

	private void move(AntWarrior antWarrior) {
		antWarrior.setMovementAngle(antWarrior.getMovementAngle() + Math.PI/50 * antWarrior.getMovementDirection());
		antWarrior.setPosX(antWarrior.getSpawnX() + (int)(AntWarrior.MOVEMENT_RADIUS * Math.cos(antWarrior.getMovementAngle())));
		antWarrior.setPosY(antWarrior.getSpawnY() + (int)(AntWarrior.MOVEMENT_RADIUS * Math.sin(antWarrior.getMovementAngle())));
		antWarrior.moveImage();
	}

}
