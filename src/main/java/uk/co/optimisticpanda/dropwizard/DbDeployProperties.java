package uk.co.optimisticpanda.dropwizard;

import static com.dbdeploy.database.DelimiterType.normal;
import static com.dbdeploy.database.LineEnding.platform;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.base.Enums.getIfPresent;
import static com.google.common.base.Optional.fromNullable;

import java.io.File;
import java.nio.charset.Charset;

import com.dbdeploy.database.DelimiterType;
import com.dbdeploy.database.LineEnding;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;

public class DbDeployProperties {

    private String changelogTableName;  
    private String dbms;  
    private String delimiter;  
    private String delimiterType;  
    private String encoding;  
    private Long lastChangeToApply;  
    private String lineEnding;  
    private String scriptLocation;  
    private String outputFile;  
    private String templatesLocation;  
    private String undoOutputDirectory;  
    
    public String getChangelogTableName() {
        return fromNullable(changelogTableName).or("changelog");
    }
    public String getDbms() {
        return fromNullable(dbms).orNull();
    }
    public String getDelimiter() {
        return fromNullable(delimiter).or(";");
    }
    
    public DelimiterType getDelimiterType() {
        return getOptional(DelimiterType.class, delimiterType).or(normal);
    }

    public Charset getEncoding() {
        return Charset.forName(fromNullable(encoding).or(UTF_8.name()));
    }
 
    public Long getLastChangeToApply() {
        return fromNullable(lastChangeToApply).or(Long.MAX_VALUE);
    }

    public LineEnding getLineEnding() {
        return getOptional(LineEnding.class, lineEnding).or(platform);
    }

    public String getScriptLocation() {
        return fromNullable(scriptLocation).orNull();
    }

    public File getOutputFile() {
        return outputFile == null ? null : new File(outputFile);
    }

    public String getTemplatesLocation() {
        return fromNullable(templatesLocation).or("/");
    }
    
    public File getUndoOutputDirectory() {
        return undoOutputDirectory == null ? null : new File(undoOutputDirectory);
    }
    
    private <D extends Enum<D>> Optional<D> getOptional(Class<D> clazz, String value){
        return value == null ? Optional.<D>absent() : getIfPresent(clazz, value);
    }
    
    @VisibleForTesting
    void setChangelogTableName(String changelogTableName) {
        this.changelogTableName = changelogTableName;
    }
    
    @VisibleForTesting
    void setDbms(String dbms) {
        this.dbms = dbms;
    }
    
    @VisibleForTesting
    void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    @VisibleForTesting
    void setDelimiterType(String delimiterType) {
        this.delimiterType = delimiterType;
    }
    
    @VisibleForTesting
    void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    @VisibleForTesting
    void setLastChangeToApply(Long lastChangeToApply) {
        this.lastChangeToApply = lastChangeToApply;
    }
    
    @VisibleForTesting
    void setLineEnding(String lineEnding) {
        this.lineEnding = lineEnding;
    }
    
    @VisibleForTesting
    void setScriptLocation(String scriptLocation) {
        this.scriptLocation = scriptLocation;
    }
    
    @VisibleForTesting
    void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
    
    @VisibleForTesting
    void setTemplateLocation(String templateLocation) {
        this.templatesLocation = templateLocation;
    }
    
    @VisibleForTesting
    void setUndoOutputDirectory(String undoOutputDirectory) {
        this.undoOutputDirectory = undoOutputDirectory;
    }
    
}
