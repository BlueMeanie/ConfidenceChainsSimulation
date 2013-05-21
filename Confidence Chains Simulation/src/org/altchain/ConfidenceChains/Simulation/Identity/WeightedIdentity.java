package org.altchain.ConfidenceChains.Simulation.Identity;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class WeightedIdentity extends Identity {
	
	public static ConcurrentHashMap<UUID,Double> weightTable = new ConcurrentHashMap<UUID,Double>();

	WeightedIdentity( String name, UUID id, double weight ) {
		
		super(name, id);
		// TODO Auto-generated constructor stub
		
		this.weight = weight;
		
		weightTable.put(id, weight);
		nameTable.put(id, name);
		
	}
	
	public WeightedIdentity( String name, double weight ) {
		
		super(name );
		// TODO Auto-generated constructor stub
		
		this.weight = weight;
		
		weightTable.put(id, weight);
		nameTable.put(id, name);

		
	}
	
	public WeightedIdentity( String name, double weight, String color ) {
	
		super(name );
		// TODO Auto-generated constructor stub
		
		this.weight = weight;
		this.color = color;
		
		weightTable.put(id, weight);
		nameTable.put(id, name);
		
	}

	
	

	public double weight;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
