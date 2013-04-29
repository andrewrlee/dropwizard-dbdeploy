package uk.co.optimisticpanda.dropwizard.commands;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.sourceforge.argparse4j.inf.Namespace;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.dropwizard.DbDeployDatabaseConfiguration;
import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathDbDeploy;

import com.yammer.dropwizard.config.Configuration;

public class RunScriptCommandTest {

    private RunScriptCommand<Configuration> runScriptCommand;
    private ClasspathDbDeploy dbdeploy;
    private Namespace namespace;
    private DbDeployDatabaseConfiguration config;

    @Before
    public void createCommand(){
        runScriptCommand = new RunScriptCommand<Configuration>("run", "desc", null, Configuration.class);
        dbdeploy = mock(ClasspathDbDeploy.class);
        namespace = mock(Namespace.class);
        config = mock(DbDeployDatabaseConfiguration.class);
        
    }
    
    @Test
    public void checkExecuteScriptDoesntApplyWhenItIsFound() throws Exception{
        when(namespace.getString("script")).thenReturn("scriptName");
        
        runScriptCommand.run(namespace, dbdeploy, config);

        verify(dbdeploy).executeScript("scriptName");
    }

}
