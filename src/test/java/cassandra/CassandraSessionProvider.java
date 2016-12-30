package cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.rogers.ute.logging.LoggerWrapper;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class CassandraSessionProvider implements com.rogers.ute.billingaccountsvc.cassandra.CassandraSessionProvider{

    private static final LoggerWrapper logger = LoggerWrapper.slf4jLogger(CassandraSessionProvider.class);
    private final Cluster cluster;
    private final String keyspace;

    //TODO: make sessionOptional plain (not Optional) Will break the backward compatibility
    private Optional<Session> sessionOptional = Optional.empty();
    private final Object sessionInitializationSyncObject = new Object();

    public CassandraSessionProvider(Cluster cluster, String keyspace) {
        this.cluster = cluster;
        this.keyspace = keyspace;
    }

    /**
     * does double check before and after synchronized block to avoid blocking calls
     *
     * @return
     */
    @Override
    public Session getSession() {
        return sessionOptional.orElseGet(() -> {
            synchronized (sessionInitializationSyncObject) {
                return sessionOptional.orElseGet(() -> {
                    logger.info("New cassandra session created");
                    Session session = getCassandraCluster().connect(keyspace);
                    sessionOptional = Optional.of(session);
                    return session;
                });
            }
        });
    }


    private static Cluster getCassandraCluster() {
        //List<String> cassandraContactPoints = appConfig.getConfig().getStringList("persistence.cassandra.endpoints");
        Cluster.Builder builder = Cluster.builder();
//        for (String cassandraContactPoint : cassandraContactPoints) {
//            builder.addContactPoint(cassandraContactPoint);
//        }
        System.out.println("Cluster creation");
        builder.addContactPoint("10.96.90.191");
        System.out.println("Cluster Created");

//        Optional<String> dbUserOptional = appConfig.getSecretKeyOptional("cassandra.user");
//        Optional<String> dbPasswordOptional = appConfig.getSecretKeyOptional("cassandra.password");
//        dbUserOptional.ifPresent(dbUser ->
//                dbPasswordOptional.ifPresent(
//                        dbPassword -> builder.withCredentials(dbUser, dbPassword)
//                )
//        );

        //SSLUtil.createCassandraSSLOptions(appConfig).ifPresent(sslOptions -> builder.withSSL(sslOptions));

        return builder.build();
    }

    public static CassandraSessionProvider getSessionProvider() {
        return new CassandraSessionProvider(getCassandraCluster(), "ute");
    }


}
