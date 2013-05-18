package org.altchain.ConfidenceChains.Simulation.Block;

import java.rmi.server.UID;
import java.util.HashMap;
import java.util.UUID;

import org.altchain.ConfidenceChains.Simulation.Identity.Identity;


public class SignedBlock extends Block {
	
	
	public Identity signature;

	SignedBlock(UUID id, Block previous, Identity signature ) {
		
		super(id, previous);
		this.signature = signature;
		
		// put it in the lookup
		uidLookup.put(id, this);
		
	}
	
	public SignedBlock( Block previous, Identity signature ){
	
		super(UUID.randomUUID(), previous);
		this.signature = signature;
		
		// put it in the lookup
		uidLookup.put(id, this);

	}
	
	public static SignedBlock Sign( Block block, Identity signature ){
		
		// remove it from the lookup
		uidLookup.remove(block.id);
		
		//now create new signed block
		SignedBlock sb = new SignedBlock( block.id, block.previous, signature );
	
		//now put it back in the lookup
		uidLookup.put(sb.id, sb);
		
		return sb;
		
	}
	
	public void printBlock(){
		System.out.print( "sig(" + signature.name + ")" );
	}


	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
