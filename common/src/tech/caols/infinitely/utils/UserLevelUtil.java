package tech.caols.infinitely.utils;

import org.apache.http.protocol.HttpContext;
import tech.caols.infinitely.consts.ConfigsKeys;
import tech.caols.infinitely.datamodels.Configs;
import tech.caols.infinitely.repositories.ConfigsRepository;

public class UserLevelUtil {

    private static ConfigsRepository ConfigsRepository = new ConfigsRepository();

    public static String getUserLevels(HttpContext context) {
        Object attribute = context.getAttribute("USER-LEVELS");
        if (attribute == null) {
            attribute = ConfigsRepository.findByKey(ConfigsKeys.DefaultUserLevel);
            if (attribute != null) {
                attribute = ((Configs) attribute).getValue();
            } else {
                attribute = "";
            }
        }
        return (String) attribute;
    }

}
