package uk.co.optimisticpanda.dropwizard;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import uk.co.optimisticpanda.dropwizard.commands.MainDbDeployCommand;
import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathDbDeploy;

import com.dbdeploy.database.LineEnding;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.yammer.dropwizard.cli.Command;
import com.yammer.dropwizard.config.Bootstrap;

public class DbDeployBundleTest {

    @Test
    public void checkAppWithBundleHasDbdeployCommand() throws Exception {
        TestService service = new TestService();
        Bootstrap<TestConfiguration> bootstrap = new Bootstrap<TestConfiguration>(service);
        service.initialize(bootstrap);
        ImmutableList<Command> commands = bootstrap.getCommands();

        assertEquals(1, commands.size());
        assertEquals(MainDbDeployCommand.class, commands.get(0).getClass());
    }

    @Test
    public void checkDbDeployIsPopulatedCorrectly() throws Exception {
        TestService service = new TestService();

        Bootstrap<TestConfiguration> bootstrap = new Bootstrap<TestConfiguration>(service);
        service.initialize(bootstrap);
        ImmutableList<Command> commands = bootstrap.getCommands();

        MainDbDeployCommand<?> command = (MainDbDeployCommand<?>) commands.get(0);

        DbDeployDatabaseConfiguration config = new DbDeployDatabaseConfiguration();
        config.setUrl("test url");
        config.setUser("test user");
        config.setDriverClass("driver class");
        config.setPassword("password");

        DbDeployProperties props = new DbDeployProperties();
        props.setDbms("mysql");
        props.setOutputFile("filea");
        props.setScriptLocation("fileb");
        props.setUndoOutputDirectory("filec");
        props.setTemplateLocation("filed");

        props.setChangelogTableName("changelog");
        props.setDelimiter(";");
        props.setDelimiterType("normal");
        props.setEncoding("UTF-8");
        props.setLastChangeToApply(Long.MAX_VALUE);
        props.setLineEnding("platform");
        
        config.dbdeploy = props;

        ClasspathDbDeploy dbDeploy = command.getDbDeploy(config);
        assertEquals(config.getUrl(), dbDeploy.getUrl());
        assertEquals(config.getUser(), dbDeploy.getUserid());
        assertEquals(config.getDriverClass(), dbDeploy.getDriver());
        assertEquals(config.getPassword(), dbDeploy.getPassword());
        assertEquals("changelog", dbDeploy.getChangeLogTableName());
        assertEquals("mysql", dbDeploy.getDbms());
        assertEquals(";", dbDeploy.getDelimiter());
        assertEquals(Charsets.UTF_8, dbDeploy.getEncoding());
        assertEquals(Long.valueOf(Long.MAX_VALUE), dbDeploy.getLastChangeToApply());
        assertEquals(LineEnding.platform, dbDeploy.getLineEnding());
    }

}
