import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AffineServer {
   
    public static int modInverse(int a, int m) {
        a = a % m;
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1) {
                return i;
            }
        }
        return -1;
    }
   
    public static String decrypt(String cipherText, int a, int b) {

        StringBuilder plain = new StringBuilder();

        int inverse = modInverse(a, 26);

        if (inverse == -1) {
            return "Invalid Key! 'a' has no multiplicative inverse.";
        }

        cipherText = cipherText.toUpperCase();

        for (int i = 0; i < cipherText.length(); i++) {

            char ch = cipherText.charAt(i);

            if (Character.isLetter(ch)) {

                int y = ch - 'A';
                int decrypted = (inverse * (y - b + 26)) % 26;
                plain.append((char) (decrypted + 'A'));

            } else {

                plain.append(ch);
            }
        }

        return plain.toString();
    }

    public static void main(String[] args) {
        int port = 6666;
         
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running");
           
            try (Socket socket = serverSocket.accept();
                 DataInputStream in = new DataInputStream(socket.getInputStream())) {
               
                System.out.println("Client connected.");
                String ciphertext = in.readUTF();
               
                System.out.println("Received Ciphertext: " + ciphertext);
               
                int a = in.read();
                int b = in.read();
               
                String plaintext = decrypt(ciphertext, a ,b);
                System.out.println("Decrypted Plaintext: " + plaintext);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}