package com.rogers.ute.hhHistoryService.cassandra;

import com.datastax.driver.core.Session;

public interface CassandraSessionProvider {

    Session getSession();
}
