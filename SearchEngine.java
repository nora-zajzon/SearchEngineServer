import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    String[] word = new String[]{};

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return String.format("Your word list is: " + Arrays.asList(word));
        } else{
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("word")) {
                    String[] copy = Arrays.copyOf(word, word.length + 1);
                    copy[word.length] = parameters[1];
                    word = copy;
                    return String.format("New list of words: " + Arrays.asList(word));
                }
            }else if (url.getPath().contains("/remove")) {
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("word")) {
                    int index = Arrays.asList(word).indexOf(parameters[1]);
                    if (index != -1) {
                        List<String> wordList = new ArrayList<>(Arrays.asList(word));
                        wordList.remove(index);
                        word = wordList.toArray(new String[0]);
                        return String.format("Updated list of words: " + Arrays.asList(word));
                    }
                }
            }
            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
