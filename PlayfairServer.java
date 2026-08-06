import java.io.*;
import java.net.*;
import java.util.*;

public class PlayfairServer {

    static char[][] matrix = new char[5][5];
    static Map<Character, int[]> position = new HashMap<>();

    // Generate 5x5 key matrix
    static void generateMatrix(String key) {

        boolean[] used = new boolean[26];
        key = key.toUpperCase().replace("J", "I");

        StringBuilder sb = new StringBuilder();

        for (char c : key.toCharArray()) {
            if (c >= 'A' && c <= 'Z' && !used[c - 'A']) {
                used[c - 'A'] = true;
                sb.append(c);
            }
        }

        for (char c = 'A'; c <= 'Z'; c++) {

            if (c == 'J')
                continue;

            if (!used[c - 'A'])
                sb.append(c);
        }

        int k = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = sb.charAt(k);
                position.put(matrix[i][j], new int[] { i, j });
                k++;
            }
        }
    }

    // Decrypt
    static String decrypt(String text) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int[] p1 = position.get(a);
            int[] p2 = position.get(b);

            if (p1[0] == p2[0]) {

                result.append(matrix[p1[0]][(p1[1] + 4) % 5]);
                result.append(matrix[p2[0]][(p2[1] + 4) % 5]);

            } else if (p1[1] == p2[1]) {

                result.append(matrix[(p1[0] + 4) % 5][p1[1]]);
                result.append(matrix[(p2[0] + 4) % 5][p2[1]]);

            } else {

                result.append(matrix[p1[0]][p2[1]]);
                result.append(matrix[p2[0]][p1[1]]);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws Exception {

        ServerSocket server = new ServerSocket(5000);

        System.out.println("Server Waiting...");

        Socket socket = server.accept();

        System.out.println("Client Connected");

        DataInputStream dis = new DataInputStream(socket.getInputStream());

        String key = dis.readUTF();
        String cipher = dis.readUTF();

        generateMatrix(key);

        System.out.println("Received Key : " + key);
        System.out.println("Received Cipher Text : " + cipher);

        String plain = decrypt(cipher);

        System.out.println("Decrypted Text : " + plain);

        dis.close();
        socket.close();
        server.close();
    }
}