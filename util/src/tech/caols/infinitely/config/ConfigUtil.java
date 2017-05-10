package tech.caols.infinitely.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;

public class ConfigUtil {

    private String basePathOfClass = getClass()
            .getProtectionDomain().getCodeSource().getLocation().getFile();

    private String rootFileName;

    public ConfigUtil() {
        String bpoc = this.basePathOfClass.substring(0,
                this.basePathOfClass.lastIndexOf(IOUtils.DIR_SEPARATOR) + 1);
        this.rootFileName = this.basePathOfClass.substring(bpoc.length(),
                this.basePathOfClass.lastIndexOf('.'));
        this.basePathOfClass = bpoc;
    }

    public <C> C getConfig(String fileName, Class<C> clazz) {
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

        String result = "";

        try {
            result = IOUtils.toString(new FileInputStream(new File(this.basePathOfClass, fileName)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String getRootFileName() {
        return rootFileName;
    }
}
