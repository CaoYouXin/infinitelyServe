package tech.caols.infinitely.utils;

import java.util.StringJoiner;

public class UserLevelUtilTest {

    public static void main(String[] args) {
        StringJoiner stringJoiner = new StringJoiner("', '", "('", "')");
        for (String s : "".split(",")) {
            stringJoiner.add(s);
        }
        System.out.println(stringJoiner.toString());
    }

}
