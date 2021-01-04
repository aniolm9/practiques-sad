package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

public class ChatClient extends JFrame implements ActionListener {
    String port;
    String host;
    int portNum;
    String nickName;
    Vector<String> userList;
    JList<String> jlu;
    JTextArea jta;
    JTextField jtf;
    JButton jb;
    JPanel jp1;
    JScrollPane jsp2;
    JScrollPane jsp;
    PrintWriter pw = null;

    public ChatClient() {
        this.port="";
        this.host="";
        this.portNum=0;
        this.nickName="";
        this.userList = new Vector<String>();
        jlu = new JList<String>();
        jta = new JTextArea();//text area
        jsp = new JScrollPane(jta);//scroll panel
        jsp2 = new JScrollPane(jlu);//users list
        jtf = new JTextField(10);//text box

        // Register a carriage return event
        jtf.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        jb = new JButton("send");
        jb.addActionListener(this);// Register a listener event
        jp1 = new JPanel();
        jp1.add(jtf);
        jp1.add(jb);
        this.add(jsp2, BorderLayout.WEST);
        this.add(jsp, BorderLayout.CENTER);
        this.add(jp1, BorderLayout.SOUTH);
        this.setTitle("Chat Client");
        this.setSize(300, 200);
        this.setVisible(true);
        jta.setText("Introduce Nickname: ");
        jta.setEditable(false);
    }

    private void runClient() {
        try {
            // Ask for port and host and validate
            this.host = JOptionPane.showInputDialog("Introduce host");
            port = JOptionPane.showInputDialog("Introduce port");
            try {
                portNum = Integer.parseInt(port);
            } catch(NumberFormatException e){
                JOptionPane.showMessageDialog(jp1,"Port must be a 4 digit number");
            }

            Socket s = new Socket(host, portNum);
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            pw = new PrintWriter(s.getOutputStream(), true);

            String info = "";
            String msg;
            while (true) {
                info = in.readLine();
                System.out.print(info);
                //Display connected users
                //displayConnectedUsers(in, userList, info);
                if(info.contains("####") ){
                    if(info.split("####").length>1){
                        userList.clear();
                        Collections.addAll(userList, info.split("####")[0].split(","));
                        jlu.setListData(userList);
                    }
                    msg=info.split("####")[info.split("####").length-1];
                } else { msg = info;}
                //process messages sent by other users or server
                String str = null;
                if(msg.startsWith("/")){

                } else if(msg.equals("OK")){
                    jta.setText(jta.getText() + "\n" + "Username Accepted");
                } else {
                    if (jta.getText() == null || "".equals(jta.getText())) {
                        str = msg;
                    } else {
                        //if (!info.split(":")[0].equals("Server"))
                        str = jta.getText() + "\r\n" + msg;
                    }
                    jta.setText(str);
                    jta.setCaretPosition(jta.getDocument().getLength());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(jp1,"No connection to server");
            System.exit(1);
        }
    }

    // Give the information to the server
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb) {
            sendMessage();
        }
    }

    // Send message to server
    public void sendMessage() {
        String info = jtf.getText();
        pw.println(info);
        pw.flush();
        jtf.setText("");
        if (jta.getText() == null || "".equals(jta.getText())) {
            jta.append("You:" + info);
        } else {
            jta.append("\r\nYou: " + info);
        }
        jta.setCaretPosition(jta.getDocument().getLength()); // Set scroll bar auto scrolling
    }

    /* When receiving a Userlist from broadcastUsers,
     puts the users in the client list and displays it in Jlist jlu.
     Then returns the last received message which is not a user,
     so the run method is able to process it
    */
    /*private void displayConnectedUsers(BufferedReader in, Vector<String> userList, String info) throws IOException {
        String code;
        try {
            code = info.split(",")[0];
            if(code.contains("Userlist")){
                userList.clear();
                userList.addAll(Arrays.asList(info.split(",")));
                String user = in.readLine();
                while(user.contains(",")){
                    userList.add(user.split(",")[user.split(",").length-1]);
                    user= in.readLine();
                }
                info =user;
                userList.set(0,"Connected Users:");
                jlu.setListData(userList);
            }
        } catch(ArrayIndexOutOfBoundsException exception){
            exception.printStackTrace();
        }
        //return info;
    }*/

    public static void main(String[] args) {
        ChatClient client = new ChatClient();
        client.runClient();
    }
}
