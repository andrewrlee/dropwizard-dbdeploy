package uk.co.optimisticpanda.dropwizard;

import uk.co.optimisticpanda.dropwizard.DbDeployBundle;
import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class TestService extends Service<TestConfiguration> {
    @Override
    public void initialize(Bootstrap<TestConfiguration> bootstrap) {
        bootstrap.addBundle(new DbDeployBundle<TestConfiguration>() {
            public DbDeployDatabaseConfiguration getDatabaseConfiguration(TestConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
    }

    @Override
    public void run(TestConfiguration configuration, Environment environment) throws Exception {
      
    }

    public static void main(String[] args) throws Exception {
        new TestService().run(args);
    }
    
    public static void runTask(String... args) throws Exception {
        new TestService().run(args);
    }
    
    
}