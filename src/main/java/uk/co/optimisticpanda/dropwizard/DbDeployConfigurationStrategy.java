package uk.co.optimisticpanda.dropwizard;

import com.yammer.dropwizard.config.Configuration;

public interface DbDeployConfigurationStrategy<T extends Configuration> {

    DbDeployDatabaseConfiguration getDatabaseConfiguration(T configuration);
    
}
