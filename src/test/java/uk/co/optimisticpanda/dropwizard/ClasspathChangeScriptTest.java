package uk.co.optimisticpanda.dropwizard;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathChangeScript;

import com.google.common.base.Charsets;

public class ClasspathChangeScriptTest {

    @Test
    public void checkCanGetContents(){
        ClasspathChangeScript script = new ClasspathChangeScript(3L, "scripts/set0/001_create_table_aaa.sql", Charsets.UTF_8);
        
        String content = script.getContent();
        assertTrue(content.contains("CREATE TABLE aaa("));
        assertTrue(content.contains("change_number BIGINT NOT NULL"));
    }
    
}
