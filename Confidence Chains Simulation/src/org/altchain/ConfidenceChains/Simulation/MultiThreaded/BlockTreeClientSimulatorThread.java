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

public class BlockTreeClientSimulatorThread extends SimpleClientSimulatorThread {

	
	
	BlockTree blockTree;
	
	static int counter=0;
	
	int thisCount;
	
	private Logger LOGGER = null;
	
	static String logPrefix;
	static {
		
		logPrefix = String.valueOf( new java.util.Date().getTime() );

	}
	
	
	public static class threadIDLogRecord extends LogRecord {

		Integer threadId = null;
		
		public threadIDLogRecord(Level level, String msg, Integer threadID ) {
		
			super(level, msg);
			
			this.threadId = threadID;
			
		}
		
		
	}
	
	BlockTreeClientSimulatorThread(SignedBlock genesis, WeightedIdentity identity) {
		
		super(identity);
		
		blockTree = new BlockTree(genesis);
		
		thisCount = counter++;
		
		
		
		try {
			
			// set up logging.
			
			LOGGER = Logger.getLogger( BlockTreeClientSimulatorThread.class.getName() + thisCount );
			LOGGER.setLevel( Level.INFO );
			
			FileHandler fileTxt = new FileHandler( logPrefix + "-" + thisCount + "-log.html");
			FileHandler latestLog = new FileHandler( "latest-log-" + thisCount + ".html");
			
			// Create txt Formatter
		    HtmlLogFormatter formatterHTML = new HtmlLogFormatter();
		    fileTxt.setFormatter(formatterHTML);
		    latestLog.setFormatter(formatterHTML);
		    
		    LOGGER.addHandler(fileTxt);
		    LOGGER.addHandler(latestLog);
		    
		    //LOGGER.info("starting thread "+ thisCount +"...");
			
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
			
			LOGGER.log( new threadIDLogRecord( Level.INFO, 
											   "<a href=\"" + diagramFilename + "\">GRAPH</a>", 
											   thisCount ) );
			
		} catch (BlockHasNoPreviousException e) {
			
			//LOGGER.warning( "bad block: no previous hash.");
			
		} catch (IOException e) {
			
			//LOGGER.warning( "cant write file.");
			
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
					
					LOGGER.log( new threadIDLogRecord( Level.INFO, 
							    this.identity.color + " : new block " + newBlock.id, 
							    thisCount ) );

					// and broadcast it
					
					broadcaster.broadcastBlock(newBlock);
					
				} catch (InterruptedException e) {
					
					//LOGGER.warning("INTERRUPT!");
					
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
		
		thread1.start();
		
		thread2.start();
		
		thread3.start();
		
		// now concatenate log files
		
		List<String> files = new LinkedList<String>();
		
		for ( int i=0; i<counter; i++ ){
			
			files.add("latest-log-"+i+".html");
			
		}
		
		String finalFile = "latest-log-final2.html";
		
		try {
			concatLogFiles(files, finalFile);
		} catch (FileNotFoundException e) {
			System.err.println("LOG FILE CONCAT ERROR!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("LOG FILE CONCAT ERROR! IO issue.");
		}

		
		
	}

	private static void concatLogFiles(List<String> files, String finalFile)
			throws FileNotFoundException, IOException {
//		OutputStream out = new FileOutputStream(finalFile);
//	    byte[] buf = new byte[1 << 20];
//	    for (String file : files) {
//	        InputStream in = new FileInputStream(file);
//	        int b = 0;
//	        while ( (b = in.read(buf)) >= 0) {
//	            out.write(buf, 0, b);
//	            out.flush();
//	        }
//	    }
//	    out.close();
		
		PrintWriter pw = new PrintWriter(new FileOutputStream(finalFile));
      
        for (int i = 0; i < files.size(); i++) {

                //System.out.println("Processing " + files[i].getPath() + "... ");
                BufferedReader br = new BufferedReader(new FileReader( files.get(i) ) );
                String line = br.readLine();
                while (line != null) {
                        pw.println(line);
                        line = br.readLine();
                }
                br.close();
        }
        pw.close();

	}

}
