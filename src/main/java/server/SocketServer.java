package server;

import co.gongzh.procbridge.IDelegate;
import co.gongzh.procbridge.Server;
import org.jetbrains.annotations.Nullable;
import server.user.EsentraUser;

import java.io.FileNotFoundException;
import java.util.ArrayList;

@SuppressWarnings("all")
public class SocketServer {

    public static ArrayList<EsentraUser> users = new ArrayList<>();
    private static int count = -1;

    public static void main(String[] args) throws FileNotFoundException {
        Server server = new Server(3000, new IDelegate() {
            @Override
            public @Nullable Object handleRequest(@Nullable String method, @Nullable Object payload) {
                switch (method) {
                    case "echo":
                        System.out.println(method + " " + payload.toString());
                        return payload;
                    case "addUser":
                        String[] userProperties = payload.toString().split(":");
                        users.add(new EsentraUser(userProperties[0], Boolean.parseBoolean(userProperties[1])));
                        count++;
                        System.out.println(users.size());
                        System.out.println(users.get(count).getProperties());
                        return payload;
                    case "isUser":
                        for(EsentraUser user : users) {
                            if(payload.toString().equals(user.getMcName())) {
                                return "true";
                            }
                        }
                        return "false";
                }
                return payload;
            }
        });

        server.start();
        System.out.println("Started server on port " + server.getPort());
    }

}