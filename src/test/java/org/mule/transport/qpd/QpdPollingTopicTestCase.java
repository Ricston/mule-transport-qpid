/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.api.MuleException;
import org.mule.api.MuleMessage;
import org.mule.module.client.MuleClient;
import org.mule.tck.FunctionalTestCase;

public class QpdPollingTopicTestCase extends FunctionalTestCase
{

	@Override
	protected String getConfigResources() 
	{
		return "qpd-namespace-configPollingTopic.xml";
	}
	
	public void testTransport()
	{
		MuleClient client = null;
		MuleMessage reply = null;
		
		try 
		{
			client = new MuleClient();
			client.dispatch("vm://inbound", "Testing Topics...", null);
			reply = client.request("vm://outbound", 2000);
		} 
		catch (MuleException e) 
		{
			fail("Exception raised: " + e.getDetailedMessage());
		}
		
		assertNotNull(reply);
		assertNotNull(reply.getPayload());
		assertEquals("Testing Topics...", reply.getPayload());
	}

}
