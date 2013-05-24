package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ThreadIDLogRecord extends LogRecord {

	Integer columnNum = null;
	
	public ThreadIDLogRecord(Level level, String msg, Integer columnNum ) {
	
		super(level, msg);
		
		this.columnNum = columnNum;
		
	}
	
	
}
