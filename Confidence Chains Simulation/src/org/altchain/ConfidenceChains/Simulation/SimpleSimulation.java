package org.altchain.ConfidenceChains.Simulation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;


public class SimpleSimulation {

	/**
	 * @param args
	 */
	
	public static class Block {
		
		public Integer id;
		
		public Block previous;
		
		public boolean isGenesis = false;
		
		public Integer signature;
		
		Block( Integer id, Block previous, boolean isGenesis, Integer IdentitySignature ){
			
			this.id = id;
			this.previous = previous;
			this.isGenesis = isGenesis;
			this.signature = IdentitySignature;
			
		}
		
		public Integer confidenceValue;
		
	}
	// just a simple simulation to show how chains are formed under different circumstance
		
		
		
	public static int timeLimit = 10;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// set up identity weights
		
		int idCount = 0;
		
		HashMap<Integer, Integer> Identities = new HashMap< Integer, Integer >();
		
		setUpIdentityWeights( Identities );
		
		// create the Block Chain
		
		LinkedList<Block> BlockChain = new LinkedList<Block>();
		
		// create the Genesis Issuance Block
		
		Block GenesisBlock = new Block(idCount++, null, true, 1 );
		//Block sampleBlock = new Block(idCount++, GenesisBlock, false, 2 );

		
		// add it to the chain.
		
		BlockChain.add( GenesisBlock );
		//BlockChain.add( sampleBlock );
		
		System.out.println("CONFIDENCE CHAINS SIMULATION.\n\n");
		System.out.println("-----------------------------------------------\n\n");

		//printChain( BlockChain );
	
		// main time loop
		
		// this is a simplified simulation where one block is added to the chain 
		// and the highest confidence is chosen for next time T
		
		
		for( int t=0; t < timeLimit; t++ ) {
			
			LinkedList<Block> tempBestChain = new LinkedList<Block>( BlockChain );
			int confidenceValueTempBestChain = 0;
			
			// for each identity compute the confidence of the chain with a new block signed by it
			for ( int identity=0; identity < Identities.size(); identity++ ){
				
				// make a copy of block chain
				LinkedList<Block> tempChain = new LinkedList<Block>( BlockChain );
				
				// add a block signed by the identity
				Block tempBlock = new Block(idCount++, BlockChain.getLast() , false, identity );
				tempChain.addLast(tempBlock);
				
				int confidenceValue = computeConfidenceValue( tempBestChain, Identities );
				// if its the most confident, then replace tempBestChain with it.
				if( confidenceValue > confidenceValueTempBestChain ) {
					tempBestChain = tempChain;
					confidenceValueTempBestChain = confidenceValue;
				}
				
				//rinse, repeat
				
			}
			
			BlockChain = tempBestChain;
			printChain(BlockChain);
			
		}
		

	}

	private static int computeConfidenceValue(LinkedList<Block> tempBestChain, HashMap<Integer, Integer> identities) {
			
		
		// how to compute value of confidence chain?
		
		// first create an array the size of numIdentities
		
		// start at the HEAD of the chain
		
		// for each block until the TAIL of chain
		
		  // get signature + weight associated with signature
		
		  // set the array, indexed by identity, to the weight
		
		  // add to Total Confidence Value sum of all weights in Array.	
		
		int sum = 0;
		
		int[] Weights = new int[ identities.size() ];
		
		//System.out.println("COMPUTING CONFIDENCE...");
		
		//printChain( tempBestChain );
		
		for (int b=(tempBestChain.size()-1); b >= 0; b-- ) {
		
			Weights[ tempBestChain.get(b).signature ] = 
				identities.get( 
						tempBestChain.get(b).signature );
			
			// summate all the weights
			for (int a=0; a< Weights.length; a++) {
			    sum += Weights[a];
			}
			
		}
		
		//System.out.println("confidence value: "+sum+"\n\n");
		
		return sum;
		
	}

	private static void printChain(LinkedList<Block> blockChain) {
		
		System.out.print("chain( ");
				
		for( int i=0; i< blockChain.size(); i++ ){
			
			printBlock( blockChain.get(i) );
			System.out.print(", ");
				
		}
		
		System.out.print(")\n");

		
	}
	
	private static void printBlock( Block block ) {

		System.out.print( block.id + ":["+block.signature+"]");

	}

	private static void setUpIdentityWeights(
			HashMap<Integer, Integer> identities) {
		
		// we will have three identities with weights 1= 1, 2= 2, 3= 3;
		
		identities.put(0, 30);
		identities.put(1, 20);
		identities.put(2, 10);

		
	}

}
