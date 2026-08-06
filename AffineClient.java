import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AffineClient {
   
    public static String encrypt(String text, int a, int b) {

        StringBuilder cipher = new StringBuilder();
        text = text.toUpperCase();

        for (int i = 0; i < text.length(); i++) {

            char ch = text.charAt(i);

            if (Character.isLetter(ch)) {

                int x = ch - 'A';
                int encrypted = (a * x + b) % 26;
                cipher.append((char) (encrypted + 'A'));

            } else {

                cipher.append(ch);
            }
        }

        return cipher.toString();
    }
   
    public static int modInverse(int a, int m) {
        a = a % m;
        for (int i = 1; i < m; i++) {
            if ((a * i) % m == 1) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 6666;
       

        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket(host, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream())) {
           
            System.out.println("Enter message: ");
            String plaintext = sc.nextLine();
           
            System.out.println("Enter the value of a:");
            int a = sc.nextInt();
           
            System.out.println("Enter the value of b:");
            int b = sc.nextInt();
           
            if (modInverse(a, 26) == -1) {

            System.out.println("\nInvalid value of 'a'");
            System.out.println("Choose one of these values:");
            System.out.println("1, 3, 5, 7, 9, 11, 15, 17, 19, 21, 23, 25");
            sc.close();
            return;
            }

           
            String ciphertext = encrypt(plaintext, a , b);
            System.out.println("Encrypted Ciphertext: " + ciphertext);
           
            out.writeUTF(ciphertext);
            out.write(a);
            out.write(b);
            out.flush();
            System.out.println("Message sent successfully");
           
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


