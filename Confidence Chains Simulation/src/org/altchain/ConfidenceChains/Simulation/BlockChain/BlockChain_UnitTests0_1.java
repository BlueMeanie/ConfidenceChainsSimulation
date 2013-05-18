package org.altchain.ConfidenceChains.Simulation.BlockChain;

import static org.junit.Assert.*;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.Identity.Identity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BlockChain_UnitTests0_1 {
	
	BlockChain testChain;
	WeightedIdentity issuerID = new WeightedIdentity("issuer", 100.00);
	WeightedIdentity ctrptyID1 = new WeightedIdentity("counterparty1", 20.00);
	WeightedIdentity ctrptyID2 = new WeightedIdentity("counterparty2", 20.00);
	WeightedIdentity ctrptyID3 = new WeightedIdentity("counterparty2", 10.00);
	WeightedIdentity ctrptyID4 = new WeightedIdentity("counterparty2", 10.00);


	
	
	@Before
	public void setUp() throws Exception {
			
		Block genesis = new Block(null);
		SignedBlock sb = SignedBlock.Sign(genesis, issuerID);
		
		testChain = new BlockChain(sb);
		testChain.addBlock( new SignedBlock(null, ctrptyID1 ) );
		testChain.addBlock( new SignedBlock(null, ctrptyID2 ) );
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBlockChain() {
		assertTrue("testing that Weightmap has 5 elements", WeightedIdentity.weightTable.size() == 5 );
	}

	@Test
	public void testAddBlock() {
		
		// create a copy of the chain
		BlockChain newChain = new BlockChain(testChain);
	
		// add a block
		newChain.addBlock( new SignedBlock(null, ctrptyID1) );
		
		// test to see that the length of new is 1 more than length of first
		assertTrue("Chain copy with block added has 1 more length than original", 
				testChain.chain.size() == newChain.chain.size() -1
		);

		
	}

	@Test
	public void testComputeConfidenceScore() {
		double Score = testChain.computeConfidenceScore();
		assertTrue("testing that first testChain has a Conf of 200.0", Score == 200.0 );
	}
	
	@Test
	public void testChain2() {
		testChain.addBlock( new SignedBlock(null, ctrptyID1 ) );
		testChain.addBlock( new SignedBlock(null, ctrptyID2 ) );
		testChain.addBlock( new SignedBlock(null, issuerID ) );
		double Score = testChain.computeConfidenceScore();
		assertTrue("testing that second testChain has a Conf of 780.0", Score == 780.0 );
	}

	@Test
	public void testPrintChain() {
		testChain.printChain("testPrintChain()");
	}

}
