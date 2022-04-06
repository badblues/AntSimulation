package com.antsim.ai;

import com.antsim.ant.AntWarrior;

public class AntWarriorAI extends BaseAI{

	private AntWarrior antWarrior;

	public AntWarriorAI(AntWarrior antWarrior) {
		this.antWarrior = antWarrior;
	}

	@Override
	public void run() {
		//super.run();
		while(!exit) {
			antWarrior.setPosX(antWarrior.getPosX() + 0);
			antWarrior.setPosY(antWarrior.getPosY() + 10);
			antWarrior.moveImage(antWarrior.getPosX(), antWarrior.getPosY());
			System.out.println(antWarrior.getPosX() + " " + antWarrior.getPosY());
			try {
				sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
