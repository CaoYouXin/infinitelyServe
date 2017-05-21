package tech.caols.infinitely;

import java.util.Date;

public class UtilsTest {

    public static void main(String[] args) {
        String md5 = SimpleUtils.getMD5("18618128264" + new Date());
        System.out.println(md5.toUpperCase().length());
        System.out.println(md5.toUpperCase().substring(0, 5));
    }

}
