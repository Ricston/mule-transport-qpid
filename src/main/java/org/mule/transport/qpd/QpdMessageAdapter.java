/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.apache.qpid.jms.Message;
import org.mule.api.MessagingException;
import org.mule.transport.AbstractMessageAdapter;

/**
 * <code>QpdMessageAdapter</code> TODO document
 */
public class QpdMessageAdapter extends AbstractMessageAdapter
{
 
    /* For general guidelines on writing transports see
       http://mule.mulesource.org/display/MULE/Writing+Transports */

    /* The MessageAdapter is used to wrap an underlying
       message. It should store a copy of the underlying message as an
       instance variable. */
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6549130166853534812L;
	
	private Message qpdMessage;

	public QpdMessageAdapter(Object message) throws MessagingException
    {
        /* The constructor should determine that the
           message is of the correct type or throw an exception i.e.
        
        if (message instanceof byte[]) {
            this.message = (byte[]) message;
        } else {
            throw new MessageTypeNotSupportedException(message, getClass());
        }
        */
		
		super();
		qpdMessage = (Message) message;
		
    }

    public String getPayloadAsString(String encoding) throws Exception
    {
        // return the string representation of the wrapped message
        return qpdMessage.toString();
    }

    public byte[] getPayloadAsBytes() throws Exception
    {
        // return the byte[] representation of the wrapped message
        return qpdMessage.toString().getBytes();
    }

    public Object getPayload()
    {
        // return the actual wrapped message
        return qpdMessage;
    }

}
