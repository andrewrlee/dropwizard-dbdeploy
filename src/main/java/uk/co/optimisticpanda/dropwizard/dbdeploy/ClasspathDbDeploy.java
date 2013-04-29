package uk.co.optimisticpanda.dropwizard.dbdeploy;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.dbdeploy.ChangeScriptApplier;
import com.dbdeploy.Controller;
import com.dbdeploy.appliers.DirectToDbApplier;
import com.dbdeploy.database.DelimiterType;
import com.dbdeploy.database.LineEnding;
import com.dbdeploy.database.QueryStatementSplitter;
import com.dbdeploy.database.changelog.DatabaseSchemaVersionManager;
import com.dbdeploy.database.changelog.QueryExecuter;
import com.dbdeploy.exceptions.UsageException;
import com.dbdeploy.scripts.ChangeScript;
import com.dbdeploy.scripts.ChangeScriptRepository;
import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class ClasspathDbDeploy {
    private static Logger log = LoggerFactory.getLogger(ClasspathDbDeploy.class);

    private String url;
    private String userid;
    private String password;
    private Charset encoding = Charsets.UTF_8;
    private String scriptlocation;
    private File outputfile;
    private File undoOutputfile;
    private LineEnding lineEnding = LineEnding.platform;
    private String dbms;
    private Long lastChangeToApply = Long.MAX_VALUE;
    private String driver;
    private String changeLogTableName = "changelog";
    private String delimiter = ";";
    private DelimiterType delimiterType = DelimiterType.normal;
    private String templatesLocation;

    public void go() throws Exception {
        log.info(getWelcomeString());

        validate();
        checkForRequiredParameter(scriptlocation, "scriptlocation");

        Class.forName(driver);

        QueryExecuter queryExecuter = new QueryExecuter(url, userid, password);

        DatabaseSchemaVersionManager databaseSchemaVersionManager = new DatabaseSchemaVersionManager(queryExecuter, changeLogTableName);

        List<ChangeScript> changeScripts = new ClasspathResourceScanner(encoding).getChangeScriptsForLocation(scriptlocation);
        ChangeScriptRepository changeScriptRepository = new ChangeScriptRepository(changeScripts);

        ChangeScriptApplier doScriptApplier = getChangeScriptApplier(Optional.of(databaseSchemaVersionManager));

        ChangeScriptApplier undoScriptApplier = null;

        if (undoOutputfile != null) {
            undoScriptApplier = new ClasspathUndoTemplateBasedApplier(new PrintWriter(undoOutputfile), dbms, changeLogTableName, templatesLocation);
        }

        Controller controller = new Controller(changeScriptRepository, databaseSchemaVersionManager, doScriptApplier, undoScriptApplier);

        controller.processChangeScripts(lastChangeToApply);

        queryExecuter.close();
    }

    public void executeScript(String resourceLocation) throws Exception {
        log.info(getWelcomeString());
        validate();

        Class.forName(driver);

        ChangeScriptApplier scriptApplier = getChangeScriptApplier(Optional.<DatabaseSchemaVersionManager>absent());

        ChangeScript script = new ClasspathChangeScript(1L, resourceLocation, encoding);
        scriptApplier.apply(Lists.newArrayList(script));
        log.info("Completed execution of script: {}", resourceLocation);
    }

    private void validate() throws UsageException {
        checkForRequiredParameter(userid, "userid");
        checkForRequiredParameter(driver, "driver");
        checkForRequiredParameter(url, "url");
    }

    private void checkForRequiredParameter(String parameterValue, String parameterName) throws UsageException {
        if (parameterValue == null || parameterValue.length() == 0) {
            UsageException.throwForMissingRequiredValue(parameterName);
        }
    }

    protected ChangeScriptApplier getChangeScriptApplier(Optional<DatabaseSchemaVersionManager> databaseSchemaVersionManager) throws Exception {
        QueryExecuter queryExecuter = new QueryExecuter(url, userid, password);
        QueryStatementSplitter splitter = new QueryStatementSplitter();
        splitter.setDelimiter(delimiter);
        splitter.setDelimiterType(delimiterType);
        splitter.setOutputLineEnding(lineEnding);

        if (databaseSchemaVersionManager.isPresent()) {
            return new DirectToDbApplier(queryExecuter, databaseSchemaVersionManager.get(), splitter);
        }
        if (outputfile != null) {
            return new ClasspathTemplateBasedApplier(new PrintWriter(outputfile, encoding.toString()), dbms, changeLogTableName, templatesLocation);
        }
        return new ScriptApplier(queryExecuter, splitter);
    }

    public void setTemplateLocation(String templateLocation) {
        this.templatesLocation = templateLocation;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setDelimiterType(DelimiterType delimiterType) {
        this.delimiterType = delimiterType;
    }

    public String getWelcomeString() {
        String version = getClass().getPackage().getImplementationVersion();
        return "dbdeploy " + version;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScriptLocation(String scriptLocation) {
        this.scriptlocation = scriptLocation;
    }

    public void setOutputfile(File outputfile) {
        this.outputfile = outputfile;
    }

    public void setDbms(String dbms) {
        this.dbms = dbms;
    }

    public void setLastChangeToApply(Long lastChangeToApply) {
        this.lastChangeToApply = lastChangeToApply;
    }

    public void setUndoOutputfile(File undoOutputfile) {
        this.undoOutputfile = undoOutputfile;
    }

    public void setChangeLogTableName(String changeLogTableName) {
        this.changeLogTableName = changeLogTableName;
    }

    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    public void setLineEnding(LineEnding lineEnding) {
        this.lineEnding = lineEnding;
    }

    public String getUrl() {
        return url;
    }

    public String getUserid() {
        return userid;
    }

    public String getPassword() {
        return password;
    }

    public Charset getEncoding() {
        return encoding;
    }

    public String getScriptlocation() {
        return scriptlocation;
    }

    public File getOutputfile() {
        return outputfile;
    }

    public File getUndoOutputfile() {
        return undoOutputfile;
    }

    public LineEnding getLineEnding() {
        return lineEnding;
    }

    public String getDbms() {
        return dbms;
    }

    public Long getLastChangeToApply() {
        return lastChangeToApply;
    }

    public String getDriver() {
        return driver;
    }

    public String getChangeLogTableName() {
        return changeLogTableName;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public DelimiterType getDelimiterType() {
        return delimiterType;
    }

    public String getTemplatesLocation() {
        return templatesLocation;
    }

}
