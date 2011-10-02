/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.DefaultMuleMessage;
import org.mule.transport.AbstractMessageAdapterTestCase;
import org.mule.api.transport.MessageAdapter;
import org.mule.api.MessagingException;
import org.mule.api.MuleMessage;

public class QpdMessageAdapterTestCase extends AbstractMessageAdapterTestCase
{
    public Object getValidMessage() throws Exception
    {
        // TODO Create a valid message for your transport
    	MuleMessage msg = new DefaultMuleMessage(new String("This is a test"));
    	
    	return msg;
    }

    public MessageAdapter createAdapter(Object payload) throws MessagingException
    {
        return new QpdMessageAdapter(payload);
    }

}