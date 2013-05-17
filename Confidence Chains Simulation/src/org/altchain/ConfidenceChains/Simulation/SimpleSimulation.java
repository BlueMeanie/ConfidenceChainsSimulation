package org.altchain.ConfidenceChains.Simulation;

import java.util.HashMap;
import java.util.LinkedList;


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
		
	}
	// just a simple simulation to show how chains are formed under different circumstance
		
		
		
	public static int timeLimit = 100;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		// set up identity weights
		
		HashMap<Integer, Integer> Identities = new HashMap< Integer, Integer >();
		
		setUpIdentityWeights( Identities );
		
		// create the Block Chain
		
		LinkedList<Block> BlockChain = new LinkedList<Block>();
		
		// create the Genesis Issuance Block
		
		Block GenesisBlock = new Block(0, null, true, 1 );
		Block sampleBlock = new Block(1, GenesisBlock, false, 2 );

		
		// add it to the chain.
		
		BlockChain.add( GenesisBlock );
		BlockChain.add( sampleBlock );
		
		System.out.println("CONFIDENCE CHAINS SIMULATION.\n\n");
		System.out.println("-----------------------------------------------\n\n");

		printChain( BlockChain );
	
		// main time loop
		
		for( int t=0; t < timeLimit; t++ ) {
			
			
			
			
			
		}
		

	}

	private static void printChain(LinkedList<Block> blockChain) {
		
		System.out.print("chain( ");
				
		for( int i=0; i< blockChain.size(); i++ ){
			
			System.out.print(i+":["+blockChain.get(i).signature+"] ");
			
		}
		
		System.out.print(")\n");

		
	}

	private static void setUpIdentityWeights(
			HashMap<Integer, Integer> identities) {
		
		// we will have three identities with weights 1= 1, 2= 2, 3= 3;
		
		identities.put(1, 1);
		identities.put(2, 2);
		identities.put(3, 3);

		
	}

}
