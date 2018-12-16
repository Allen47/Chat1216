package Client;
import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class NetClient extends Thread {
    private String serverIp;
    private int port;
    private OutputStream ous;
    private BufferedReader brd;
    private Socket client;
    private JTextArea jta_recive;
    String name;
    String pwd;

    public NetClient(String serverIp, int port, JTextArea jta_recive) {
        this.serverIp = serverIp;
        this.port = port;
        this.jta_recive = jta_recive;
    }

    public boolean coon2Server() {
        try {
            this.client = new Socket(this.serverIp, this.port);
            InputStream ins = this.client.getInputStream();
            this.brd = new BufferedReader(new InputStreamReader(ins));
            this.ous = this.client.getOutputStream();
            return true;
        } catch (Exception var2) {
            var2.printStackTrace();
            return false;
        }
    }

    public boolean loginServer(String name, String pwd) {
        try {
            this.name = name;
            String input = this.brd.readLine();
            System.out.println("服务器说：" + input);
            name = name + "\r\n";
            this.ous.write(name.getBytes());
            this.ous.flush();
            System.out.println("客户机已经将用户名发送，等待服务器回应");
            this.pwd = pwd;
            input = this.brd.readLine();
            System.out.println("服务器说：" + input);
            pwd = pwd + "\r\n";
            this.ous.write(pwd.getBytes());
            this.ous.flush();
            return true;
        } catch (Exception var4) {
            return false;
        }
    }

    public void run() {
        while(this.isConnected()) {
            this.readFromServer();
        }
        CloseMe();
    }

    public void readFromServer() {
        try {
            String input = this.brd.readLine();
            System.out.println("服务器说：" + input);
            this.jta_recive.append(input + "\r\n");

        } catch (Exception var2) {

        }

    }

    public void sendMsgToAll(String msg) {
        try {
            if (this.name.equals("admin")) {
                msg = "!" + msg;
            } else {
                msg = "A" + msg;
            }
            msg = msg + "\r\n";
            this.ous.write(msg.getBytes());
            this.ous.flush();
        } catch (Exception var3) {
            var3.printStackTrace();
            System.out.println("断开连接");
            CloseMe();
        }
    }

    public void sendMsgToOne(String msg,String name){
        try {
            msg='@'+name+'#'+this.name+'%'+msg+"\r\n";
            this.ous.write(msg.getBytes());
            this.ous.flush();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("断开连接");
            CloseMe();
        }

    }


    public void sendCloseMsg(String msg){
        msg=msg+"\r\n";
        try {
            this.ous.write(msg.getBytes());
            this.ous.flush();
            CloseMe();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isConnected() {
        try {
            this.client.sendUrgentData(255);
            return true;
        } catch (Exception var2) {
            return false;
        }
    }

    public void CloseMe() {
        try {
            client.close();
            ListTools.RemoveClient(this.name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
