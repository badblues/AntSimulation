package com.antsim.ai;

import com.antsim.ant.AntWorker;

public class AntWorkerAI extends BaseAI{

	private AntWorker antWorker;
	int homeX = 400;
	int homeY = 360;


	public AntWorkerAI(AntWorker antWorker) {
		this.antWorker = antWorker;
	}

	@Override
	public void run() {
		//super.run();
		while(!exit) {
			//TODO change position
			antWorker.moveImage(antWorker.getPosX(), antWorker.getPosY());
			System.out.println(antWorker.getPosX() + " " + antWorker.getPosY());
			try {
				sleep(1000);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}


}
