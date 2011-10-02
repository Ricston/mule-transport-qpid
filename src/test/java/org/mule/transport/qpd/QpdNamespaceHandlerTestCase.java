/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.tck.FunctionalTestCase;

public class QpdNamespaceHandlerTestCase extends FunctionalTestCase
{
    protected String getConfigResources()
    {
        return "qpd-namespace-config.xml";
    }

    public void testQpdConfig() throws Exception
    {
        QpdConnector c = (QpdConnector) muleContext.getRegistry().lookupConnector("qpdConnector");
        assertNotNull(c);
        assertTrue(c.isConnected());
        assertTrue(c.isStarted());

        //TODO Assert specific properties are configured correctly
    }
}
