package uk.co.optimisticpanda.dropwizard;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathResourceScanner;

import com.dbdeploy.scripts.ChangeScript;
import com.google.common.base.Charsets;
import static uk.co.optimisticpanda.dropwizard.dbdeploy.ClasspathResourceScanner.*;

public class ClasspathResourceScannerTest{

    private ClasspathResourceScanner scanner;

    @Before
    public void setUp(){
        scanner = new ClasspathResourceScanner(Charsets.UTF_8);
    }
    
    @Test
    public void checkSimpleReadingFromExpandedClasspath(){
        Collection<String> resources =  scan("scripts/set0");
        assertEquals(3, resources.size());
        assertTrue(resources.contains("scripts/set0/001_create_table_aaa.sql"));
        assertTrue(resources.contains("scripts/set0/002_create_table_bbb.sql"));
        assertTrue(resources.contains("scripts/set0/003_create_table_ccc.sql"));
    }
    
    @Test
    public void getResource(){
        String script = load(Charsets.UTF_8, "scripts/set0/001_create_table_aaa.sql");
        assertTrue(script.contains("CREATE TABLE aaa("));
        assertTrue(script.contains("change_number BIGINT NOT NULL"));
    }

    @Test
    public void getFilenameForResource(){
        assertEquals("001_create_table_aaa.sql", getFileNameForResource("scripts/set0/001_create_table_aaa.sql"));
        assertEquals("Bundle.class", getFileNameForResource("com/yammer/dropwizard/Bundle.class"));
    }
    
    @Test
    public void checkRetrieveResources(){
        List<ChangeScript> scripts = scanner.getChangeScriptsForLocation("scripts/set0");
        assertEquals(3, scripts.size());
        
        assertEquals("scripts/set0/001_create_table_aaa.sql", scripts.get(0).getDescription());
        assertEquals("scripts/set0/002_create_table_bbb.sql", scripts.get(1).getDescription());
        assertEquals("scripts/set0/003_create_table_ccc.sql", scripts.get(2).getDescription());
    }
}

