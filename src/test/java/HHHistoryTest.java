import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.rogers.ute.billingaccountsvc.backend.readbillingdetails.BillingDetailsService;
import com.rogers.ute.billingaccountsvc.cache.BillingAccountCacheDao;
import com.rogers.ute.billingaccountsvc.cache.impl.cassandra.BillingAccountCacheDaoCassandra;
import com.rogers.ute.billingaccountsvc.cache.impl.cassandra.CassandraConfig;
import com.rogers.ute.billingaccountsvc.cassandra.CassandraSessionProvider;
import com.rogers.ute.billingaccountsvc.services.BillingAccountServiceCachable;
import com.rogers.ute.billingaccountsvc.services.impl.BillingAccountServiceCachableImpl;
import com.rogers.ute.hhHistoryService.actors.frontend.CassandraReaderActor;
import com.rogers.ute.hhHistoryService.actors.frontend.HHHistoryServiceActor;
import com.rogers.ute.hhHistoryService.service.HHHistoryService;
import com.rogers.ute.hhHistoryService.service.impl.HHHistoryServiceImpl;
import com.rogers.ute.logging.ProcessingContext;
import play.libs.Json;

public class HHHistoryTest {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        CassandraSessionProvider sessionProvider = new cassandra.CassandraSessionProvider(null, "ute");
        ActorRef cassandraReaderActor = null;
        BillingDetailsService billingDetailsService = null;
        System.out.println("BillingAccountCacheDao");
        BillingAccountCacheDao billingAccountCacheDao = new BillingAccountCacheDaoCassandra(new CassandraConfig("ute", 5000),null,sessionProvider,system,null);
        System.out.println("BillingAccountCacheDao :: "+Json.toJson(billingAccountCacheDao));
        BillingAccountServiceCachable billingAccountService = new BillingAccountServiceCachableImpl(null,billingAccountCacheDao,billingDetailsService,null,10000);
        try {
            cassandraReaderActor = CassandraReaderActor.create(system, com.rogers.ute.hhHistoryService.cassandra.CassandraConfig.create(null), cassandra.CassandraSessionProvider.getSessionProvider().getSession(),billingAccountService);
        }catch(Exception e){}
        ActorRef hhHistoryServiceActor = HHHistoryServiceActor.create(system, cassandraReaderActor);
        HHHistoryService hhHistoryService = new HHHistoryServiceImpl(hhHistoryServiceActor);

        hhHistoryService.hhHistory(new ProcessingContext("", "", ""), null, "ctn6","ban6", System.currentTimeMillis()).whenComplete((d, t) -> {
            if (t == null) {
                //if(d.getException() == null) {
                    System.out.println("\nFinal Response : " + Json.toJson(d));
               // }
               // else{
                   // if(d.getException() instanceof HHException){
                   //     System.err.println("HHException : " + Json.toJson((HHException)d.getException()));
                   // }
               // }
            }
            else {
                System.err.println(t);
            }

//            system.shutdown();
        });


        System.out.println("End");
//        system.shutdown();
    }
}
