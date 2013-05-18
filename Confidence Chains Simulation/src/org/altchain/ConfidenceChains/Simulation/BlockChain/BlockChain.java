package org.altchain.ConfidenceChains.Simulation.BlockChain;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.ListIterator;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BlockChain {

	LinkedList<Block> chain = new LinkedList<Block>();
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
		
		// first make an iterator and advance to end of list
		ListIterator itr = chain.listIterator();  
		while(itr.hasNext()) itr.next();
		
		//
		
		return 0.0;
		
	}
	
	void printChain( ){
		
		System.out.print("CHAIN:");
		
		ListIterator itr = chain.listIterator();
		while(itr.hasNext())
	    {
	      Block b = (Block) itr.next();
	      b.printBlock();
	      System.out.print("|");
	    }
	 
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
