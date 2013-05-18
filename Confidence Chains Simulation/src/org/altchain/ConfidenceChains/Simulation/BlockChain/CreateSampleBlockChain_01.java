package org.altchain.ConfidenceChains.Simulation.BlockChain;

import static org.junit.Assert.*;

import org.altchain.ConfidenceChains.Simulation.Block.Block;
import org.altchain.ConfidenceChains.Simulation.Block.SignedBlock;
import org.altchain.ConfidenceChains.Simulation.Identity.Identity;
import org.altchain.ConfidenceChains.Simulation.Identity.WeightedIdentity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CreateSampleBlockChain_01 {
	
	BlockChain testChain;
	WeightedIdentity issuerID = new WeightedIdentity("issuer", 100.00);
	WeightedIdentity ctrptyID1 = new WeightedIdentity("counterparty1", 20.00);
	WeightedIdentity ctrptyID2 = new WeightedIdentity("counterparty2", 20.00);

	
	
	@Before
	public void setUp() throws Exception {
			
		Block genesis = new Block(null);
		SignedBlock sb = SignedBlock.Sign(genesis, issuerID);
		
		testChain = new BlockChain(sb);
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBlockChain() {
		testChain.addBlock( new SignedBlock(null, ctrptyID1 ) );
		testChain.addBlock( new SignedBlock(null, ctrptyID2 ) );
		testChain.printChain();
	}

	@Test
	public void testAddBlock() {
		fail("Not yet implemented");
	}

	@Test
	public void testComputeConfidenceScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testPrintChain() {
		fail("Not yet implemented");
	}

}
