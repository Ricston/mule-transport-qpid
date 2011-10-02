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

public class QpdPollingTopicMulticastingTest extends FunctionalTestCase
{
	@Override
	protected String getConfigResources() 
	{
		return "qpd-namespace-configPollingTopic-Multicasting.xml";
	}
	
	public void testMulticasting()
	{
		MuleClient client = null;
		MuleMessage reply1 = null;
		MuleMessage reply2 = null;
		
		try 
		{
			client = new MuleClient();
			client.dispatch("vm://inbound", "Testing multicasting...", null);
						
			reply1 = client.request("vm://outbound1", 2000);
			reply2 = client.request("vm://outbound2", 2000);
		} 
		catch (MuleException e) 
		{
			System.out.println("MuleException raised: " + e.getMessage());
		}
		
		assertNotNull(reply1);
		assertNotNull(reply2);
		assertEquals("Testing multicasting...", reply1.getPayload());
		assertEquals("Testing multicasting...", reply2.getPayload());
	}
}
