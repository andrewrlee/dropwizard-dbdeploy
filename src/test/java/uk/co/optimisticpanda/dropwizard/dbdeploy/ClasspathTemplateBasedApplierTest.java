package uk.co.optimisticpanda.dropwizard.dbdeploy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;

import org.junit.Test;

import com.dbdeploy.scripts.ChangeScript;

public class ClasspathTemplateBasedApplierTest {

    @Test
    public void checkCanLoadTemplatesWithoutPrefix() throws IOException {
        ClasspathTemplateBasedApplier applier = createApplier("templates/mysql");
        assertNotNull(applier.getTemplate("mysql_apply.ftl"));
        assertNotNull(applier.getTemplate("mysql_undo.ftl"));
    }

    @Test
    public void checkCanLoadTemplatesWithPrefix() throws IOException {
        ClasspathTemplateBasedApplier applier = createApplier("/templates/mysql");
        assertNotNull(applier.getTemplate("mysql_apply.ftl"));
        assertNotNull(applier.getTemplate("mysql_undo.ftl"));
    }

    @Test
    public void checkDefaultTemplatesAreAccessible() throws IOException {
        ClasspathTemplateBasedApplier applier = createApplier("/");
        assertNotNull(applier.getTemplate("mysql_apply.ftl"));
        assertNotNull(applier.getTemplate("mysql_undo.ftl"));
    }

    @Test(expected = FileNotFoundException.class)
    public void checkErrorsWhenNonExistingTemplate() throws IOException {
        ClasspathTemplateBasedApplier applier = createApplier("/templates/mysql");
        assertNotNull(applier.getTemplate("nonexistent.ftl"));
    }

    @Test
    public void checkTemplateQualifier() throws IOException {
        ClasspathTemplateBasedApplier applier = createApplier("/templates/mysql");
        assertEquals("apply", applier.getTemplateQualifier());
    }

    @Test
    public void checkApplyMethod() throws IOException{
        ClasspathTemplateBasedApplier applier = createApplier("/templates/mysql");
        ChangeScript dummy = new ChangeScript(1L, "description"){
            @Override
            public String getContent() {
                return "";
            }};
        applier.apply(Arrays.asList(dummy));
    }

    private ClasspathTemplateBasedApplier createApplier(String templateLocation) throws IOException {
        StringWriter writer = new StringWriter();
        return new ClasspathTemplateBasedApplier(writer, "mysql", "changelog", templateLocation);
    }

}