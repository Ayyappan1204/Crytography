import java.io.*;
import java.net.*;
import java.util.*;

public class PlayfairClient {

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

    // Prepare plaintext
    static String prepareText(String text) {

        text = text.toUpperCase().replaceAll("[^A-Z]", "").replace("J", "I");

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {

            sb.append(text.charAt(i));

            if (i == text.length() - 1) {
                sb.append('X');
            } else if (text.charAt(i) == text.charAt(i + 1)) {
                sb.append('X');
            } else {
                sb.append(text.charAt(++i));
            }
        }

        if (sb.length() % 2 != 0)
            sb.append('X');

        return sb.toString();
    }

    // Encrypt
    static String encrypt(String text) {

        text = prepareText(text);

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < text.length(); i += 2) {

            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int[] p1 = position.get(a);
            int[] p2 = position.get(b);

            if (p1[0] == p2[0]) {

                result.append(matrix[p1[0]][(p1[1] + 1) % 5]);
                result.append(matrix[p2[0]][(p2[1] + 1) % 5]);

            } else if (p1[1] == p2[1]) {

                result.append(matrix[(p1[0] + 1) % 5][p1[1]]);
                result.append(matrix[(p2[0] + 1) % 5][p2[1]]);

            } else {

                result.append(matrix[p1[0]][p2[1]]);
                result.append(matrix[p2[0]][p1[1]]);
            }
        }

        return result.toString();
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        System.out.print("Enter Key : ");
        String key = sc.nextLine();

        generateMatrix(key);

        System.out.print("Enter Plain Text : ");
        String plain = sc.nextLine();

        String cipher = encrypt(plain);

        System.out.println("Encrypted Text : " + cipher);

        dos.writeUTF(key);
        dos.writeUTF(cipher);

        dos.close();
        socket.close();
        sc.close();
    }
}