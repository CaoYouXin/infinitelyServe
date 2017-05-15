package tech.caols.infinitely.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import tech.caols.infinitely.Constants;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public class ListServerHandler implements HttpRequestHandler {

    private final String serverRoot;
    private final String uploadRoot;

    public ListServerHandler(String serverRoot, String uploadRoot) {
        this.serverRoot = serverRoot;
        this.uploadRoot = uploadRoot;
    }

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (!HttpUtils.isGet(httpRequest)) {
            throw new MethodNotSupportedException(HttpUtils.getMethod(httpRequest) + " method not supported");
        }

        List<Server> body = this.collectDeployedServer();
        body.addAll(this.collectNotDeployedServer());
        HttpUtils.response(httpContext, body);
    }

    private Collection<Server> collectNotDeployedServer() throws IOException {
        List<String> notDeployedServers = run("ls " + this.uploadRoot + "*_jar.zip", true);

        final List<Server> ret = new ArrayList<>();
        notDeployedServers.forEach(notDeployedServer -> {
            Server server = new Server();

            String serverName = notDeployedServer.substring(
                    this.uploadRoot.length(), notDeployedServer.lastIndexOf(".zip")
            );
            server.setName(serverName);
            server.setDeployed(false);
            server.setStatus(Server.NOT_RUNNING);
            server.setType(getType(serverName));

            ret.add(server);
        });

        return ret;
    }

    private List<Server> collectDeployedServer() throws IOException {
        final List<String> deployedServers = this.run("ls " + this.serverRoot, true);
        final List<String> runningServers = this.run("ls " + this.serverRoot + "*/*.log", true);

        final List<Server> ret = new ArrayList<>();
        deployedServers.forEach(deployedServer -> {
            Server server = new Server();

            server.setName(deployedServer);
            server.setDeployed(true);
            server.setType(getType(deployedServer));
            server.setStatus(runningServers.stream()
                    .anyMatch(runningServer -> runningServer.contains(deployedServer))
                    ? Server.RUNNING : Server.NOT_RUNNING);

            ret.add(server);
        });

        return ret;
    }

    private String getType(String serverName) {
        if (serverName.startsWith("pre_")) {
            return Server.PRE;
        } else if (serverName.startsWith("post_")) {
            return Server.POST;
        } else if (serverName.startsWith("service_")) {
            return Server.SERVICE;
        } else {
            throw new RuntimeException(serverName + " con not be parsed to any type");
        }
    }

    static class Server {
        public static final String PRE = "pre processor";
        public static final String POST = "post processor";
        public static final String SERVICE = "service";

        public static final String RUNNING = "running";
        public static final String NOT_RUNNING = "not running";

        private String type;
        private String name;
        private String status;
        private boolean deployed;

        @Override
        public String toString() {
            return "Server{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", status='" + status + '\'' +
                    ", deployed=" + deployed +
                    '}';
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public boolean isDeployed() {
            return deployed;
        }

        public void setDeployed(boolean deployed) {
            this.deployed = deployed;
        }
    }

    private List<String> run(String cmd, boolean needOutput) throws IOException {
        System.out.println(cmd);
        Process process = Runtime.getRuntime().exec(new String[] { "bash", "-c", cmd });

        if (!needOutput) {
            return null;
        }
        List<String> ret = new ArrayList<>();

        InputStream stdin = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(stdin);
        BufferedReader br = new BufferedReader(isr);

        String line = null;
        System.out.println("<OUTPUT>");

        while ( (line = br.readLine()) != null) {
            System.out.println(line);
            ret.add(line);
        }

        System.out.println("</OUTPUT>");
        int exitVal = 0;
        try {
            exitVal = process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Process exitValue: " + exitVal);

        return ret;
    }

}
