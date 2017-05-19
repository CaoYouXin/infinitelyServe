package tech.caols.infinitely.cmd;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CMDTest {

    private static final Logger logger = LogManager.getLogger(CMDTest.class);

    public static void main(String[] args) throws IOException, InterruptedException {

//        String dir = "/Users/cls/Dev/Git/personal/infinitely/serve/testout/";
//        String fileName = "service001_jar";
//        run("unzip -qo " + fileName + ".zip -d " + dir, true);
//        run("java -jar " + dir + fileName + "/"
//                + fileName.replace('_', '.') + " start", false);

        run("ls /Users/cls/Dev/Git/personal/infinitely/serve/out/artifacts/*/*.log", true);
    }

    public static void run(String cmd, boolean wait) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd });

        if (!wait) {
            return;
        }

        InputStream stdin = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        logger.info("<OUTPUT>");

        while ( (line = br.readLine()) != null)
            logger.info(line);

        logger.info("</OUTPUT>");
        int exitVal = proc.waitFor();
        logger.info("Process exitValue: " + exitVal);
    }

}
