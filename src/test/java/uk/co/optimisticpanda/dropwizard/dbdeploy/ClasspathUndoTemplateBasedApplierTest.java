package uk.co.optimisticpanda.dropwizard.dbdeploy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;

import org.junit.Test;

public class ClasspathUndoTemplateBasedApplierTest {
    
    @Test
    public void checkCanLoadTemplatesWithoutPrefix() throws IOException{
        ClasspathUndoTemplateBasedApplier applier = createApplier("templates/mysql");
        assertNotNull(applier.getTemplate("mysql_apply.ftl"));
        assertNotNull(applier.getTemplate("mysql_undo.ftl"));
    }
    
    @Test
    public void checkCanLoadTemplatesWithPrefix() throws IOException{
        ClasspathUndoTemplateBasedApplier applier = createApplier("/templates/mysql");
        assertNotNull(applier.getTemplate("mysql_apply.ftl"));
        assertNotNull(applier.getTemplate("mysql_undo.ftl"));
    }
    
    @Test(expected=FileNotFoundException.class)
    public void checkErrorsWhenNonExistingTemplate() throws IOException{
        ClasspathUndoTemplateBasedApplier applier = createApplier("/templates/mysql");
        assertNotNull(applier.getTemplate("nonexistent.ftl"));
    }
    
    @Test
    public void checkTemplateQualifier() throws IOException{
        ClasspathUndoTemplateBasedApplier applier = createApplier("/templates/mysql");
        assertEquals("undo", applier.getTemplateQualifier());
    }

    private ClasspathUndoTemplateBasedApplier createApplier(String templateLocation) throws IOException{
        StringWriter writer = new StringWriter();
        return new ClasspathUndoTemplateBasedApplier(writer, "mysql", "changelog", templateLocation);
    }
    
}