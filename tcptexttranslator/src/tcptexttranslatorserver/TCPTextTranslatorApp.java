package tcptexttranslatorserver;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This main  class launches the application 
 * @author Mirza Sahid Afridi
 */

public class TCPTextTranslatorApp {

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Translator translate = new Translator(); 

        // Create data from Translator.java using streams
        translate.createData();

        try {
            int totalRequest = 0;

            // Bind request to a port number, 6789
            int portNo = 6999;
            serverSocket = new ServerSocket(portNo);

            // Listen continuously for requests for connection
            // Server needs to be alive forever in an unterminated while loop
            while (true) {
                // Accept client request for connection
                Socket clientSocket = serverSocket.accept();

                // Create input stream to read the text input from the client
                DataInputStream inputStream = new DataInputStream
                		(clientSocket.getInputStream());

                // The choice of target language from the client
                String text = inputStream.readUTF();
                int languageChoice = inputStream.readInt();
                String language = "";
                String translated = "";

                // If the client input target language is Malay
                if (languageChoice == 1) {
                    // Parse the text for Malay translation
                    language = "Malay";
                    translated = translate.translateToBM(text);
                }
                // Else if the client input target language is Arabic
                else if (languageChoice == 2) {
                    // Parse the text for Arabic translation
                    language = "Arabic";
                    translated = translate.translateToArb(text);
                }
                // Else if the client input target language is Korean
                else if (languageChoice == 3) {
                    // Parse the text for Korean translation
                    language = "Korean";
                    translated = translate.translateToKrn(text);
                }

                // Create stream to write data on the network
                DataOutputStream outputStream = new DataOutputStream
                		(clientSocket.getOutputStream());

                // Send the translated text back to the client
                outputStream.writeUTF(translated);

                // Update and display the request status
                System.out.println("Data sent to the client: " + text + 
                		" is translated to " + language);
                System.out.println("Accepted connection from the client. "
                		+ "Total request = " + ++totalRequest);

                text = "";
                language = "";

                // Close the socket
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close the server socket when the server is stopped
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}