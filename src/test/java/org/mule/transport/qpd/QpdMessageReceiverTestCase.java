/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.transport.qpd;

import org.mule.api.endpoint.InboundEndpoint;
import org.mule.api.service.Service;
import org.mule.api.transport.MessageReceiver;
import org.mule.endpoint.DefaultInboundEndpoint;
import org.mule.endpoint.EndpointURIEndpointBuilder;
import org.mule.transport.AbstractMessageReceiverTestCase;

import com.mockobjects.dynamic.Mock;


public class QpdMessageReceiverTestCase extends AbstractMessageReceiverTestCase
{
    public MessageReceiver getMessageReceiver() throws Exception
    {
        Mock mockService = new Mock(Service.class);
        mockService.expectAndReturn("getResponseRouter", null);
        return new QpdMessageReceiver(endpoint.getConnector(), (Service) mockService.proxy(), endpoint);
    }

    public InboundEndpoint getEndpoint() throws Exception
    {
    	EndpointURIEndpointBuilder endpointBuilder = new EndpointURIEndpointBuilder("qpd://direct://amq.direct//queue", muleContext);
    	
    	return endpointBuilder.buildInboundEndpoint();
    }

}
