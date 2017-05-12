package tech.caols.infinitely.cmd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CMDTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        String dir = "/Users/cls/Dev/Git/personal/infinitely/serve/testout/";
        String fileName = "service001_jar";
        run("unzip -qo " + fileName + ".zip -d " + dir, true);
        run("java -jar " + dir + fileName + "/"
                + fileName.replace('_', '.') + " start", false);

    }

    public static void run(String cmd, boolean wait) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(cmd);

        if (!wait) {
            return;
        }

        InputStream stdin = proc.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        System.out.println("<OUTPUT>");

        while ( (line = br.readLine()) != null)
            System.out.println(line);

        System.out.println("</OUTPUT>");
        int exitVal = proc.waitFor();
        System.out.println("Process exitValue: " + exitVal);
    }

}