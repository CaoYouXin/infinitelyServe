package tech.caols.infinitely.rest;

import tech.caols.infinitely.server.SimpleServer;

public class Test {

    public static void main(String[] args) {
        SimpleServer simpleServer = new SimpleServer(10000, "");
        RestHelper restHelper = new RestHelper(simpleServer);
        restHelper.addRestObject(new RestObject());
    }

}
