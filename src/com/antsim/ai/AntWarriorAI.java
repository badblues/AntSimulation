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
		System.out.println("huy govno");
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		scheduler.schedule(() -> {
			System.out.println("govno");
			synchronized(antsVector) {
				for (Ant antWarrior : antsVector) {
					if (antWarrior instanceof  AntWarrior) {
						antWarrior.setPosX(antWarrior.getPosX() + 5);
						antWarrior.setPosY(antWarrior.getPosY() + 10);
						System.out.println(antWarrior.getPosX() + " " + antWarrior.getPosY());
						antWarrior.moveImage(antWarrior.getPosX(), antWarrior.getPosY());
					}
				}
			}
		}, 1000, TimeUnit.MILLISECONDS);
	}
}
