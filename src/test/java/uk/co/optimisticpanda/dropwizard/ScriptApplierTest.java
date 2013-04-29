package uk.co.optimisticpanda.dropwizard;

import static com.google.common.collect.Lists.newArrayList;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import uk.co.optimisticpanda.dropwizard.dbdeploy.ScriptApplier;

import com.dbdeploy.database.QueryStatementSplitter;
import com.dbdeploy.database.changelog.QueryExecuter;
import com.dbdeploy.scripts.ChangeScript;
import com.google.common.collect.Lists;

public class ScriptApplierTest {

    private QueryExecuter queryExecuter;
    private QueryStatementSplitter splitter;
    private ScriptApplier scriptApplier;
    private ChangeScript changeScript;

    @Before
    public void setUpScriptApplier(){
        queryExecuter = mock(QueryExecuter.class);
        splitter = mock(QueryStatementSplitter.class);
        changeScript = mock(ChangeScript.class);
        scriptApplier = new ScriptApplier(queryExecuter, splitter);
    }
    
    @Test
    public void checkApply() throws SQLException{

        when(splitter.split("")).thenReturn(newArrayList("statement1", "statement2"));
        
        scriptApplier.apply("");
        
        InOrder order = inOrder(queryExecuter, splitter);
        order.verify(queryExecuter).setAutoCommit(false);
        order.verify(queryExecuter).execute("statement1");
        order.verify(queryExecuter).execute("statement2");
        order.verify(queryExecuter).commit();
    }
    
    @Test
    public void checkListApply() throws SQLException{
        String content = "statement1, statement2";
        when(splitter.split(content)).thenReturn(newArrayList("statement1", "statement2"));
        when(changeScript.getContent()).thenReturn(content);
        
        scriptApplier.apply(Lists.newArrayList(changeScript));
        
        InOrder order = inOrder(queryExecuter, splitter, changeScript);
        order.verify(queryExecuter).setAutoCommit(false);
        order.verify(changeScript).getContent();
        order.verify(queryExecuter).execute("statement1");
        order.verify(queryExecuter).execute("statement2");
        order.verify(queryExecuter).commit();
    }
    
    
    
}
