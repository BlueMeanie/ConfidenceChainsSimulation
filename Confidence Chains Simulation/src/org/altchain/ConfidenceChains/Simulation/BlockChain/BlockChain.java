package org.altchain.ConfidenceChains.Simulation.BlockChain;

import java.io.OutputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.Identity.Identity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BlockChain {

	public LinkedList<Block> chain = new LinkedList<Block>();
	/**
	 * @param args
	 * @return 
	 */
	
	BlockChain( Block genesisBlock ){	
		addBlock(genesisBlock);	
	}
	
	//copy constructor
	// note: does not copy the blocks themselves, just the list
	
	BlockChain( BlockChain orig ){
		// make a complete copy
		this.chain = new LinkedList<Block>(orig.chain);
	}
	
	void addBlock( Block b ){
		chain.addLast(b);
	}
	
	
	
	double computeConfidenceScore(  ) {

		double confidenceScore = 0;
		
		// Generate an iterator. Start at end of list
		ListIterator li = chain.listIterator(chain.size());
		
		// now create a tally system for all the identities in the system
		HashSet<WeightedIdentity> IDTally = new HashSet<WeightedIdentity>();
		
		// Iterate all blocks in reverse.
		while(li.hasPrevious()) {
			
		  double blockScore = 0;
			
		  SignedBlock b = (SignedBlock)li.previous();
		  
		  // for each block, tally the identity
		  // this assumes the block was signed by a weighted identity
		  // if not will throw an exception
		  IDTally.add((WeightedIdentity)b.signature);
		  
		  // now simply add all the compounded weights of the identities in the tally
		  Iterator IdIter = IDTally.iterator();
		  while (IdIter.hasNext()) {
			WeightedIdentity id = (WeightedIdentity) IdIter.next();
			blockScore += id.weight;
		  }
		  
		  // for debug pursposes
		  //System.out.println("blockscore: "+blockScore);
		  
		  confidenceScore += blockScore;
		  
		}
		
		return confidenceScore;
		
	}
	
	void printChain( ){
			
		ListIterator itr = chain.listIterator();
		while(itr.hasNext())
	    {
	      Block b = (Block) itr.next();
	      b.printBlock();
	      if (itr.hasNext()) System.out.print("|");
	    }
		
		
	}
	
	void printChain( String prefixMessage ){
		
		System.out.print( prefixMessage + ":");
		
		this.printChain();
		
		System.out.println("");
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
