package com.rogers.ute.hhHistoryService.cassandra;

import com.typesafe.config.Config;

public class CassandraConfig {

    private final String keyspace;
    private final long timeout;

    public static CassandraConfig create(Config config) {
        //return new CassandraConfig(config.getString("cassandra.keyspace"), //TODO: Enable
        //        config.getDuration("cassandra.timeout", TimeUnit.MILLISECONDS));
        return new CassandraConfig("ute", 5000);
    }

    public CassandraConfig(String keyspace, long timeout) {
        this.keyspace = keyspace;
        this.timeout = timeout;
    }

    public String getKeyspace() {
        return keyspace;
    }

    public long getTimeout() {
        return timeout;
    }
}