package il.ac.technion.ie.dataset.prepare.context;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by I062070 on 18/11/2015.
 */
public class DatasetProperties {

    private static final Logger logger = Logger.getLogger(DatasetProperties.class);
    private final PropertiesConfiguration propertiesConfiguration;

    private DatasetProperties(PropertiesConfiguration propertiesConfiguration) {
        this.propertiesConfiguration = propertiesConfiguration;
    }

    public static DatasetProperties factory(String pathToPropFile) throws FileNotFoundException {
        PropertiesConfiguration configuration = DatasetProperties.init(pathToPropFile);
        DatasetProperties datasetProperties = new DatasetProperties(configuration);
        return datasetProperties;
    }


    private static PropertiesConfiguration init(String pathToPropFile) throws FileNotFoundException {
        if (StringUtils.isEmpty(pathToPropFile)) {
            throw new FileNotFoundException("prop file not exists at: " + pathToPropFile);
        }
        File file = new File(pathToPropFile);
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("prop file not exists at: " + pathToPropFile);
        }
        try {
            return new PropertiesConfiguration(pathToPropFile);
        } catch (ConfigurationException e) {
            logger.error("Failed to create propertiesConfiguration", e);
        }
        return null;
    }

    public PropertiesConfiguration getPropertiesConfiguration() {
        return propertiesConfiguration;
    }
}
