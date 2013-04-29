package uk.co.optimisticpanda.dropwizard;

import static uk.co.optimisticpanda.dropwizard.TestService.runTask;
import uk.org.lidalia.sysoutslf4j.context.LogLevel;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

public class Runner {

    public static void main(String[] args) throws Exception {
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J(LogLevel.INFO,LogLevel.INFO);
        runTask("db", "run", "-s", "scripts/other/drop.sql", "src/test/resources/test.create.yaml");
        runTask("db", "run", "--script", "scripts/changelog/createSchemaVersionTable.mysql.sql", "src/test/resources/test.yaml");
        runTask("db", "upgrade", "src/test/resources/test.yaml");
        runTask("db", "upgrade", "src/test/resources/test.yaml");
    }
    
}
