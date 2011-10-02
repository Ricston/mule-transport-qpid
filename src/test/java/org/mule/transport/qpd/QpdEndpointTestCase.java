/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.api.endpoint.EndpointURI;
import org.mule.endpoint.MuleEndpointURI;
import org.mule.tck.AbstractMuleTestCase;

public class QpdEndpointTestCase extends AbstractMuleTestCase
{
	public void testValidEndpointURI() throws Exception
    {
        EndpointURI url = new MuleEndpointURI("qpd://direct://amq.direct//queue");
        assertEquals("qpd", url.getScheme());
        assertEquals(-1, url.getPort());
        assertEquals("direct", url.getHost());
        assertEquals(0, url.getParams().size());
    }
}
