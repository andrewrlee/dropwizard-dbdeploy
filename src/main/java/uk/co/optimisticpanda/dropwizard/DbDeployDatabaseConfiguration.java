package uk.co.optimisticpanda.dropwizard;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.db.DatabaseConfiguration;

public class DbDeployDatabaseConfiguration extends DatabaseConfiguration {

    @JsonProperty
    DbDeployProperties dbdeploy;
    
    public DbDeployProperties getDbdeploy() {
        return dbdeploy;
    }
}
