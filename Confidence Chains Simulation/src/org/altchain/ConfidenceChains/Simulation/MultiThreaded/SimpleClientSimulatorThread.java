package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Logger;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;

public class SimpleClientSimulatorThread extends Thread {
	
	public static class P2PBroadcast extends Observable {
		
		// a threadsafe deque of blocks
		LinkedBlockingDeque<SignedBlock> blocks = new LinkedBlockingDeque<SignedBlock>();
		
		public boolean broadcastBlock( SignedBlock block ){
			blocks.add(block);
			setChanged();
			this.notifyObservers(block);
			return true;
		}
		
	}
	
	public static int threadCounter=0;
	
	int thisThreadNum;
	
	protected static Logger LOGGER = null;
	
	public static P2PBroadcast broadcaster = new P2PBroadcast();
	
	int blockCounter = 0;
	
	public class PeerNode implements Observer {
		    
	    public void update(Observable obj, Object arg) {
	        if (arg instanceof SignedBlock) {
	            handleRecieveBlock((Block)arg);
	        }
	    }
	}
	
	// non static parts of the class
	
	PeerNode observer = new PeerNode();
	
	// the identity of the client
	WeightedIdentity identity;
	
	// the constructor supplies the identity
	SimpleClientSimulatorThread( WeightedIdentity identity ){
		this.identity = identity;
	}
	
	// random number generator
	Random rand = new Random( System.currentTimeMillis() );

	
////////////////////////////////////////////////////////
	
	// main runner thread
	
	public void run () {
		
		try {
			
			this.sleep(5000);
			
			System.out.println("starting "+identity.name+" thread...");
			
			broadcaster.addObserver(observer);

			runMainLoop();
			
		} catch (Exception e) {
			
			threadFailed(e);
			
		}
		
		
	      
	}

	protected void threadFailed(Exception e) {
	
		return;
	
	}

	protected void runMainLoop() {
		for( int i=0; i<10; i++ ){
			
				// here be the main client thread loop

				try {
					
					sleep( rand.nextInt(1000) );
					
					// now generate a block
					
					SignedBlock newBlock = new SignedBlock(null, identity);
					
					// and broadcast it
					
					broadcaster.broadcastBlock(newBlock);
					
				} catch (InterruptedException e) {
					
				}
			
		}
	}
	
	// here is the function called when the Simulator Thread receives a block broadcast
	
	protected void handleRecieveBlock(Block block) {
		// here goes the code to handle the block broadcast
		SignedBlock sb = (SignedBlock)block;
		System.out.println( blockCounter++ + " " + identity.name + " got block! "+sb.id);
	}
	
	

	public static void main(String[] args) {
		
		// now create a bunch of clientSimulator threads
		
		WeightedIdentity i1 = new WeightedIdentity("i1",10.00);
		WeightedIdentity i2 = new WeightedIdentity("i2",10.00);
		WeightedIdentity i3 = new WeightedIdentity("i3",10.00);
		
		SimpleClientSimulatorThread thread1 = new SimpleClientSimulatorThread(i1);
		SimpleClientSimulatorThread thread2 = new SimpleClientSimulatorThread(i2);
		SimpleClientSimulatorThread thread3 = new SimpleClientSimulatorThread(i3);

		System.out.println("starting thread 1...");
		thread1.start();
		
		System.out.println("starting thread 2...");
		thread2.start();
		
		System.out.println("starting thread 3...");
		thread3.start();
		
	}

}
