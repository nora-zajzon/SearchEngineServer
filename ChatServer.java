import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    List<String> chatMessages = new ArrayList<>();

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return "Chat messages so far:\n" + String.join("\n", chatMessages);
        } else{
            if (url.getPath().contains("/add-message")) {
                String[] parameters = url.getQuery().split("&");
                if (parameters[0].substring(0,2).equals("s=")) {
                    String message = parameters[0].substring(2);
                    String user = parameters[1].substring(5);
                    String chatMessage = user + ": " + message;
                    chatMessages.add(chatMessage);
                    return "Updated chat messages:\n" + String.join("\n", chatMessages);
                    }
                }
            }
            return "404 Not Found!";
        }
    }

class ChatServer{
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
