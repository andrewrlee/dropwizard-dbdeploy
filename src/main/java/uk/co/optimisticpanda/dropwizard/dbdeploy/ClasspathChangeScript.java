package uk.co.optimisticpanda.dropwizard.dbdeploy;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.dbdeploy.exceptions.DbDeployException;
import com.dbdeploy.scripts.ChangeScript;
import com.google.common.base.Charsets;

public class ClasspathChangeScript extends ChangeScript {

    private final String location;
    private final Charset encoding;
    private static final String UNDO_MARKER = "--//@UNDO";

    public ClasspathChangeScript(long id) {
        this(id, "test");
    }

    public ClasspathChangeScript(long id, String description) {
        super(id,description);
        this.location = null;
        this.encoding = Charsets.UTF_8;
    }

    public ClasspathChangeScript(long id, String location, Charset encoding) {
        super(id,location);
        this.location = location;
        this.encoding = encoding;
    }

    @Override
    public File getFile() {
        throw new UnsupportedOperationException("Classpath resources don't neccessarily have files");
    }
    
    public String getContent() {
        return getChangeContents(false);
    }

    public String getUndoContent() {
        return getChangeContents(true);
    }

    private String getChangeContents(boolean onlyAfterUndoMarker) {
        try {
            StringBuilder content = new StringBuilder();
            boolean foundUndoMarker = false;
            BufferedReader reader = new BufferedReader(ClasspathResourceScanner.reader(encoding, location).getInput());

            try {
                for (;;) {
                    String str = reader.readLine();

                    if (str == null)
                        break;

                    if (str.trim().equals(UNDO_MARKER)) {
                        foundUndoMarker = true;
                        continue;
                    }

                    if (foundUndoMarker == onlyAfterUndoMarker) {
                        content.append(str);
                        content.append('\n');
                    }
                }
            } finally {
                reader.close();
            }

            return content.toString();
        } catch (IOException e) {
            throw new DbDeployException("Failed to read change script file", e);
        }
    }

}
