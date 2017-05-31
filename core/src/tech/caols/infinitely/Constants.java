package tech.caols.infinitely;

public class Constants {

    /**
     * processor type
     */
    public static final String PRE_PROCESSOR = "pre";
    public static final String POST_PROCESSOR = "post";

    /**
     * proxy handler
     */
    public static final String RET_OBJECT = "ret-object";
    public static final String RET_OBJECT_STRING = "ret-object-string";
    public static final String CODE = "code";
    public static final String BODY = "body";
    public static final String OUT_SET = "set";
    public static final String OUT_REMOVE = "remove";

    /**
     * resource service
     */
    public static final String USER_LEVELS = "User_Levels";

    /**
     * ret code
     */
    public static final int CODE_INVALID = 50000;
    public static final int CODE_VALID = 20000;
    public static final int CODE_REPLACE_VALID = 30000;
    public static final int CODE_NO_USER = 50200;
    public static final int CODE_WRONG_PWD = 50201;
    public static final int CODE_WRONG_CAPTCHA = 50202;
    public static final int CODE_USED_PHONE = 50203;
    public static final int CODE_UNAUTHED = 50300;
    public static final int CODE_UNPRIVILEGED = 50301;

}
