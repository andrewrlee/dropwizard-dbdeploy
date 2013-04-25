package uk.co.optimisticpanda.dropwizard;

import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbdeploy.ChangeScriptApplier;
import com.dbdeploy.database.QueryStatementSplitter;
import com.dbdeploy.database.changelog.QueryExecuter;
import com.dbdeploy.scripts.ChangeScript;
import com.google.common.base.Throwables;

public class ScriptApplier implements ChangeScriptApplier {

    private static Logger log = LoggerFactory.getLogger(ScriptApplier.class);
    private final QueryExecuter queryExecuter;
    private final QueryStatementSplitter splitter;

    public ScriptApplier(QueryExecuter queryExecuter, QueryStatementSplitter splitter) {
        this.queryExecuter = queryExecuter;
        this.splitter = splitter;
    }

    @Override
    public void apply(List<ChangeScript> changeScripts) {
        begin();

        for (ChangeScript changeScript : changeScripts) {
            String content = changeScript.getContent();
            log.info("Applying:\n" + content + "\n...");
            applyChangeScript(content);
        }

        commitTransaction();
    }

    public void apply(String sql) {
        begin();
        applyChangeScript(sql);
        commitTransaction();

    }

    public void begin() {
        try {
            queryExecuter.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void applyChangeScript(String sql) {
        List<String> statements = splitter.split(sql);

        for (int i = 0; i < statements.size(); i++) {
            String statement = statements.get(i);
            try {
                if (statements.size() > 1) {
                    log.info(" -> statement " + (i + 1) + " of " + statements.size() + "...");
                }
                queryExecuter.execute(statement);
            } catch (SQLException e) {
                throw Throwables.propagate(e);
            }
        }
    }

    protected void commitTransaction() {
        try {
            queryExecuter.commit();
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

}
