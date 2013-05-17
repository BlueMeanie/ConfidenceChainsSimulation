package org.altchain.ConfidenceChains.Simulation.BlockChain;

import java.util.LinkedList;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BlockChain {

	LinkedList<Block> chain;
	/**
	 * @param args
	 * @return 
	 */
	
	BlockChain( Block genesisBlock ){	
		addBlock(genesisBlock);	
	}
	
	void addBlock( Block b ){
		chain.addLast(b);
	}
	
	double computeConfidenceScore( WeightedIdentitySet weights ) {
		
		return 0.0;
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
