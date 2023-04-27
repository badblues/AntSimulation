package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.Warrior;

import java.util.Vector;

public class WarriorAI extends BaseAI{

	public WarriorAI(Vector<Ant> antsVector) {
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
					if (ant instanceof Warrior) {
						move((Warrior) ant);
					}
				}
			}
			try {
				sleep(40);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void move(Warrior warrior) {
		warrior.setMovementAngle(warrior.getMovementAngle() + Math.PI/50 * warrior.getMovementDirection());
		warrior.setPosX(warrior.getSpawnX() + (int)(Warrior.MOVEMENT_RADIUS * Math.cos(warrior.getMovementAngle())));
		warrior.setPosY(warrior.getSpawnY() + (int)(Warrior.MOVEMENT_RADIUS * Math.sin(warrior.getMovementAngle())));
		warrior.moveImage();
	}

}
