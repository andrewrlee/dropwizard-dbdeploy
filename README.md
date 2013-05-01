dropwizard-dbdeploy
===================

Embedded dbdeploy for dropwizard

This project provides a [command](http://dropwizard.codahale.com/manual/core/#man-core-commands) for dropwizard that will run an embedded version of [dbdeploy](https://github.com/tackley/dbdeploy). This has the benefit over dropwizard-migrations in that it uses sql deltas rather than liquibase xml change definitions. For an introduction to dbdeploy see [here](http://code.google.com/p/dbdeploy/wiki/GettingStarted).

Whilst dbdeploy only allows for its deltas to be available on the file system, this project uses the [reflections](http://code.google.com/p/reflections/) library, which allows script files to be located inside the main application jar. This means you can provide joint application and database releases in one archive. 

A "run" command is also provided, which allows you to run arbitrary scripts that are present in the classpath.

N.B This is also an easy way to provide a database release as a standalone java application - you don't neccessarily have to use dropwizard as you stack for you application.


## Example project

An example project can be found [here](https://github.com/plasma147/dropwizard-dbdeploy/blob/master/dropwizard-dbdeploy-sample.zip?raw=true)


## Example Yaml File

```yaml
database:  
    
    driverClass : com.mysql.jdbc.Driver
    user        : root
    password    : 
    url         : jdbc:mysql://localhost:3306/demando

    dbdeploy:
       scriptLocation    : db/scripts/deltas/
 
```


## Example Usage

```sh
echo "##########################";
echo "Recreating database!";
echo "##########################";
java -jar dropwizard-dbdeploy-sample-0.0.1-SNAPSHOT.jar db run -s scripts/drop.sql sample.create.yaml

echo "##########################";
echo "Creating changelog table!";
echo "##########################";
java -jar dropwizard-dbdeploy-sample-0.0.1-SNAPSHOT.jar db run -s scripts/createSchemaVersionTable.mysql.sql sample.yaml

echo "##########################";
echo "running upgrade";
echo "##########################";
java -jar dropwizard-dbdeploy-sample-0.0.1-SNAPSHOT.jar db upgrade sample.yaml
```


## Integrating with dbdeploy

dropwizard-dbdeploy is available in the central maven repo:

```xml
<dependency>
  <groupId>uk.co.optimisticpanda</groupId>
  <artifactId>dropwizard-dbdeploy</artifactId>
  <version>0.0.1</version>
</dependency>
```

To add to a dropwizard application, you just need to use the DbDeployDatabaseConfiguration object rather than the DatabaseConfiguration Object. (This extends the DatabaseConfiguration Object, so it's still possible to use [dropwizard-jdbi](http://dropwizard.codahale.com/manual/jdbi/) and [dropwizard-hibernate](http://dropwizard.codahale.com/manual/hibernate/)): 

```java
public class AutoUpgradeDbSampleService extends Service<SampleConfiguration> {
    @Override
    public void initialize(Bootstrap<SampleConfiguration> bootstrap) {
        bootstrap.addBundle(new DbDeployBundle<SampleConfiguration>() {
            public DbDeployDatabaseConfiguration getDatabaseConfiguration(SampleConfiguration configuration) {
                return configuration.getDatabaseConfiguration();
            }
        });
    }
    ...
}
```


## Chaining Database Upgrade and Application Startup

You can easily create a main class, that chains the server and upgrade commands, which will automatically run upgrades on application startup: 

```java
public static void main(String[] args) throws Exception {
        new AutoUpgradeDbSampleService().run(new String[]
                {"db", "upgrade", "sample.yaml"});
        new AutoUpgradeDbSampleService().run(new String[]
                { "server", "sample.yaml"});
    }
```


## Full dbdeploy Options:

Most options have defaults as described below. The only mandatory option is scriptLocation. 

```yaml

database:
    # Supports all of the standard dropwizard database parameters
    driverClass: com.mysql.jdbc.Driver
    user       : root
    password   : 
    url        : jdbc:mysql://localhost:3306/demando
         
    # Configures the dbdeploy instance
    dbdeploy:

       # The location of the classpath folder that contains all of the deltas 
       scriptLocation : scripts/deltas/

       # The name of the dbms. Possible options include: db2, hsql, mssql, mysql, ora, syb-ase 
       dbms           : mysql

       # The delimiter between statements, defaults to ';' 
       delimiter      : ;

       # The type of delimiter 'normal' (delimiter can appear anywhere) or 'row' (must be on a row on its own), defaults to 'normal' 
       delimiterType  : normal
       
       # The encoding present in the script/delta files, defaults to 'UTF-8' 
       encoding       : UTF-8
   
       # The number of the last delta to apply. This is optional with no default
       lastChangeToApply : 099

       # The style of line ending present in the script/delta files. Possible options include : platform, cr, crlf, lf. Defaults to platform line seperator.
       lineEnding : platform
 
       # Specifying this file means that instead of applying the delta to the database, it generates one script with all of the applicable deltas.
       outputFile : output.sql
   
       # Overide the default undo and apply script templates. This points to somewhere on the classpath. This is optional and defaults to the standard templates located inside dbdeploy. 
       templatesLocation : db/templates

       # Specifying this file will generate an undo file in this specific location. This is optional
       undoOutputFile : undoOutput.sql
    
```
