/**
 * 
 */
package org.altchain.ConfidenceChains.Simulation.BlockTree;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.LinkedList;

import org.altchain.ConfidenceChains.Simulation.Block.BlockHasNoPreviousException;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author jjzeidner
 *
 */
public class BlockTree_UnitTests0_1 {

	/**
	 * @throws java.lang.Exception
	 */
	
	BlockTree sampleTree;
	
	SignedBlock sb5;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		
	}
	
	private BlockTree createSampleTree1(){
		
		WeightedIdentity t1 = new WeightedIdentity("t1",10.0, "blue");
		WeightedIdentity t2 = new WeightedIdentity("t2",10.0, "red");
		WeightedIdentity t3 = new WeightedIdentity("t3",10.0, "green");

		// create a bunch of blocks
		
		SignedBlock sb1 = new SignedBlock( null, t1 );
		SignedBlock sb2 = new SignedBlock( sb1, t2 );
		SignedBlock sb3 = new SignedBlock( sb1, t3 );
		SignedBlock sb4 = new SignedBlock( sb2, t1 );
					sb5 = new SignedBlock( sb4, t2 );
		SignedBlock sb6 = new SignedBlock( sb4, t3 );
		SignedBlock sb7 = new SignedBlock( sb4, t1 );
		SignedBlock sb8 = new SignedBlock( sb6, t2 );
		SignedBlock sb9 = new SignedBlock( sb6, t1 );
		SignedBlock sb10 = new SignedBlock( sb6, t3 );


		
		// create a BlockTree
		
		BlockTree myTree = new BlockTree(sb1);
		try {
			
			myTree.addBlock(sb2);
			myTree.addBlock(sb3);
			myTree.addBlock(sb4);
			
			myTree.printDOT("tree1.svg");
			
			myTree.addBlock(sb5);
			myTree.addBlock(sb6);
			myTree.addBlock(sb7);
			
			myTree.printDOT("tree2.svg");

			myTree.addBlock(sb8);
			myTree.addBlock(sb9);
			myTree.addBlock(sb10);
			
			myTree.printDOT("tree3.svg");



			
		} catch (BlockHasNoPreviousException e) {
			
			fail("malformed blocks");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return myTree;
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
		sampleTree = createSampleTree1();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree#BlockTree(org.altchain.ConfidenceChains.Simulation.Block.Block, org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentitySet)}.
	 */
	@Test
	public void testBlockTree() {
		//fail("Not yet implemented");
		
		//System.out.println( "confidence score of root: "+sampleTree.root.confidenceScore );
	
		//System.out.println( "confidence score of sb5 : "+this.sampleTree.lookup.get( sb5.id ).confidenceScore );
	
	}

	/**
	 * Test method for {@link org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree#addBlock(org.altchain.ConfidenceChains.Simulation.Block.Block)}.
	 */
	@Test
	public void testAddBlock() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree#getMostConfidentChain()}.
	 */
	@Test
	public void testGetMostConfidentChain() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link org.altchain.ConfidenceChains.Simulation.BlockTree.BlockTree#shouldPublishBlock(org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity)}.
	 */
	@Test
	public void testShouldPublishBlock() {
		//fail("Not yet implemented");
	}
	
	@Test
	public void testPrintDOT() {
		
		try {
			sampleTree.printDOT( "ispdf.svg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
