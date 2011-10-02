/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.api.endpoint.InboundEndpoint;
import org.mule.tck.AbstractMuleTestCase;

public class QpdConnectorFactoryTestCase extends AbstractMuleTestCase
{
    public void testCreateFromFactory() throws Exception
    {
        InboundEndpoint endpoint = muleContext.getRegistry()
                .lookupEndpointFactory().getInboundEndpoint(getEndpointURI());
        assertNotNull(endpoint);
        assertNotNull(endpoint.getConnector());
        assertTrue(endpoint.getConnector() instanceof QpdConnector);
        System.out.println(endpoint.getEndpointURI().getAddress().toString());
        assertEquals(getEndpointURI(), endpoint.getEndpointURI().toString());
        
    }

    public String getEndpointURI() 
    {
    	return new String("qpd://direct://amq.direct//queue");
    }

}
