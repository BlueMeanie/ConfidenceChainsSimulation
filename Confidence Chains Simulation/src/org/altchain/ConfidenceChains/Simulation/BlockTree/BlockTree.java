package org.altchain.ConfidenceChains.Simulation.BlockTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.UUID;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.BlockChain.BlockChain;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BlockTree {
	
	// this class maintains a tree of all blocks, generates chains from that tree, 
	// and offers operations on the tree
	
	HashMap<UUID,Node> lookup = new HashMap<UUID,Node>();
	
	HashSet<Node> leaves = new HashSet<Node>();

	public class Node {
		
		Block block;
		
		Node parent = null;
		
		// this is the confidence score of the chain headed by the node
		// it only needs to be computed once for each node
		double confidenceScore;
		
		public HashSet<Node> children = new HashSet<Node>();
		
		Node( Block b ){
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
			this.confidenceScore = computeNodeConfidenceScore();
			
		}

		private double computeNodeConfidenceScore() {
			
			// this is a simple and unoptimized way of doing this
			
			// basically we construct a BlockChain type from Leaf-to-Root path
			
			// then compute the ConfidenceScore
							  
			Node currentNode = this;
			BlockChain thisChain = new BlockChain(this.block);
				  
			while( currentNode != null ){
					  
				currentNode = currentNode.parent;

				if (currentNode != null) 
					thisChain.chain.addFirst( currentNode.block );
						  
			}
			
			return thisChain.computeConfidenceScore();
			
		}
		
	}
	
	Node root;
	
	// note that in our model the identity weights are FIXED, meaning that 
	// they do not change over time, if the weights are DYNAMIC, then the 
	// block chain confidence calculations need to be recomputed each time
	// they are polled.
	
	BlockTree( Block genesis, WeightedIdentitySet identities ) {
		
		root = new Node(genesis);
		
	}
	
	Node addBlock( Block block ){
		
		// first look up the previous id
		
		Node previousBlockNode = lookup.get( block.previous );
		
		// add the block to the Node's children
		
		Node retval = new Node(block);
		previousBlockNode.addChild(retval);
		
		
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


	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
