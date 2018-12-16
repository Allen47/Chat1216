package Server;

import java.io.*;
import java.net.Socket;

public class ServerThread extends Thread {
    private Socket client;//线程中处理的客户对象
    private OutputStream ous;//输出流对象
    private UserInfo user;//这个线程处理对象代表的用户的信息
    //创建对象时必须传入一个Socket对象
    public ServerThread(Socket client) {
        this.client=client;
    }
    //获取这个线程对象代表的用户对象
    public UserInfo getOwerUser() {
        return this.user;
    }
    public void run() {
        processSocket();
    }

    public void sendMsg2Me(String msg) {
        try {
            msg+="\r\n";
            ous.write(msg.getBytes());
            ous.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
            System.out.println(user.getName()+"断开连接");
            ChatTools.removeClient(user);
        }
    }
    //读取客户机发来的消息
    private void processSocket() {
        try {
            InputStream ins=client.getInputStream();
            ous=client.getOutputStream();
            //将输入流ins封装为可以读取一行字符串也就是以\r\n结尾的字符串
            BufferedReader brd=new BufferedReader(new InputStreamReader(ins));
            sendMsg2Me("欢迎您来聊天！请输入你的用户名：");
            String userName=brd.readLine();
            sendMsg2Me(userName+"请输入你的密码");
            String pws=brd.readLine();
            user=new UserInfo();
            user.setName(userName);
            user.setPwd(pws);
            //调用数据库模块，验证用户是否存在
            boolean loginState= DaoTools.checkLogin(user);
            if(!loginState) {//不存在则账号关闭
                this.closeMe();
                return;
            }
            ChatTools.addClient(this);//认证成功：将这个对象加入服务器队列
            String input=brd.readLine();//一行一行的读取客户机发来的消息
            while(isConnected()) {
                if(isConnected()) {
                    System.out.println("服务器收到的是"+input);
                    //读到一条消息后，就发送给其他的客户机
                    ChatTools.castMsg(this.user, input);
                    input=brd.readLine();//读取下一条
                }else{
                    //ChatTools.castMsg(this.user, "我下线了，再见！");
                    break;
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
            ChatTools.castMsg(this.user, "我下线了，再见！");
            ChatTools.removeClient(this.user);
            this.closeMe();
        }
    }
    //关闭这个线程
    public void closeMe() {
        try {
            client.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public boolean isConnected(){
        try{
            client.sendUrgentData(0xFF);
            return true;
        }catch(Exception e){
            return false;
        }

    }

}
