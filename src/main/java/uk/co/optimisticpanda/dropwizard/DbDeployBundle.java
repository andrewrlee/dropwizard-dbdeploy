package uk.co.optimisticpanda.dropwizard;

import uk.co.optimisticpanda.dropwizard.commands.MainDbDeployCommand;

import com.yammer.dropwizard.Bundle;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.util.Generics;

public abstract class DbDeployBundle<T extends Configuration> implements Bundle, DbDeployConfigurationStrategy<T> {

    public String commandName;

    /**
     * Provides the name of the dbdeploy main command. Defaults to "db"
     */
    public DbDeployBundle(String commandName) {
        this.commandName = commandName;
    }

    public DbDeployBundle() {
        this.commandName = "db";
    }

    public void initialize(Bootstrap<?> bootstrap) {
        final Class<T> klass = Generics.getTypeParameter(getClass(), Configuration.class);
        bootstrap.addCommand(new MainDbDeployCommand<T>(this, klass, commandName));
    }

    @Override
    public void run(Environment environment) {
        // Do nothing
    }
}
