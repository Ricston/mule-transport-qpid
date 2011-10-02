/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import javax.jms.JMSException;

import org.apache.qpid.jms.MessageConsumer;
//import org.apache.qpid.jms.Session;
//import org.apache.qpid.transport.MessageAcceptMode;
//import org.apache.qpid.transport.MessageAcquireMode;
//import org.apache.qpid.transport.MessageCreditUnit;
//import org.apache.qpid.transport.MessageTransfer;
//import org.apache.qpid.transport.Option;
//import org.apache.qpid.transport.SessionException;
//import org.apache.qpid.transport.SessionListener;
import org.mule.DefaultMuleMessage;
import org.mule.transport.ConnectException;
import org.mule.transport.AbstractPollingMessageReceiver;
import org.mule.api.service.Service;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.transport.Connector;
import org.mule.api.transport.MessageAdapter;

/**
 * <code>QpdMessageReceiver</code> TODO document
 */
public class QpdPollingMessageReceiver extends AbstractPollingMessageReceiver
{
	/* Properties */
	protected QpdConnector connector;
	protected MessageConsumer consumer;

	public QpdPollingMessageReceiver(Connector connector, Service service,
			InboundEndpoint endpoint) throws CreateException 
	{
		/* Constructor with parameters passed to super class */
		super(connector, service, endpoint);
		this.connector = (QpdConnector) connector;
	}

	public void doConnect() throws ConnectException 
	{
		/* Calls method createConsumer */
		createConsumer();
	}

	private void createConsumer() 
	{
		/*
		 * Creates a consumer by calling the connector's method
		 * getMessageConsumer
		 */
		try 
		{
			consumer = connector.getMessageConsumer(endpoint.getEndpointURI());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in createConsumer: " + e.getMessage());
		}
	}

	public void doDisconnect() throws ConnectException 
	{
		/*
		 * Disconnects and tidies up any sources allocated using the doConnect()
		 * method. This method should return the MessageReceiver into a
		 * disconnected state so that it can be connected again using the
		 * doConnect() method.
		 */

		try 
		{
			closeConsumer();
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in doDisconnect: " + e.getMessage());
		}
	}

	private void closeConsumer() throws JMSException 
	{
		/* Closes the consumer */
		consumer.close();
		consumer = null;
	}

	public void poll() throws Exception 
	{
		/*
		 * Once the object has been read, it can be passed into Mule by first
		 * wrapping the object with the Message adapter for this transport and
		 * calling routeMessage.
		 */		
		Object message = consumer.receive();
		MessageAdapter adapter = connector.getMessageAdapter(message);
		routeMessage(new DefaultMuleMessage(adapter), endpoint.isSynchronous());
	}
}
