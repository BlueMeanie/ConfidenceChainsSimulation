package org.altchain.ConfidenceChains.Simulation.BlockTree;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.BlockHasNoPreviousException;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.BlockChain.BlockChain;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BlockTree {
	
	// this class maintains a tree of all blocks, generates chains from that tree, 
	// and offers operations on the tree
	
	HashMap<UUID,Node> lookup = new HashMap<UUID,Node>();
	
	HashSet<Node> leaves = new HashSet<Node>();

	static int nodeCounter = 0;
	
	public class Node {
				
		SignedBlock block;
		
		Node parent = null;
		
		// just a simple serial number
		int nodeNum = nodeCounter++;
		
		// this is the confidence score of the chain headed by the node
		// it only needs to be computed once for each node
		double confidenceScore;
		
		public HashSet<Node> children = new HashSet<Node>();
		
		Node( SignedBlock b ){
			this.block = b;
			lookup.put(block.id, this);
		}
		
		void addChild( Node n ){
			// remove from leaves
			leaves.remove(this);
			// add new leaf
			leaves.add(n);
			
			// add the child
			children.add(n);
			// link parent in new child
			n.parent = this;
			
			// now compute the confidence score for the node
			n.confidenceScore = n.computeNodeConfidenceScore();
			
		}

		private double computeNodeConfidenceScore() {
			
			// this is a simple and unoptimized way of doing this
			
			// basically we construct a BlockChain type from Leaf-to-Root path
			
			// then compute the ConfidenceScore
			
			System.out.println("\ncomputing confidence score. signed["+this.block.signature.name+"], id="+this.block.id+" :");
							  
			Node currentNode = this;
			BlockChain thisChain = new BlockChain(this.block);
				  
			while( currentNode != null ){
				
				//System.out.println(">"+currentNode currentNode.block.id);
					 
				currentNode = currentNode.parent;

				if (currentNode != null) 
					thisChain.chain.addFirst( currentNode.block );
						  
			}
			
		    thisChain.printChain("chain ");
			double score = thisChain.computeConfidenceScore();
			System.out.println("score :"+score);
			
			return score;
			
		}
		
	}
	
	Node root;
	
	// note that in our model the identity weights are FIXED, meaning that 
	// they do not change over time, if the weights are DYNAMIC, then the 
	// block chain confidence calculations need to be recomputed each time
	// they are polled.
	
	BlockTree( SignedBlock genesis ) {
		
		root = new Node(genesis);
		
		// add it to the lookup
		
		lookup.put(genesis.id, root);
		
		// set the initial confidence score
		
		root.confidenceScore = ((WeightedIdentity)(root.block.signature)).weight;
		
	}
	
	Node addBlock( SignedBlock block ) throws BlockHasNoPreviousException{
		
		// first look up the previous id
		
		Node previousBlockNode = lookup.get( block.previous.id );
		
		if ( previousBlockNode == null ) throw new BlockHasNoPreviousException();
		
		// add the block to the Node's children
		
		Node retval = new Node(block);
		previousBlockNode.addChild(retval);
		
		lookup.put(block.id, retval);
		
		return retval;
		
	}
	
	BlockChain getMostConfidentChain(){
		
		// form a BlockChain from each leaf in the tree to the root
		
		// iterate through the leaves
		
		Iterator leavesIter = leaves.iterator();
		  while (leavesIter.hasNext()) {
			  
			Node tail = (Node) leavesIter.next();
			
			// create a new BlockChain by iterating from each leaf to the root
			// then compare the confidence values of the chains
			// and then return the chain with the highest score
			
		
		  }
		
		return null;
		
	}
	
	// key algorithm
	
	// this is the heart of our simulation algorithm
	// it tells us something very simple
	// if the SimulationThread were to publish a block under a specific identity
	// would it result in a chain of Confidence Score with MORE GAIN than it's own 
	// identity weight.
	
	// another way to explain:
	
	// any node can publish a block and increase the confidence score simply because 
	// the chain is longer by 1 block.  Thus in the LOWEST CASE, the node will increase the CS
	// by the value of it's identity weight.  Thus, it's not typically worth publishing, 
	// because we can be certain that another identity can offer more.
	
	// this function will tell us whether a block is worth broadcasting
	
	boolean shouldPublishBlock( WeightedIdentity identity ){
		
		return false;
		
	}
	
	private static void traverseDOT( Node n, BufferedWriter out ) throws IOException{
		
		if ( n.parent != null ){
			
			out.write( Integer.toString(n.nodeNum) );
			out.write(" -> ");
			out.write( Integer.toString(n.parent.nodeNum) + ";");
			
		}
		
		for ( Node child : n.children ) {
			traverseDOT( child, out );
		}
		
	}

	void printDOT( LinkedList<String> DOTData, String pathname ){
		
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(pathname);
			  final BufferedWriter out = new BufferedWriter(fstream);
			  
			  out.write("digraph blocktree {");
			  
			  for ( UUID NodeID : lookup.keySet() ){
				  Node nn = lookup.get(NodeID);
				  
				  out.write( nn.nodeNum + "[shape=box] [label=\"" + nn.block.signature.name + ":"+((WeightedIdentity)(nn.block.signature)).weight+"\"];\n" );
				  
				  //a [label="Foo"];
			  }
			  
			  traverseDOT( root, out );
			  
			  out.write("}");
			  
			  //Close the output stream
			  out.close();
			  
			  }catch (Exception e){  //Catch exception if any
				  
				  System.err.println("Problem Writing File: " + e.getMessage());
				  
		}
		
	}

	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
