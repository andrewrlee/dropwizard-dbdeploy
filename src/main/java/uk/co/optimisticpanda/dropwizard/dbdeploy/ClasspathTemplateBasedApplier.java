package uk.co.optimisticpanda.dropwizard.dbdeploy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dbdeploy.ChangeScriptApplier;
import com.dbdeploy.exceptions.UsageException;
import com.dbdeploy.scripts.ChangeScript;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class ClasspathTemplateBasedApplier implements ChangeScriptApplier {
    private Configuration configuration;
    private Writer writer;
    private String syntax;
    private String changeLogTableName;

    public ClasspathTemplateBasedApplier(Writer writer, String syntax, String changeLogTableName, String templateLocation) throws IOException {
        this.syntax = syntax;
        this.changeLogTableName = changeLogTableName;
        this.writer = writer;
        this.configuration = new Configuration();
        String classpathLocation = templateLocation.startsWith("/") ? templateLocation : "/" + templateLocation; 
        this.configuration.setTemplateLoader(new ClassTemplateLoader(getClass(), classpathLocation));
    }

    public Template getTemplate(String filename) throws IOException {
        return configuration.getTemplate(filename);
    }

    public void apply(List<ChangeScript> changeScripts) {
        String filename = syntax + "_" + getTemplateQualifier() + ".ftl";

        try {
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("scripts", changeScripts);
            model.put("changeLogTableName", changeLogTableName);

            try {
                Template template = getTemplate(filename);
                template.process(model, writer);
            } finally {
                writer.close();
            }
        } catch (FileNotFoundException ex) {
            throw new UsageException("Could not find template named " + filename + "\n" + "Check that you have got the name of the database syntax correct.", ex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected String getTemplateQualifier() {
        return "apply";
    }

}
