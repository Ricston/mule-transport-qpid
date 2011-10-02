/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import java.net.URISyntaxException;
import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.qpid.client.AMQAnyDestination;
import org.apache.qpid.jms.Connection;
import org.apache.qpid.jms.MessageConsumer;
import org.apache.qpid.jms.MessageProducer;
import org.apache.qpid.jms.Session;
import org.apache.qpid.url.AMQBindingURL;
import org.apache.qpid.url.BindingURL;
import org.mule.transport.AbstractConnector;
import org.mule.api.endpoint.EndpointURI;
import org.mule.api.lifecycle.InitialisationException;

/**
 * <code>QpdConnector</code> TODO document
 */
public class QpdConnector extends AbstractConnector
{
    /* Properties */
    public static final String QPD = "qpd";
    
    private Connection connection = null;
    
    private Context context = null;
    
    private ConnectionFactory connectionFactory = null;
    
    private Session session = null;
    
    private String initialFactory = "org.apache.qpid.jndi.PropertiesFileInitialContextFactory";

	private String connFactory = "amqp://guest:guest@clientid/test?brokerlist='tcp://localhost:5672'";
    
    private boolean topic = false;

    /* All configuration for the transport should be set
       on the Connector object, this is the object that gets configured in
       MuleXml */

    public void doInitialise() throws InitialisationException
    {
        /* Is called once all bean properties have been
           set on the connector and can be used to validate and initialize the
           connectors state. */
    	try 
    	{
    		// calls method createConnectionFactory in 
    		// order to initialize the connectionFactory
			connectionFactory = createConnectionFactory();
		} 
    	catch (Exception e) 
    	{  
			System.out.println("Exception in doInitialises: " + e.getMessage());
		}	
    }
    
    protected ConnectionFactory createConnectionFactory() throws Exception
    {
    	/* Creates a ConnectionFactory on the context 
    	 * Initialized in the initContext
    	 */
    	// if connectionFactory has already been initialized then it is returned
        if (connectionFactory != null)
        {
            return connectionFactory;
        }
        // else a context is initialized by calling initContext method
        // a connectionFactory is created, initialized and returned
        else
        {
        	this.initContext();
        	
        	ConnectionFactory temp = (ConnectionFactory) context.lookup("qpidConnectionfactory");
            	
            return (ConnectionFactory)temp;  	
        }
    }
    
    protected void initContext() throws NamingException
    {
    	/* Initializes the context using the properties 
    	 * defined in the config.
    	 */
    	Properties properties = new Properties();

    	// properties contains the properties that will be used 
		// everytime the connection is required to transfer messages
			
    	properties.setProperty("java.naming.factory.initial", initialFactory);
    	properties.setProperty("connectionfactory.qpidConnectionfactory", connFactory);
    	
    	if(topic)
    	{
    		properties.setProperty("destination.topicExchange", "amq.topic");
    	}
    	else
    	{
    		properties.setProperty("destination.topicExchange", "amq.direct");
    	}		
    	context = new InitialContext(properties);
    }
    
    protected Connection createConnection()
    {
    	/* Creates a Connection and returns it */
    	ConnectionFactory cf = connectionFactory;
    	Connection connection = null;
    	
    	try
    	{
    		// a connection is created by calling the createConnection
    		// method found in the ConnectionFactory class
    		connection = (Connection) cf.createConnection();
    	}
    	catch (Exception e)
    	{
    		System.out.println("Exception in createConnection: " + e.getMessage());
    	}
    	return connection;
    }

    public void doConnect() throws Exception
    {
        /* Makes a connection to the underlying resource. 
         * When connections are managed at the receiver/dispatcher
           level, this method may do nothing */    	
    	
    	connection = createConnection();
    	
    	connection.start();
    	
    }

    public void doDisconnect() throws Exception
    {
        /* Disconnects any connections made in the
           connect method If the connect method did not do anything then this
           method shouldn't do anything either. */
    	
    	 try
         {
             if (connection != null)
             {
                 connection.close();
             }
         }
         finally
         {
             connection = null;
         }
    }

    public void doStart()
    {
        /* If there is a single server instance or
           connection associated with the connector i.e.  AxisServer or a Jms
           Connection or Jdbc Connection, this method should put the resource
           in a started state here. */
    	
        if (connection != null)
        {
            try
            {
                connection.start();
            }
            catch (Exception e)
            {
            	System.out.println("Exception in doStart: " + e.getMessage());
            }
        }
    }

    public void doStop() 
    {
        /* Should put any associated resources into a
           stopped state. Mule will automatically call the stop() method. */
    	
    	if (connection != null)
        {
            try
            {
                connection.stop();
            }
            catch (Exception e)
            {
                System.out.println("Exception in doStop: " + e.getMessage());
            }
        }
    }
    
    public void doDispose()
    {
    	/* Should clean up any open resources associated
           with the connector. */
    	
    	if(connection != null)
    	{
    		try
    		{
    			connection.close();
    		}
    		catch (Exception e)
    		{
    			System.out.println("Exception in doDispose: " + e.getMessage());
    		}
    		finally
    		{
    			connection = null;
    		}
    		
    		if (context != null)
    		{
    			try 
    			{
    				context.close();
    			}
    			catch (NamingException ne)
    			{
    				System.out.println("NamingException in doDispose: " + ne.getMessage());
    			}
    			finally
    			{
    				context = null;
    			}
    		}
    	}
    }
    
    /* Getters */
    public Connection getConnection()
    {
    	return connection;
    }
    
    public Context getContext()
    {
    	return context;
    }
    
    public Session getSession()
    {
    	try 
    	{
    		// a session is initialized by calling the 
    		// createSession method found in the Connection class
			session = (Session) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} 
    	catch (JMSException e) {
			System.out.println("JMSException in getSession: " + e.getMessage());
		}
    	return session;
    }
       
    public Destination getDestination(EndpointURI bind)
    {  
    	/* Return a destination according to the EndpointURI
    	 * passed as a parameter. It contains the address
    	 * and whether the exchange is a topic or queue
    	 */
    	String address = bind.getUri().toString();    	
    	StringBuffer temp = new StringBuffer();
    	temp.append(address);
    
    	temp.delete(0, 6);
    	
    	address = temp.toString();	
    	
    	AMQBindingURL binding = null;
		try 
		{
			binding = new AMQBindingURL(address);
			
			if(isTopic())
			{
				binding.setQueueName(getQueueName(bind.toString()));
			}
		} 
		catch (URISyntaxException e) 
		{
			System.out.println("URISyntaxException in getDestination: " + e.getMessage());
		}
    	
		//binding.setOption(BindingURL.OPTION_AUTODELETE, "true");
    	Destination destination = new AMQAnyDestination(binding);
    	
    	return destination;
    }
    
    public MessageProducer getMessageProducer(EndpointURI bind)
    {
    	/*
    	 * Creates a producer and returns it
    	 */
    	Session ssn = this.getSession(); 
    	MessageProducer producer = null;
    	
		try 
		{
			producer = (MessageProducer) ssn.createProducer(this.getDestination(bind));
		} 
		catch (JMSException e) 
		{
			System.out.println("JMSException in getMessageProducer: " + e.getMessage());
		}
    	
    	return producer;
    }
    
    public MessageConsumer getMessageConsumer(EndpointURI bind)
    {
    	/*
    	 * Creates a Consumer and returns it
    	 */
    	Session ssn = this.getSession();    	
    	Destination dest = this.getDestination(bind);    	
    	MessageConsumer consumer = null;
    	
    	try 
    	{
			consumer = (MessageConsumer) ssn.createConsumer(dest);
		} 
    	catch (JMSException e) 
    	{
			System.out.println("JMSException in getMessageConsumer: " + e.getMessage());
		}
    	return consumer;
    }
    
    public String getInitialFactory() 
    {
		return initialFactory;
	}

	public void setInitialFactory(String initialFactory) 
	{
		this.initialFactory = initialFactory;
	}

	public String getConnFactory() 
	{
		return connFactory;
	}

	public void setConnFactory(String connFactory) 
	{
		this.connFactory = connFactory;
	}

	public boolean isTopic() 
	{
		return topic;
	}

	public void setTopic(boolean topic) 
	{
		this.topic = topic;
	}
	
	public String getQueueName(String uri)
	{
		/*
		 * Returns the queue name as defined from the config
		 */
		String name = null;
		int counter = 0;
		
		while(counter<3)
		{
			for(int x=0; x<uri.length(); x++)
			{
				if(uri.charAt(x)=='/' && uri.charAt(x+1)=='/')
				{
					if(counter==2)
					{
						name = uri.substring((x+2), uri.indexOf('?'));
						x=uri.length();
						counter=3;
					}
					else
					{
						counter++;
					}
				}
			}
		}
		return name;
	}

    public String getProtocol()
    {
        return QPD;
    }

}
