package com.antsim.ai;

import com.antsim.ant.Ant;
import com.antsim.ant.AntWarrior;
import com.antsim.ant.AntWorker;

import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AntWorkerAI extends BaseAI{

	private volatile boolean running = true;
	private volatile boolean paused = false;
	private final Object pauseLock = new Object();

	int homeX = 400;
	int homeY = 360;

	public AntWorkerAI(Vector<Ant> antsVector) {
		super(antsVector);
		setName("Workers movement thread");
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
					if (ant instanceof AntWorker) {
						move((AntWorker) ant);
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
