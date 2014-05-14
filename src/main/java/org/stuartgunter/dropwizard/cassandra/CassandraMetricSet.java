/*
 * Copyright 2014 Stuart Gunter
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.stuartgunter.dropwizard.cassandra;

import com.codahale.metrics.Metric;
import com.codahale.metrics.MetricSet;
import com.datastax.driver.core.Cluster;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Exposes the metrics provided by the provided Cluster, embedding the cluster name in the metric name.
 */
public class CassandraMetricSet implements MetricSet {

    private Map<String, Metric> metrics;

    public CassandraMetricSet(Cluster cluster) {
        final String clusterName = cluster.getClusterName();
        Map<String, Metric> driverMetrics = cluster.getMetrics().getRegistry().getMetrics();
        ImmutableMap.Builder<String, Metric> builder = ImmutableMap.builder();
        for (Map.Entry<String, Metric> metricEntry : driverMetrics.entrySet()) {
            builder.put(name(Cluster.class, clusterName, metricEntry.getKey()), metricEntry.getValue());
        }
        metrics = builder.build();
    }

    @Override
    public Map<String, Metric> getMetrics() {
        return metrics;
    }
}
