package tech.caols.infinitely.db;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test0 {

    public static void main(String[] args) {
        Pattern p = Pattern.compile(String.format("(?<alias>%s)\\.(?<field>\\S+?)(?<after>>|<|\\s|\\)|=)", "a"));
        Matcher m = p.matcher("a.aFloat > 0.5");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            System.out.println(m.group("alias"));
            System.out.println(m.group("field"));
            System.out.println(m.group("after"));

//            String cat = m.group("cat");
//            System.out.println(cat);
            m.appendReplacement(sb, "dog");
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
    }

}
