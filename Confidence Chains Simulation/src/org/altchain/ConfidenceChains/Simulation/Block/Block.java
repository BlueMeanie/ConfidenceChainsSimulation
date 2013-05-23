package org.altchain.ConfidenceChains.Simulation.Block;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Block {
	
	public UUID id;
	
	private static int counter = 0;
	public int serialNum = counter++;
	
	public Block previous;
	
	public static ConcurrentHashMap<UUID, Block> uidLookup = new ConcurrentHashMap<UUID,Block>();
	
	public Block( UUID id, Block previous ){
		this.id = id;
		this.previous = previous;
		
		uidLookup.put(id, this);
	}
	
	public Block( Block previous ){
		this.id = UUID.randomUUID();
		this.previous = previous;
		
		uidLookup.put(this.id, this);
	}
	
	// txs could go here

	public void printBlock(){
		System.out.print("#");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	

}
