import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {
    // Keep the parent constructors.
    public EditableBufferedReader(Reader in) {
        super(in);
    }
    public EditableBufferedReader(Reader in, int sz) {
        super(in, sz);
    }

    // Method to change console mode to "raw".
    private void setRaw() {
        String[] cmd = {"/bin/sh", "-c", "stty raw < /dev/tty"};
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to change console mode to "cooked".
    private void unsetRaw() {
        String[] cmd = {"/bin/sh", "-c", "stty cooked < /dev/tty"};
        try {
            Runtime.getRuntime().exec(cmd).waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Override parent method and return just after introducing a character.
    @Override
    public int read() throws IOException {
        try {
            this.setRaw();
            return super.read();
        }
        finally {
            this.unsetRaw();
        }
    }
}
