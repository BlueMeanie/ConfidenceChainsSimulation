package org.altchain.ConfidenceChains.Simulation.Identity;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Identity {
	

	public static ConcurrentHashMap<UUID,String> nameTable = new ConcurrentHashMap<UUID,String>();

	public String name;
	
	UUID id;
	
	// used for graph drawing
	public String color = null;
	
	Identity( String name, UUID id ){
		
		this.name = name;
		this.id = id;
		
		nameTable.put(this.id, this.name);
		
	}
	
	Identity( String name ){
		
		this.name = name;
		this.id = UUID.randomUUID();
		
		nameTable.put(this.id, this.name);
				
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
