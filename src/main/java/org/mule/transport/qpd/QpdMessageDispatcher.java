/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.apache.qpid.jms.Message;
import org.apache.qpid.jms.MessageProducer;
import org.apache.qpid.jms.Session;
import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.endpoint.OutboundEndpoint;
import org.mule.transport.AbstractMessageDispatcher;

public class QpdMessageDispatcher extends AbstractMessageDispatcher
{	
	/* Properties */
	private QpdConnector connector;
	private Message message;
	
	 public QpdMessageDispatcher(OutboundEndpoint endpoint)
	 {
		 /* 
		  * Constructor with parameters that reference to a the specific 
		  * connector for this dispatcher. 
		  */
	     super(endpoint);
	     
	     // gets the connector initialized by the Connector
	     this.connector = (QpdConnector)endpoint.getConnector();
	     
	 }
	
	 public void doDispatch(MuleEvent event) throws Exception
	 {
	     /* This is invoked when the endpoint is asynchronous.  
	        It should invoke the transport but not return any
	        result.  If a result is returned it should be ignored, but if the
	        underlying transport does have a notion of asynchronous processing,
	        that should be invoked.  This method is executed in a different
	        thread to the request thread. */
		
	     if (connector.getConnection() == null)
	     {
	    	 throw new IllegalStateException("No Qpd Connection");
	     }
	     // calls the dispatchMessage in order to send a particular message
	     dispatchMessage(event, false);
	 }
	
	 private void dispatchMessage(MuleEvent event, boolean b) 
	 {
		 /* Sends the message */
		 
		 Session session = null;
	     MessageProducer producer = null;

	     try 
	     {
	    	 // initializes a session according to the connector's properties
	    	 session = connector.getSession();
 
			 producer = connector.getMessageProducer(event.getEndpoint().getEndpointURI());
			
			 // a Message is created containing the message to be transferred
			 message = (Message) session.createObjectMessage(event.getMessage().getPayloadAsString());
			 
			 producer.send(message);
			 
		 } 
	     catch (Exception e) 
	     {
			System.out.println("Exception in dispatchMessage: " + e.getMessage());
		}
	 }

	public MuleMessage doSend(MuleEvent event) throws Exception
	 {
		/* Sends the message */
		 
		 Session session = null;
	     MessageProducer producer = null;

	     try 
	     {
	    	 // initializes a session according to the connector's properties
	    	 session = connector.getSession();

			 producer = connector.getMessageProducer(event.getEndpoint().getEndpointURI());
			
			 // a Message is created containing the message to be transferred
			 message = (Message) session.createObjectMessage(event.getMessage().getPayloadAsString());
			 
			 producer.send(message);
			 
		 } 
	     catch (Exception e) 
	     {
			System.out.println("Exception in dispatchMessage: " + e.getMessage());
		 }
	     return null;
	 }
}
