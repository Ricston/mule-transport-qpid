/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd.transformers;

import org.apache.qpid.jms.Message;
import org.mule.transformer.AbstractMessageAwareTransformer;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;

/**
 * <code>FulMessageToObject</code> TODO Document
 */
public class QpdMessageToObject extends AbstractMessageAwareTransformer
{

    /* For general guidelines on writing transports see
       http://mule.mulesource.org/display/MULE/Writing+Transports */

	public QpdMessageToObject()
    {
		
		
		/* Here you can set default types that the
		   transformer will accept at runtime.  Mule will then validate the
		   transformer at runtime. You can register one or more source
		   types. eg.
	
		   registerSourceType(XXX.class.getName());
		 */
	
		/* IMPLEMENTATION NOTE: It's good practice to set the expected return
		   type for this transformer here This helps Mule validate event flows
		   and Transformer chains
	
		   setReturnClass(YYY.class);
		 */
		
		registerSourceType(Message.class);
		
		setReturnClass(Object.class);
    }

	public Object transform(MuleMessage message, String outputEncoding) throws TransformerException
	{
		// Transform the payload of message here. See comments in {@link AbstractMessageAwareTransformer}

	    // Make sure you return a transformed object that matches the
	    // returnClass type
		Message result = (Message) message.getPayload();
		
		return result;
	}

}



