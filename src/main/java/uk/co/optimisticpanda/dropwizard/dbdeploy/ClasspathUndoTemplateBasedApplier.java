package uk.co.optimisticpanda.dropwizard.dbdeploy;

import java.io.IOException;
import java.io.Writer;

public class ClasspathUndoTemplateBasedApplier extends ClasspathTemplateBasedApplier{

    public ClasspathUndoTemplateBasedApplier(Writer writer, String syntax, String changeLogTableName, String templateLocation) throws IOException {
        super(writer, syntax, changeLogTableName, templateLocation);
    }

    @Override
    protected String getTemplateQualifier() {
        return "undo";
    }
}
