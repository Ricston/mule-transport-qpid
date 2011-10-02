/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.api.endpoint.MalformedEndpointException;
import org.mule.endpoint.AbstractEndpointURIBuilder;

import java.net.URI;
import java.util.Properties;

/**
 * <code>FullEndpointURIBuilder</code> TODO Document
 */
public class QpdEndpointURIBuilder extends AbstractEndpointURIBuilder
{

    protected void setEndpoint(URI uri, Properties props) throws MalformedEndpointException
    {
        address = uri.toString().substring(6);
    }

}
