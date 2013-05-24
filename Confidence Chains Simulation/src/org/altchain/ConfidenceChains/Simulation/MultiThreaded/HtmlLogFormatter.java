package org.altchain.ConfidenceChains.Simulation.MultiThreaded;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.altchain.ConfidenceChains.Simulation.MultiThreaded.TriumvirateClientSimulatorThread.threadIDLogRecord;

public class HtmlLogFormatter extends Formatter {

	// This method is called for every log records
	
	  static long millisecStart = 0;
	  
	  String title;
	  
	  String introMsg;
	  
	  String[] columnHeaders;

	  
	  HtmlLogFormatter ( String title, String introMsg, String[] columnHeaders ){
		  
		  super();
		  
		  this.title = title;
		  
		  this.introMsg = introMsg;
		  
		  this.columnHeaders = columnHeaders;
		  
	  }
	
	  public String format(LogRecord rec) {
		  
		if( millisecStart == 0 ) {
			
			millisecStart = rec.getMillis();
			
		}
		
		threadIDLogRecord idRec = (threadIDLogRecord)rec;
						  
	    StringBuffer buf = new StringBuffer( 1000  );
	    // Bold any levels >= WARNING
	    buf.append( "<div style=\"position: absolute; top: " + calculateYPosition(rec) + ";" +
	                                                 "left: " + ( ( idRec.threadId * 300 ) + 40 ) + "\">\n" );
	    
	    //<div style="position: absolute; top: 648;width:100%;height:1px;background-color:#CCCCCC;">


	    /*if (rec.getLevel().intValue() >= Level.WARNING.intValue()) {
	    	
	      buf.append( "<b>" );
	      buf.append( rec.getLevel() );
	      buf.append( "</b>" );)
	      
	    } else {
	      buf.append( rec.getLevel() );
	    }
	    
	    buf.append( " " + calcDate(rec.getMillis()) );*/
	    
	    buf.append( formatMessage(rec) );
	    
	    buf.append("</div>\n");
	    
	    buf.append( "<div style=\"position: absolute; top: " + ( calculateYPosition(rec) + 18 ) + ";width:100%;height:1px;background-color:#CCCCCC;\">\n" );
	    buf.append("</div>\n");

	    
	    return buf.toString();
	    
	  }
	  
		private long yLayoutOffset = 200;


	private long calculateYPosition(LogRecord rec) {
		return ( ( rec. getMillis() - millisecStart ) * 6 ) + yLayoutOffset;
	}

	  private String calcDate(long millisecs) {
	    SimpleDateFormat date_format = new SimpleDateFormat("MMM dd,yyyy HH:mm");
	    Date resultdate = new Date(millisecs);
	    return date_format.format(resultdate);
	  }

	  // This method is called just after the handler using this
	  // formatter is created
	  public String getHead(Handler h) {
		  
		  StringBuffer buf = new StringBuffer();
		  
	      buf.append( "<html><title>"+this.title+"</title><body><h1>" + this.title + "</h1>" + "<p>"+ this.introMsg + "</p>" );
	    
	      // also make vertical rulers for each thread
	      
	      // should have same amount of column headers as threads
	      assert( columnHeaders.length == TriumvirateClientSimulatorThread.counter );
	    		
	      for( int i=0; i< TriumvirateClientSimulatorThread.counter; i++ ) {
	    	  
	  	    buf.append( "<div style=\"position: absolute; left: " + ( (i*300) + 40 ) + ";top:200px;height:100000px;width:1px;background-color:#888888;\">\n" );
	  	    buf.append("</div>");
	  	    
	  	    buf.append( "<div style=\"position: absolute; left: " + ( (i*300) + 40 ) + ";top:140px;\">\n" );
	  	    buf.append( "<h3>"+ columnHeaders[i] +"</h3>" );
	  	    buf.append( "</div>" );
	  	    
	      }
	      
	      return buf.toString();
	    
	  }

	  // This method is called just after the handler using this
	  // formatter is closed
	  public String getTail(Handler h) {
		  
	    return "</body></html>"; 

	  }

}
