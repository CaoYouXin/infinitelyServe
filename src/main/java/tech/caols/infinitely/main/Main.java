package tech.caols.infinitely.main;

import tech.caols.infinitely.register.Register;
import tech.caols.infinitely.server.SimpleServer;

public class Main {

    public static void main(String[] args) {
        SimpleServer simpleServer = new SimpleServer(8889);
        simpleServer.start(new Register());
    }

}
