package tech.caols.infinitely.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import tech.caols.infinitely.SimpleUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class ConfigUtil {

    private String basePathOfClass = getClass()
            .getProtectionDomain().getCodeSource().getLocation().getFile();

    private String rootFileName;

    public ConfigUtil() {
        int lastIndexOf = this.basePathOfClass.lastIndexOf(IOUtils.DIR_SEPARATOR);

        if (lastIndexOf != this.basePathOfClass.length() - 1) {
            String bpoc = this.basePathOfClass.substring(0, lastIndexOf + 1);
            this.rootFileName = this.basePathOfClass.substring(bpoc.length(),
                    this.basePathOfClass.lastIndexOf('.'));
            this.basePathOfClass = bpoc;
        }
    }

    public <C> C getConfigFromFile(String fileName, Class<C> clazz) {
        try {
            return new ObjectMapper().readValue(this.getFileWithUtil(fileName), clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setConfig(String fileName, Object config) {

        try {
            IOUtils.write(new ObjectMapper().writeValueAsString(config),
                    new FileOutputStream(new File(this.basePathOfClass, fileName))
                    , Consts.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String getFileWithUtil(String fileName) {
        return SimpleUtils.getFileWithUtil(new File(this.basePathOfClass, fileName));
    }

    public String getRootFileName() {
        return rootFileName;
    }

    public void eraseConfigFile(String fileName) {
        File file = new File(this.basePathOfClass, fileName);
        file.deleteOnExit();
    }
}
