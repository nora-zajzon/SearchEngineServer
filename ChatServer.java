import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

interface URLHandler {
    String handleRequest(URI url);
}

class Handler implements URLHandler {
    // The one bit of state on the server: a list of chat messages that will be manipulated by various requests.
    List<String> chatMessages = new ArrayList<>();

    @Override
    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return "Chat messages so far:\n" + String.join("\n", chatMessages);
        } else if (url.getPath().contains("/add-message")) {
            String[] parameters = url.getQuery().split("&");
            String user = "";
            String message = "";

            for (String param : parameters) {
                if (param.startsWith("s=")) {
                    message = param.substring(2);
                } else if (param.startsWith("user=")) {
                    user = param.substring(5);
                }
            }

            if (!user.isEmpty() && !message.isEmpty()) {
                String chatMessage = user + ": " + message;
                chatMessages.add(chatMessage);
                return "Updated chat messages:\n" + String.join("\n", chatMessages);
            } else {
                return "Invalid request. Both 's' and 'user' parameters are required.";
            }
        } else {
            return "404 Not Found!";
        }
    }
}

public class ChatServer {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
