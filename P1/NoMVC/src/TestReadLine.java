import java.io.*;

class TestReadLine {
    public static void main(String[] args) {
        BufferedReader in = new EditableBufferedReader(
                new InputStreamReader(System.in));
        String str = null;
        int n = 0;
        try {
            //str = in.readLine();
            n = in.read();
        } catch (IOException e) { e.printStackTrace(); }
        System.out.println("\nline is: " + n);
    }
}