package uk.co.optimisticpanda.dropwizard.commands;

import net.sourceforge.argparse4j.inf.Namespace;
import uk.co.optimisticpanda.dropwizard.DbDeployConfigurationStrategy;
import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;
import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathDbDeploy;

import com.yammer.dropwizard.config.Configuration;

public class UpgradeCommand<T extends Configuration> extends AbstractDbDeployCommand<T>{

    protected UpgradeCommand(String name, String description, DbDeployConfigurationStrategy<T> strategy, Class<T> configurationClass) {
        super(name, description, strategy, configurationClass);
    }

    @Override
    protected void run(Namespace namespace, ClasspathDbDeploy dbdeploy, DbDeployDatabaseConfiguration config) throws Exception {
        dbdeploy.go();
    }

}
