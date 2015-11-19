package il.ac.technion.ie.dataset.prepare.utils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by I062070 on 19/11/2015.
 */
public class Util {
    private static final Logger logger = Logger.getLogger(Util.class);


    public static Integer convertStringToInt(String key) {
        if (StringUtils.isNumeric(key)) {
            return Integer.valueOf(key);
        }
        logger.warn("Given key is not numeric: " + key);
        return null;
    }

    public static Double convertStringToDouble(String key) {
        try {
            return Double.parseDouble(key);
        } catch (NumberFormatException nfe) {
            logger.warn("Given key is not numeric: " + key);
        }
        return null;
    }
}
