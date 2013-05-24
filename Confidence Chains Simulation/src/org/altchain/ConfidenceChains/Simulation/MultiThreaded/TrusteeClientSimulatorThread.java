package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.security.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.BlockHasNoPreviousException;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;

public class TrusteeClientSimulatorThread extends SimpleClientSimulatorThread {

	
	public static final String simulationTitle = "Block Tree Simulator 0.1";
	
	BlockTree blockTree;
	
	public static int counter=0;
	
	int thisCount;
	
	private static Logger LOGGER = null;
	
	static String logPrefix;
	static {
		
		logPrefix = String.valueOf( new java.util.Date().getTime() );
		
		// set up logging.
		
		LOGGER = Logger.getLogger( TrusteeClientSimulatorThread.class.getName() );
		LOGGER.setLevel( Level.INFO );
					
		FileHandler fileTxt;
		try {
			
			fileTxt = new FileHandler( logPrefix +  "trustee-log.html" );
			FileHandler latestLog = new FileHandler( "latest-trustee-log.html" );
			
			String[] columnHeaders = new String[] {"blue", "red", "green", "orange", "violet" };
			
			// Create txt Formatter
			HtmlLogFormatter formatterHTML = new HtmlLogFormatter("Confidence Chains Simulator 0.1", 
					"This is a simple simulation of how nodes in a peer to peer network will construct confidence chains.  The power structure is TRUSTEE : 5 equally powerful identities.",
					columnHeaders);
			
			fileTxt.setFormatter(formatterHTML);
			latestLog.setFormatter(formatterHTML);
					    
			LOGGER.addHandler(fileTxt);
			LOGGER.addHandler(latestLog);
			
		} catch (SecurityException e) {
			
			System.out.println( "security exception. Cannot create log file.");
			
		} catch (IOException e) {
			
			System.out.println( "I/O exception. Cannot create log file.");

		}
		

	}
	
	
	public static class threadIDLogRecord extends LogRecord {

		Integer threadId = null;
		
		public threadIDLogRecord(Level level, String msg, Integer threadID ) {
		
			super(level, msg);
			
			this.threadId = threadID;
			
		}
		
		
	}
	
	TrusteeClientSimulatorThread(SignedBlock genesis, WeightedIdentity identity) {
		
		super(identity);
		
		blockTree = new BlockTree(genesis);
		
		thisCount = counter++;
		
	}
	
	
	int blockCounter = 0;
	
	protected synchronized void handleRecieveBlock(Block block) {
		
		System.out.println("block recieved!");
		
		// here goes the code to handle the block broadcast
		
		// get the block , add it to the tree
		
		SignedBlock sb = (SignedBlock)block;
		
		try {
			
			this.blockTree.addBlock(sb);
			
			// generate a diagram
			

			
			String diagramFilename = "graphs/graph"+logPrefix+"-"+this.thisCount+"-"+ blockCounter++ +".svg";
			this.blockTree.printDOT( diagramFilename );
			
			LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
											   "<a href=\"" + diagramFilename + "\">block recieved #"+ sb.serialNum +"</a>", 
											   thisCount ) );
			
		} catch (BlockHasNoPreviousException e) {
			
			LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
					   "<span style=\"color:red;\">BLOCK HAS NO PREVIOUS</span>",
					thisCount ) );
			
		} catch (IOException e) {
			
			LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
					   "<span style=\"color:red;\">CANT WRITE FILE</span>",
					thisCount ) );
			
		} catch (Exception e ) {
			
			LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
					   "<span style=\"color:red;\">ERRAR</span>",
					thisCount ) );
			
		}
		
	}
	
	protected void runMainLoop() {
		
		for( int i=0; i<30; i++ ){
			
				// here be the main client thread loop
			    // loop is number blocks broadcasted by each client

				try {
					
					sleep( rand.nextInt(1000) );
					
					// now generate a block
					
					SignedBlock newBlock = null;
					try {
						
						newBlock = blockTree.createBestBlock(this.identity);
						
					} catch (Exception e) {
						
						LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
								   "<span style=\"color:red;\">THREAD FAIL</span>",
								thisCount ) );
						
					}
					
					LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
							    "new block #" + newBlock.serialNum  
							    , thisCount ) );

					// and broadcast it
				
					broadcaster.broadcastBlock(newBlock);
					
				} catch (InterruptedException e) {
					
					LOGGER.warning("INTERRUPT!");
					
				}
			
		}
	}

	protected void threadFailed(Exception e) {
		
		LOGGER.log( new ThreadIDLogRecord( Level.INFO, 
				"<span style=\"color:red;\">THREAD FAILED: " + e.getMessage() + "</span>",
			    thisCount ) );
	
	}
	
	public static void main(String[] args) {
		
		// now create a bunch of clientSimulator threads
		
		WeightedIdentity i1 = new WeightedIdentity("i1",1.00,"blue");
		WeightedIdentity i2 = new WeightedIdentity("i2",1.00,"red");
		WeightedIdentity i3 = new WeightedIdentity("i3",1.00,"green");
		WeightedIdentity i4 = new WeightedIdentity("i4",1.00,"orange");
		WeightedIdentity i5 = new WeightedIdentity("i5",3.00,"violet");

		
		// i1 is the ISSUER so we will sign the genesis block with it
		
		SignedBlock genesis = new SignedBlock(null, i1);
		
		TrusteeClientSimulatorThread thread1 = new TrusteeClientSimulatorThread( genesis, i1);
		TrusteeClientSimulatorThread thread2 = new TrusteeClientSimulatorThread( genesis, i2);
		TrusteeClientSimulatorThread thread3 = new TrusteeClientSimulatorThread( genesis ,i3);
		TrusteeClientSimulatorThread thread4 = new TrusteeClientSimulatorThread( genesis ,i4);
		TrusteeClientSimulatorThread thread5 = new TrusteeClientSimulatorThread( genesis ,i5);

		thread1.start();		
		thread2.start();		
		thread3.start();
		thread4.start();
		thread5.start();


	}

	

}
