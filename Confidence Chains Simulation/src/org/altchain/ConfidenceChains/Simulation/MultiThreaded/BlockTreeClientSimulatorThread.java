package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.io.IOException;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.BlockHasNoPreviousException;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;

public class BlockTreeClientSimulatorThread extends SimpleClientSimulatorThread {

	
	BlockTree blockTree;
	
	static int counter=0;
	
	int thisCount;
	
	BlockTreeClientSimulatorThread(SignedBlock genesis, WeightedIdentity identity) {
		
		super(identity);
		
		blockTree = new BlockTree(genesis);
		
		thisCount = counter++;

	}
	
	int blockCounter = 0;
	
	protected void handleRecieveBlock(Block block) {
		
		System.out.println("block recieved!");
		
		// here goes the code to handle the block broadcast
		
		// get the block , add it to the tree
		
		SignedBlock sb = (SignedBlock)block;
		
		try {
			
			System.out.println("out!");
			this.blockTree.addBlock(sb);
			
			// generate a diagram
			
			this.blockTree.printDOT( "graphs/graph"+this.thisCount+"-"+ blockCounter++ +".svg");
			
		} catch (BlockHasNoPreviousException e) {
			
			System.out.println("bad block: no previous hash.");
			
		} catch (IOException e) {
			
			System.out.println("cant write file.");
			
		}
		
		
		//SignedBlock sb = (SignedBlock)block;
		//System.out.println( counter++ + " " + identity.name + " got block! "+sb.id);
	}
	
	protected void runMainLoop() {
		
		for( int i=0; i<10; i++ ){
			
				// here be the main client thread loop

				try {
					
					sleep( rand.nextInt(10) );
					
					// now generate a block
					
					SignedBlock newBlock = blockTree.createBestBlock(this.identity);
					
					System.out.println("new block " + newBlock.id);
					
					// and broadcast it
					
					broadcaster.broadcastBlock(newBlock);
					
				} catch (InterruptedException e) {
					
					System.out.println("INTERRUPT!");
					
				}
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// now create a bunch of clientSimulator threads
		
		WeightedIdentity i1 = new WeightedIdentity("i1",10.00,"blue");
		WeightedIdentity i2 = new WeightedIdentity("i2",10.00,"red");
		WeightedIdentity i3 = new WeightedIdentity("i3",10.00,"green");
		
		// i1 is the ISSUER so we will sign the genesis block with it
		
		SignedBlock genesis = new SignedBlock(null, i1);
		
		BlockTreeClientSimulatorThread thread1 = new BlockTreeClientSimulatorThread( genesis, i1);
		BlockTreeClientSimulatorThread thread2 = new BlockTreeClientSimulatorThread( genesis, i2);
		BlockTreeClientSimulatorThread thread3 = new BlockTreeClientSimulatorThread( genesis ,i3);

		System.out.println("starting thread 1...");
		thread1.start();
		
		System.out.println("starting thread 2...");
		thread2.start();
		
		System.out.println("starting thread 3...");
		thread3.start();
		
	}

}
