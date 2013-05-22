package org.altchain.ConfidenceChains.Simulation.BlockTree;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.BlockHasNoPreviousException;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.BlockChain.BlockChain;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet;

public class BlockTree {

	// this class maintains a tree of all blocks, generates chains from that
	// tree,
	// and offers operations on the tree

	ConcurrentHashMap<UUID, Node> lookup = new ConcurrentHashMap<UUID, Node>();

	Set<Node> leaves = Collections.synchronizedSet( new HashSet<Node>() );

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

		Node(SignedBlock b) {
			this.block = b;
			lookup.put(block.id, this);
		}

		void addChild(Node n) {
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
			
			// if this node is the most confident chain, update the value
			if ( n.confidenceScore > mostConfidentChainScore ){
				mostConfidentChain = n;
				mostConfidentChainScore = n.confidenceScore;
			}

		}

		private double computeNodeConfidenceScore() {

			// this is a simple and unoptimized way of doing this

			// basically we construct a BlockChain type from Leaf-to-Root path

			// then compute the ConfidenceScore

			System.out.println("\ncomputing confidence score. signed["
					+ this.block.signature.name + "], id=" + this.block.id
					+ " :");

			Node currentNode = this;
			BlockChain thisChain = new BlockChain(this.block);

			while (currentNode != null) {

				// System.out.println(">"+currentNode currentNode.block.id);

				currentNode = currentNode.parent;

				if (currentNode != null)
					thisChain.chain.addFirst(currentNode.block);

			}

			thisChain.printChain("chain ");
			double score = thisChain.computeConfidenceScore();
			System.out.println("score :" + score);

			return score;

		}

	}

	Node root;

	// note that in our model the identity weights are FIXED, meaning that
	// they do not change over time, if the weights are DYNAMIC, then the
	// block chain confidence calculations need to be recomputed each time
	// they are polled.
	
	Node mostConfidentChain;
	
	double mostConfidentChainScore =0;

	public BlockTree(SignedBlock genesis) {

		root = new Node(genesis);

		// add it to the lookup

		lookup.put(genesis.id, root);
		
		// add it to leaves
		
		leaves.add(root);

		// set the initial confidence score

		root.confidenceScore = ((WeightedIdentity) (root.block.signature)).weight;

	}

	public Node addBlock(SignedBlock block) throws BlockHasNoPreviousException {

		// first look up the previous id

		Node previousBlockNode = lookup.get(block.previous.id);

		if (previousBlockNode == null)
			throw new BlockHasNoPreviousException();

		// add the block to the Node's children

		Node retval = new Node(block);
		previousBlockNode.addChild(retval);

		lookup.put(block.id, retval);

		return retval;

	}
	

	// this is where the p2p magic happens
	// this function determines the basic p2p operation of our simulation
	// it gives us the block that is worth publishing to the network
	
	// many questions need to be answered about this algorithm
	
	// 1) is the best block ALWAYS a direct descendant of a leaf node?
	
	// 2) are there any situations where it's worthwhile to publish a block that 
	//    does not result in the highest score chain known to the node?
	
	// 3) in what situations is it not worth publishing a block?
	
	public SignedBlock createBestBlock( WeightedIdentity identity ){
		
		// this is not proven but it proves basic functionality
		
		// cycle through the leaf nodes 
		
		// keep the best block on hand
		SignedBlock bestBlock = null;
		double bestScore = 0;
		
		for ( Node leaf : leaves ){
			
			// temporarily add a child node
			
			SignedBlock sb = new SignedBlock( leaf.block, identity);
			
			Node testNode = new Node( sb );
			
			leaf.children.add( testNode );
			testNode.parent = leaf;
			
			// now compute confidence score
			
			double thisScore = testNode.computeNodeConfidenceScore();
			if( thisScore > bestScore ){
				bestScore = thisScore;
				bestBlock = sb;
			}
			
			// now detach the node
			leaf.children.remove(testNode);
			lookup.remove(testNode.block.id);
			
			// node gets destroyed by loop scope
			
		}
				
		return bestBlock;
		
	}


	
///////////////////////////////////////////////
//  tree drawing functions
///////////////////////////////////////////////	

	private static void traverseDOT(Node n, BufferedWriter out)
			throws IOException {

		if (n.parent != null) {

			out.write(Integer.toString(n.nodeNum));
			out.write(" -> ");
			out.write(Integer.toString(n.parent.nodeNum) + ";");

		}

		for (Node child : n.children) {
			traverseDOT(child, out);
		}

	}

	public void printDOT(String pathname) throws IOException {

		// requires DOT to be available in the shell

		Process process = Runtime.getRuntime().exec("dot -Tsvg");

		OutputStream stdin = process.getOutputStream();
		InputStream stderr = process.getErrorStream();
		InputStream stdout = process.getInputStream();

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(stdin));

		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stdout));
		FileOutputStream fOut = new FileOutputStream(pathname);

		out.write("digraph blocktree {");

		for (UUID NodeID : lookup.keySet()) {
			Node nn = lookup.get(NodeID);

			out.write(formatDOTNode(nn));

			// a [label="Foo"];
		}

		traverseDOT(root, out);

		out.write("}");

		// Close the output stream
		out.close();

		// put it into the final file
		String line = reader.readLine();
		while (line != null) {
			fOut.write(line.getBytes());
			fOut.flush();
			line = reader.readLine();
		}

	}

	// http://en.wikipedia.org/wiki/DOT_%28graph_description_language%29

	private String formatDOTNode(Node nn) {
		//return nn.nodeNum + "[shape=box] [color=" + nn.block.signature.color
		//		+ "] [label=\"" + nn.block.signature.name + 
		//		":" + ((WeightedIdentity) (nn.block.signature)).weight + "\\n"
		//		+ nn.confidenceScore + "\"];\n";
		
		return nn.nodeNum + "[shape=box] [color=" + nn.block.signature.color
				+ "] [label=\"" + nn.nodeNum + 
				"\\n"
				+ nn.confidenceScore + "\"];\n";
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
