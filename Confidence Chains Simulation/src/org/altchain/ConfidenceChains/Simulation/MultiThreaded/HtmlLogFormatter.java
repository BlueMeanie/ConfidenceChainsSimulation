package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.altchain.ConfidenceChains.Simulation.MultiThreaded.BlockTreeClientSimulatorThread.threadIDLogRecord;

public class HtmlLogFormatter extends Formatter {

	// This method is called for every log records
	
	  static long millisecStart = 0;
	
	  public String format(LogRecord rec) {
		  
		if( millisecStart == 0 ) {
			
			millisecStart = rec.getMillis();
			
		}
		
		threadIDLogRecord idRec = (threadIDLogRecord)rec;
						  
	    StringBuffer buf = new StringBuffer( 1000  );
	    // Bold any levels >= WARNING
	    buf.append( "<div style=\"position: absolute; top: " + ( rec. getMillis() - millisecStart ) * 6  + ";" +
	                                                 "left: " + idRec.threadId * 100 + "\">" );

	    if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
	    	
	      buf.append( "<b>" );
	      buf.append( rec.getLevel() );
	      buf.append( "</b>" );
	      
	    } else {
	      buf.append( rec.getLevel() );
	    }
	    
	    buf.append( " " + calcDate(rec.getMillis()) );
	    buf.append( " " + formatMessage(rec) );
	    
	    buf.append("</div>\n");
	    
	    return buf.toString();
	    
	  }

	  private String calcDate(long millisecs) {
	    SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
	    Date resultdate = new Date(millisecs);
	    return date_format.format(resultdate);
	  }

	  // This method is called just after the handler using this
	  // formatter is created
	  public String getHead(Handler h) {
		  
	    return  "<html><title>Confidence Chains Simulation</title><body>";
	    
	  }

	  // This method is called just after the handler using this
	  // formatter is closed
	  public String getTail(Handler h) {
		  
	    return "</body></html>"; 

	  }

}
