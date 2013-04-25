package uk.co.optimisticpanda.dropwizard.commands;

import java.util.SortedMap;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import uk.co.optimisticpanda.dropwizard.DbDeployConfigurationStrategy;
import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;
import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathDbDeploy;

import com.google.common.collect.Maps;
import com.yammer.dropwizard.config.Configuration;

public class MainDbDeployCommand<T extends Configuration> extends AbstractDbDeployCommand<T> {
    private static final String COMMAND_NAME_ATTR = "subcommand";
    private final SortedMap<String, AbstractDbDeployCommand<T>> subcommands = Maps.newTreeMap();

    public MainDbDeployCommand(DbDeployConfigurationStrategy<T> strategy, Class<T> configurationClass, String commandName) {
        super(commandName, "Run database migration tasks", strategy, configurationClass);
        addSubcommand(new UpgradeCommand<T>("upgrade", "Apply database upgrade", strategy, configurationClass));
        addSubcommand(new RunScriptCommand<T>("run", "Run a single script", strategy, configurationClass));
    }

    private void addSubcommand(AbstractDbDeployCommand<T> subcommand) {
        subcommands.put(subcommand.getName(), subcommand);
    }

    @Override
    public void configure(Subparser subparser) {
        for (AbstractDbDeployCommand<T> subcommand : subcommands.values()) {
            final Subparser cmdParser = subparser.addSubparsers().addParser(subcommand.getName()).setDefault(COMMAND_NAME_ATTR, subcommand.getName())
                    .description(subcommand.getDescription());
            subcommand.configure(cmdParser);
        }
    }

    @Override
    protected void run(Namespace namespace, ClasspathDbDeploy dbdeploy, DbDeployDatabaseConfiguration config) throws Exception {
        final AbstractDbDeployCommand<T> subcommand = subcommands.get(namespace.getString(COMMAND_NAME_ATTR));
        subcommand.run(namespace, dbdeploy, config);
    }
}
