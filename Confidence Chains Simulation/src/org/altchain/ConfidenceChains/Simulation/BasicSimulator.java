package org.altchain.ConfidenceChains.Simulation;

import java.util.Iterator;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.BlockChain.BlockChain;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BasicSimulator {
	
	// this class creates a very basic simulation of the Confidence Chains algorithm
	
	// it cycles through time and attempts to simulate how the chains will be formed
	
	// better, more accurate simulations are forthcoming
	

	// number of time quanta
	
	public static int timeDuration = 20;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// first we set up the power structure that we want to test
		
		WeightedIdentitySet identities = new WeightedIdentitySet();
		
		// a simple TRIUMVIRATE structure ie. 3 equally powerful identities
		
		WeightedIdentity t1 = new WeightedIdentity("T1",1.00);
		WeightedIdentity t2 = new WeightedIdentity("T2",5.00);
		WeightedIdentity t3 = new WeightedIdentity("T3",1.00);
		
		// now add them to our identity set
		
		identities.IdentitySet.add(t1);
		identities.IdentitySet.add(t2);
		identities.IdentitySet.add(t3);
		
		// now set up the block chain, genesis block signed by t1
		
		SignedBlock genesis = new SignedBlock(null, t1);
		BlockChain blockChain = new BlockChain(genesis);
		
		blockChain.printChain("BEGIN...");
		

		
		// begins with a confidence score of 10.0
	
		for( int t=0; t<timeDuration; t++ ){
			
			// so what we do here is for each time t, we determine
			// which identity can create the most confidence chain
			
			// used to keep track of best chain in the loop
			BlockChain thisChain = new BlockChain(blockChain);
			double thisConfidenceScore = 0.0;
			
			Iterator IdIter = identities.IdentitySet.iterator();
			  while (IdIter.hasNext()) {
				WeightedIdentity id = (WeightedIdentity) IdIter.next();
				
				// make a copy of the main chain
				BlockChain localChain = new BlockChain(blockChain);
								
				// now add a block to it signed by the id
				SignedBlock newBlock = new SignedBlock(null, id);
				localChain.chain.add(newBlock);
				
				// now measure the Confidence Score
				double localScore = localChain.computeConfidenceScore();
				
				// is it higher than the current tracker?
				if( localScore > thisConfidenceScore ){
					// if so then replace the tracker chain with the local chain
					thisChain = localChain;
					thisConfidenceScore = localScore;
				}
			  }
			  
			  // now the best chain should be in thisChain
			  
			  blockChain = thisChain;
			  
			  // print it
			  
			  blockChain.printChain("t="+t);
			
			
		}
		

	}

}
