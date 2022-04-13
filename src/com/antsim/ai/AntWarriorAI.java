package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWarrior;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntWarriorAI extends BaseAI{

	private volatile boolean running = true;
	private volatile boolean paused = false;
	private final Object pauseLock = new Object();


	public AntWarriorAI(Vector<Ant> antsVector) {
		super(antsVector);
		setName("Warriors movement name");
	}

	@Override
	public void run() {
		while (running) {
			synchronized (pauseLock) {
				if (!running) { // may have changed while waiting to
					// synchronize on pauseLock
					break;
				}
				if (paused) {
					try {
						synchronized (pauseLock) {
							pauseLock.wait(); // will cause this Thread to block until
							// another thread calls pauseLock.notifyAll()
							// Note that calling wait() will
							// relinquish the synchronized lock that this
							// thread holds on pauseLock so another thread
							// can acquire the lock to call notifyAll()
							// (link with explanation below this code)
						}
					} catch (InterruptedException ex) {
						break;
					}
					if (!running) { // running might have changed since we paused
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
				sleep(35);
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void move(AntWarrior antWarrior) {
		antWarrior.setMovementAngle(antWarrior.getMovementAngle() + Math.PI/50 * antWarrior.getMovementDirection());
		antWarrior.setPosX(antWarrior.getSpawnX() + (int)(AntWarrior.movementRadius * Math.cos(antWarrior.getMovementAngle())));
		antWarrior.setPosY(antWarrior.getSpawnY() + (int)(AntWarrior.movementRadius * Math.sin(antWarrior.getMovementAngle())));
		antWarrior.moveImage(antWarrior.getPosX(), antWarrior.getPosY());
	}


	public void mystop() {
		running = false;
		// you might also want to interrupt() the Thread that is
		// running this Runnable, too, or perhaps call:
		myresume();
		// to unblock
	}

	public void pause() {
		// you may want to throw an IllegalStateException if !running
		paused = true;
	}

	public void myresume() {
		synchronized (pauseLock) {
			paused = false;
			pauseLock.notifyAll(); // Unblocks thread
		}
	}

}
