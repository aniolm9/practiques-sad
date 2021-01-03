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
import java.util.Vector;

public class ChatClient extends JFrame implements ActionListener {
    String port;
    String host;
    int portNum;
    String nickName;
    JList<String> jlu = null;
    JTextArea jta = null;
    JTextField jtf = null;
    JButton jb = null;
    JPanel jp1 = null;
    JScrollPane jsp2 = null;
    JScrollPane jsp = null;
    PrintWriter pw = null;

    public static void main(String[] args) {
        new ChatClient();
    }

    public ChatClient() {
        this.port = "";
        this.host= "";
        this.portNum=0;
        this.nickName="";
        jlu = new JList<String>();
        jta = new JTextArea();//text area
        jsp = new JScrollPane(jta);//scroll panel
        jsp2 = new JScrollPane(jlu);
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
                onOK();
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
        this.setTitle("chat client");
        this.setSize(300, 200);
        this.setVisible(true);

        try {
            // Ask for port and host and validate
            this.host = JOptionPane.showInputDialog("Introduce host");
            while (!host.equals("127.0.0.1")){
                host = JOptionPane.showInputDialog("Wrong Host. Introduce host again");
            }

            do{
                port = JOptionPane.showInputDialog("Introduce port");
                try {
                    portNum = Integer.parseInt(port);
                } catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(jp1,"Port must be a 4 digit number");
                }
                if (portNum!=8800){
                    JOptionPane.showMessageDialog(jp1,"Wrong Port");
                }
            } while (portNum!=8800);


            Socket s = new Socket(host, portNum);
            BufferedReader in = new BufferedReader(new InputStreamReader(s
                    .getInputStream()));
            pw = new PrintWriter(s.getOutputStream(), true);


            String[] serverMsgs = {};
            Vector<String> userList = new Vector<String>();
            String info = "";
            String code = null;

            jta.setText("Introduce Nickname: ");

            //process messages
            while (true) {
                info = in.readLine();
                //Display connected users
                try{
                   code = info.split(",")[0];
                    if(code.contains("Userlist")){
                        userList.clear();
                        userList.addAll(Arrays.asList(info.split(",")));
                        String user = in.readLine();
                        while(user.contains(",")){
                            userList.add(user.split(",")[user.split(",").length-1]);
                            user=in.readLine();
                        }
                        info=user;
                        userList.set(0,"Connected Users:");
                        jlu.setListData(userList);
                        //jlu.updateUI();
                    }
                } catch(ArrayIndexOutOfBoundsException exception){
                    exception.printStackTrace();
                }

                //process messages sent by other users or server
                String str = null;
                if(info.startsWith("/")){

                } else if(info.equals("OK")){
                    jta.setText(jta.getText() + "\n" + "Username Accepted");
                } else {
                    if (jta.getText() == null || "".equals(jta.getText())) {
                        str = info;
                    } else {
                        //if (!info.split(":")[0].equals("Server"))
                        str = jta.getText() + "\r\n" + info;
                    }
                    jta.setText(str);
                    jta.setCaretPosition(jta.getDocument().getLength());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    // give the information to the server
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jb) {
            sendMessage();
        }
    }
    private void onOK(){
        // add your code here
        System.exit(0);
    }
    /**
     * @Description: Send a message to the server
     * @throws
     */
    public void sendMessage() {
        String info = jtf.getText();
        String infoAndNick =this.nickName +": "+ info;
        //pw.println(infoAndNick);
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
    public void sendNick(String username){
        pw.println(username);
        pw.flush();
    }
    public void displayUsers(){

    }
}
