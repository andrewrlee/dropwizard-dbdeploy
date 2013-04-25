package uk.co.optimisticpanda.dropwizard;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class TestConfiguration extends Configuration {
    @Valid
    @NotNull
    @JsonProperty
    private DbDeployDatabaseConfiguration database = new DbDeployDatabaseConfiguration();

    public DbDeployDatabaseConfiguration getDatabaseConfiguration() {
        return database;
    }
}