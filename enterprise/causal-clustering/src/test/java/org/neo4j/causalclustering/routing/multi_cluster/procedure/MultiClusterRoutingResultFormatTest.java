/*
 * Copyright (c) 2002-2020 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j Enterprise Edition. The included source
 * code can be redistributed and/or modified under the terms of the
 * GNU AFFERO GENERAL PUBLIC LICENSE Version 3
 * (http://www.fsf.org/licensing/licenses/agpl-3.0.html) with the
 * Commons Clause, as found in the associated LICENSE.txt file.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * Neo4j object code can be licensed independently from the source
 * under separate terms from the AGPL. Inquiries can be directed to:
 * licensing@neo4j.com
 *
 * More information is also available at:
 * https://neo4j.com/licensing/
 */
package org.neo4j.causalclustering.routing.multi_cluster.procedure;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.neo4j.causalclustering.routing.Endpoint;
import org.neo4j.causalclustering.routing.multi_cluster.MultiClusterRoutingResult;
import org.neo4j.helpers.AdvertisedSocketAddress;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class MultiClusterRoutingResultFormatTest
{

    @Test
    public void shouldSerializeToAndFromRecordFormat()
    {
        List<Endpoint> fooRouters = asList(
                Endpoint.route( new AdvertisedSocketAddress( "host1", 1 ) ),
                Endpoint.route( new AdvertisedSocketAddress( "host2", 1 ) ),
                Endpoint.route( new AdvertisedSocketAddress( "host3", 1 ) )
        );

        List<Endpoint> barRouters = asList(
                Endpoint.route( new AdvertisedSocketAddress( "host4", 1 ) ),
                Endpoint.route( new AdvertisedSocketAddress( "host5", 1 ) ),
                Endpoint.route( new AdvertisedSocketAddress( "host6", 1 ) )
        );

        Map<String,List<Endpoint>> routers = new HashMap<>();
        routers.put( "foo", fooRouters );
        routers.put( "bar", barRouters );

        long ttlSeconds = 5;
        MultiClusterRoutingResult original = new MultiClusterRoutingResult( routers, ttlSeconds * 1000 );

        Object[] record = MultiClusterRoutingResultFormat.build( original );

        MultiClusterRoutingResult parsed = MultiClusterRoutingResultFormat.parse( record );

        assertEquals( original, parsed );
    }
}
