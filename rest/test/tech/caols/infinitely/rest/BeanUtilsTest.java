package tech.caols.infinitely.rest;

public class BeanUtilsTest {

    public static void main(String[] args) {
        Bean bean1 = new Bean();
        Bean bean2 = new Bean();

        bean1.setaBoolean(true);
        bean1.setString("hello world");

        System.out.println(bean1);
        System.out.println(bean2);

        BeanUtils.copyBean(bean1, bean2);

        System.out.println(bean1);
        System.out.println(bean2);
    }

}
