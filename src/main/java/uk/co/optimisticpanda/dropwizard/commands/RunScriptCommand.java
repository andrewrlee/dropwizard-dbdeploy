package uk.co.optimisticpanda.dropwizard.commands;

import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.co.optimisticpanda.dropwizard.DbDeployConfigurationStrategy;
import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;
import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathDbDeploy;

import com.yammer.dropwizard.config.Configuration;

public class RunScriptCommand<T extends Configuration> extends AbstractDbDeployCommand<T> {
    private static Logger log = LoggerFactory.getLogger(RunScriptCommand.class);

    protected RunScriptCommand(String name, String description, DbDeployConfigurationStrategy<T> strategy, Class<T> configurationClass) {
        super(name, description, strategy, configurationClass);
    }

    @Override
    protected void run(Namespace namespace, ClasspathDbDeploy dbdeploy, DbDeployDatabaseConfiguration config) throws Exception {
        String script = namespace.getString("script");
        log.info("Executing script located at [{}]", script);
        dbdeploy.executeScript(script);
    }

    @Override
    public void configure(Subparser subparser) {
        subparser.addArgument("--script","-s") //
        .dest("script").help("Specifies which script should be run. Based on the scripts map in the dropwizard config");
        super.configure(subparser);
    }

}
