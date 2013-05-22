package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.io.IOException;
import java.security.Timestamp;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.BlockHasNoPreviousException;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;

public class BlockTreeClientSimulatorThread extends SimpleClientSimulatorThread {

	// for logging
	private final static Logger LOGGER = Logger.getLogger( BlockTreeClientSimulatorThread.class.getName() );
	
	static {
	     // set up logger
		LOGGER.setLevel( Level.INFO );
		
	  }
	
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
			
			this.blockTree.addBlock(sb);
			
			// generate a diagram
			
			String diagramFilename = "graphs/graph"+logPrefix+"-"+this.thisCount+"-"+ blockCounter++ +".svg";
			this.blockTree.printDOT( diagramFilename );
			LOGGER.info( identity.color + " : block recieved! " + "<a href=\"" + diagramFilename + "\">graph</a>");
			
		} catch (BlockHasNoPreviousException e) {
			
			LOGGER.warning( "bad block: no previous hash.");
			
		} catch (IOException e) {
			
			LOGGER.warning( "cant write file.");
			
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
					
					LOGGER.info( this.identity.color + " : new block " + newBlock.id);
					
					// and broadcast it
					
					broadcaster.broadcastBlock(newBlock);
					
				} catch (InterruptedException e) {
					
					LOGGER.warning("INTERRUPT!");
					
				}
			
		}
	}

	/**
	 * @param args
	 */
	
	static String logPrefix;
	
	public static void main(String[] args) {
		
		// set up logs
		
		logPrefix = String.valueOf(new java.util.Date().getTime() );
		
		setupLogs( logPrefix );
		
		// now create a bunch of clientSimulator threads
		
		WeightedIdentity i1 = new WeightedIdentity("i1",10.00,"blue");
		WeightedIdentity i2 = new WeightedIdentity("i2",10.00,"red");
		WeightedIdentity i3 = new WeightedIdentity("i3",10.00,"green");
		
		// i1 is the ISSUER so we will sign the genesis block with it
		
		SignedBlock genesis = new SignedBlock(null, i1);
		
		BlockTreeClientSimulatorThread thread1 = new BlockTreeClientSimulatorThread( genesis, i1);
		BlockTreeClientSimulatorThread thread2 = new BlockTreeClientSimulatorThread( genesis, i2);
		BlockTreeClientSimulatorThread thread3 = new BlockTreeClientSimulatorThread( genesis ,i3);

		LOGGER.info("logtest");
		
		LOGGER.info("starting thread 1...");
		thread1.start();
		
		LOGGER.info("starting thread 2...");
		thread2.start();
		
		LOGGER.info("starting thread 3...");
		thread3.start();
		
	}

	private static void setupLogs( String filePrefix ) {
		try {
			
			FileHandler fileTxt = new FileHandler( filePrefix + "-log.html");
			FileHandler latestLog = new FileHandler( "latest-log.html");
			
			
			// Create txt Formatter
		    HtmlLogFormatter formatterHTML = new HtmlLogFormatter();
		    fileTxt.setFormatter(formatterHTML);
		    latestLog.setFormatter(formatterHTML);
		    
		    LOGGER.addHandler(fileTxt);
		    LOGGER.addHandler(latestLog);

			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
