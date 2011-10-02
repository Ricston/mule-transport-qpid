/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.qpid.jms.MessageConsumer;
import org.apache.qpid.jms.Session;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleException;
import org.mule.api.endpoint.EndpointURI;
import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.lifecycle.CreateException;
import org.mule.api.service.Service;
import org.mule.api.transport.Connector;
import org.mule.api.transport.MessageAdapter;
import org.mule.transport.AbstractMessageReceiver;
import org.mule.transport.ConnectException;

/**
 * <code>QpdMessageReceiver</code> TODO document
 */
public class QpdMessageReceiver extends AbstractMessageReceiver implements MessageListener
{
	/* Properties */
	protected QpdConnector connector;
	protected MessageConsumer consumer;
	protected Session session;	
	protected boolean startOnConnect = false;

	public QpdMessageReceiver(Connector connector, Service service,
			InboundEndpoint endpoint) throws CreateException {
		/* Constructor with parameters passed to super class */
		super(connector, service, endpoint);
		this.connector = (QpdConnector) connector;
	}

	public void doConnect() throws ConnectException {
		/* Calls method createConsumer and afterwards doStart*/
		createConsumer();
		try 
		{
			if(startOnConnect)
				doStart();
		} 
		catch (MuleException e) 
		{
			System.out.println("MuleException in doConnect: " + e.getMessage());
		}
	}
	
	protected void doStart() throws MuleException
	{
		/*
		 * Sets the listener on the consumer 
		 * in order to listen for any messages
		 */
		try 
		{
			if(consumer == null)
			{
				startOnConnect = true;
			}
			else
			{
				startOnConnect = false;
				consumer.setMessageListener(this);
				
			}
		} 
		catch (JMSException e) 
		{
			System.out.println("JMSException in doStart: " + e.getMessage());
		}
	}
	
	protected void doStop() throws MuleException
	{
		try 
		{
			consumer.setMessageListener(null);
		} 
		catch (JMSException e) 
		{
			System.out.println("JMSException in doStop: " + e.getMessage());
		}
	}

	private void createConsumer() 
	{
		/* 
		 * Gets the endpointURI and passes it as a parameter
		 * in order to initialize the consumer
		 */
		
		EndpointURI bind = endpoint.getEndpointURI();
		
		consumer = connector.getMessageConsumer(bind);
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
		session = null;
	}
	
	@Override
	public void onMessage(Message message) 
	{
		/*
		 * When receiving the message it will be directed to this method.
		 * It will be routed to the endpoint.
		 */
		try 
		{
			MessageAdapter adapter = connector.getMessageAdapter(message);
			routeMessage(new DefaultMuleMessage(adapter), endpoint.isSynchronous());
		} 
		catch (Exception e) 
		{
			System.out.println("Exception in onMessage: " + e.getMessage());
		}
	}
}
