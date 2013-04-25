package uk.co.optimisticpanda.dropwizard.dbdeploy;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dbdeploy.exceptions.UnrecognisedFilenameException;
import com.dbdeploy.scripts.ChangeScript;
import com.dbdeploy.scripts.FilenameParser;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

public class ClasspathResourceScanner {
    private static final Logger logger = LoggerFactory.getLogger(ClasspathResourceScanner.class);
    private final FilenameParser filenameParser = new FilenameParser();
    private final Charset encoding;

    public ClasspathResourceScanner(Charset encoding) {
        this.encoding = encoding;
    }

    public List<ChangeScript> getChangeScriptsForLocation(String location) {
        logger.info("Reading change scripts from location: " + location + "...");

        List<ChangeScript> scripts = Lists.newArrayList();

        for (String resource : scan(location)) {
            try {
                String fileName = getFileNameForResource(resource);
                long id = filenameParser.extractIdFromFilename(fileName);
                scripts.add(new ClasspathChangeScript(id, resource, encoding));
            } catch (UnrecognisedFilenameException e) {
                // ignore
            }
        }

        return scripts;
    }

    public static Collection<String> scan(final String path) {
        Reflections reflections = new Reflections( //
                new ConfigurationBuilder().setScanners( //
                        new ResourcesScanner()).setUrls( //
                        ClasspathHelper.forPackage(path)));
        
        Map<String, Multimap<String, String>> store = reflections.getStore().getStoreMap();
        Preconditions.checkArgument(store.size() == 1, "Stores should just have one key");
        Multimap<String, String> foundResouces = store.values().iterator().next();
       
        return Collections2.filter(foundResouces.values(), new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.startsWith(path);
            }
        });

    }

    public static String load(Charset charset, String resourceName) {
        URL resource = Resources.getResource(resourceName);
        try {
            logger.debug("loading resource: {}", resourceName);
            return Resources.toString(resource, charset);
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static InputSupplier<InputStreamReader> reader(Charset charset, String resourceName) {
        URL resource = Resources.getResource(resourceName);
        return Resources.newReaderSupplier(resource, charset);
    }

    public static String getFileNameForResource(String resource) {
        URL url = Resources.getResource(resource);
        logger.debug("Extracting file name from url: {}", url);
        return new File(url.getFile()).getName();
    }

}
