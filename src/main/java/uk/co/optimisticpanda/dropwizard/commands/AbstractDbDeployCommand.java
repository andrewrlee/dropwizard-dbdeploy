package uk.co.optimisticpanda.dropwizard.commands;

import net.sourceforge.argparse4j.inf.Namespace;
import uk.co.optimisticpanda.dropwizard.DbDeployConfigurationStrategy;
import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;
import uk.co.optimisticpanda.dropwizard.DbDeployProperties;
import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathDbDeploy;

import com.yammer.dropwizard.cli.ConfiguredCommand;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Configuration;

public abstract class AbstractDbDeployCommand<T extends Configuration> extends ConfiguredCommand<T> {
    private final DbDeployConfigurationStrategy<T> strategy;
    private final Class<T> configurationClass;

    protected AbstractDbDeployCommand(String name, String description, DbDeployConfigurationStrategy<T> strategy, Class<T> configurationClass) {
        super(name, description);
        this.strategy = strategy;
        this.configurationClass = configurationClass;
    }

    @Override
    protected Class<T> getConfigurationClass() {
        return configurationClass;
    }

    @Override
    protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
        DbDeployDatabaseConfiguration dbConfig = strategy.getDatabaseConfiguration(configuration);
        dbConfig.setMaxSize(1);
        dbConfig.setMinSize(1);

        ClasspathDbDeploy dbdeploy = getDbDeploy(dbConfig);

        run(namespace, dbdeploy, strategy.getDatabaseConfiguration(configuration));
    }

    public ClasspathDbDeploy getDbDeploy(DbDeployDatabaseConfiguration dbConfig) {
        
        ClasspathDbDeploy dbdeploy = new ClasspathDbDeploy();
        dbdeploy.setDriver(dbConfig.getDriverClass());
        dbdeploy.setUrl(dbConfig.getUrl());
        dbdeploy.setPassword(dbConfig.getPassword());
        dbdeploy.setUserid(dbConfig.getUser());

        DbDeployProperties dbdeployProps = dbConfig.getDbdeploy() != null ? dbConfig.getDbdeploy() : new DbDeployProperties();
        dbdeploy.setChangeLogTableName(dbdeployProps.getChangelogTableName());
        dbdeploy.setDbms(dbdeployProps.getDbms());
        dbdeploy.setDelimiter(dbdeployProps.getDelimiter());
        dbdeploy.setDelimiterType(dbdeployProps.getDelimiterType());
        dbdeploy.setEncoding(dbdeployProps.getEncoding());
        dbdeploy.setLastChangeToApply(dbdeployProps.getLastChangeToApply());
        dbdeploy.setLineEnding(dbdeployProps.getLineEnding());
        dbdeploy.setScriptLocation(dbdeployProps.getScriptLocation());
        dbdeploy.setOutputfile(dbdeployProps.getOutputFile());
        dbdeploy.setTemplateLocation(dbdeployProps.getTemplatesLocation());
        dbdeploy.setUndoOutputfile(dbdeployProps.getUndoOutputFile());

        return dbdeploy;
    }

    protected abstract void run(Namespace namespace, ClasspathDbDeploy dbdeploy, DbDeployDatabaseConfiguration config) throws Exception;

}
