package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainNetUI {
    private JFrame jf_login;// 登陆界面
    private JFrame jf_chat;// 聊天界面
    private JFrame jf_list;//好友界面
    private JFrame jf_re;
    private JTextField jta_userName;// 登陆界面上的用户名输入框
    private JPasswordField jta_pwd;
    private JTextArea jta_input = new JTextArea(5, 20);// 显示接收到的消息组
    private NetClient conn;// 界面所要使用到的连结对象
    String name,pwd;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MainNetUI qu = new MainNetUI();
        qu.showLoginUI();
    }

    public void showLoginUI() {
        jf_login = new JFrame();
        jf_login.setTitle("QQ");
        jf_login.setSize(430, 355);
        jf_login.setLocationRelativeTo(null);
        jf_login.setDefaultCloseOperation(3);
        jf_login.setResizable(false);

        BorderLayout b1 = new BorderLayout();
        jf_login.setLayout(b1);
        // 添加图片 北
        ImageIcon icon = new ImageIcon("src/Client/back.png");
        JLabel labIcon = new JLabel(icon);
        jf_login.add(labIcon, BorderLayout.NORTH);

        // 添加面板容器：账号、密码
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));

        JLabel labName = new JLabel("账号:");
        labName.setHorizontalAlignment(JLabel.RIGHT);
        labName.setPreferredSize(new Dimension(110, 30));
        centerPanel.add(labName);

        jta_userName = new JTextField();
        jta_userName.setPreferredSize(new Dimension(180, 30));
        centerPanel.add(jta_userName);

        JLabel labPassowrd = new JLabel("密码 :");
        labPassowrd.setHorizontalAlignment(JLabel.RIGHT);
        labPassowrd.setPreferredSize(new Dimension(110, 30));
        centerPanel.add(labPassowrd);

        jta_pwd = new JPasswordField();
        jta_pwd.setPreferredSize(new Dimension(180, 30));
        centerPanel.add(jta_pwd, labPassowrd);

        JPanel centerPanel1 = new JPanel();
        centerPanel1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JCheckBox rememberBox = new JCheckBox();
        rememberBox.setText("记住密码");
        rememberBox.setHorizontalAlignment(JLabel.RIGHT);
        rememberBox.setPreferredSize(new Dimension(140, 30));
        centerPanel.add(rememberBox);

        JCheckBox rememberBox2 = new JCheckBox();
        rememberBox2.setText("自动登录");
        rememberBox2.setHorizontalAlignment(JLabel.RIGHT);
        rememberBox2.setPreferredSize(new Dimension(100, 30));
        centerPanel.add(rememberBox2);

        JButton southButton = new JButton();
        southButton.setPreferredSize(new Dimension(120, 40));
        southButton.setText("登录");
        centerPanel1.add(southButton);

        JButton registerButton = new JButton();
        registerButton.setPreferredSize(new Dimension(120, 40));
        registerButton.setText("注册");
        centerPanel1.add(registerButton);

        jf_login.add(centerPanel1, BorderLayout.SOUTH);
        jf_login.add(centerPanel, BorderLayout.CENTER);

        southButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginAction();
            }
        });

        registerButton.addActionListener(new ActionListener() {
//            @Override
            public void actionPerformed(ActionEvent e) {
                JTextArea jta_re = new JTextArea(5, 20);// 显示接收到的消息组
                NetClient nc=new NetClient("localhost",6666,jta_re);
                if(nc.coon2Server()){
                    nc.loginServer("admin","admin");
                    showReUI(nc);
                }
            }
        });

        jf_login.setVisible(true);
    }

    // 登陆事件处理
    private void loginAction() {
        name = jta_userName.getText();
        pwd = String.valueOf(jta_pwd);
        conn = new NetClient("localhost", 6666, jta_input);
        if (conn.coon2Server()) {
            if (conn.loginServer(name, pwd)) {
                ListTools.addClient(conn);
                showMainUI();
                conn.start();
                jf_login.dispose();
            } else {
                JOptionPane.showMessageDialog(jf_login, "帐号有误！user1~10");
            }
        }
    }

    // 显示聊天界面
    public void showMainUI() {
        FlowLayout fl = new FlowLayout();

        JPanel jp_North=new JPanel();
        jp_North.setPreferredSize(new Dimension(600,300));
        jp_North.setLayout(fl);

        JPanel jp_South=new JPanel();
        jp_South.setPreferredSize(new Dimension(600,200));
        jp_South.setLayout(fl);

        jf_chat = new javax.swing.JFrame("聊天客户端v0.1");
        BorderLayout b1=new BorderLayout();
        jf_chat.setLayout(b1);

        jf_chat.setSize(700, 600);
        jta_input.setPreferredSize(new Dimension(600,250));
        jta_input.setEditable(false);
        // 显示接收到的消息
        javax.swing.JLabel la = new JLabel("要发送的消息: ");
        la.setPreferredSize(new Dimension(600,20));
        la.setHorizontalTextPosition(JLabel.RIGHT);
        // 发送消息输入区
        final javax.swing.JTextArea jta_output = new JTextArea();
        jta_output.setPreferredSize(new Dimension(600,150));
        // 发送按钮
        JButton bu_send = new JButton("send");
        bu_send.addActionListener(new ActionListener() {
//            @Override
            public void actionPerformed(ActionEvent e) {
                // 实现在连结上的发送调用
                String msg = jta_output.getText();
                jta_output.setText("");
                conn.sendMsgToAll(msg);
                if (!conn.isConnected()) {
                    conn.CloseMe();
                    jf_chat.dispose();
                }
            }  
        });
        bu_send.setPreferredSize(new Dimension(80,40));


        jf_chat.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                String msg="#Close#";
                conn.sendCloseMsg(msg);
                jf_chat.dispose();
            }

        });

        jp_North.add(jta_input);
        jf_chat.add(jp_North,BorderLayout.NORTH);
        jp_South.add(la);
        jp_South.add(jta_output);
        jp_South.add(bu_send);
        jf_chat.add(jp_South,BorderLayout.CENTER);
        jf_chat.setVisible(true);
    }

    //显示好友界面
    public void showUI(){
        jf_list=new JFrame("好友列表");
        FlowLayout f1=new FlowLayout();
        jf_list.setLayout(f1);
        jf_list.setSize(200,500);

    }
    //显示注册界面
    public void showReUI(NetClient nc){
    	conn=nc;
        jf_re = new javax.swing.JFrame("用户注册");
        java.awt.FlowLayout fl = new java.awt.FlowLayout();
        jf_re.setLayout(fl);
        jf_re.setSize(200, 300);
        // 显示接收到的消息
        javax.swing.JLabel la = new JLabel("要发送的消息: ");
        // 发送消息输入区
        final javax.swing.JTextArea jta_output = new JTextArea(7, 25);
        // 发送按钮
        JButton bu_send = new JButton("注册");
        bu_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 实现在连结上的发送调用
                String msg = jta_output.getText();
                conn.sendMsgToAll(msg);
                if(!conn.isConnected()){
                    jf_re.dispose();
                }
            }
        });
        jf_re.add(jta_input);
        jf_re.add(la);
        jf_re.add(jta_output);
        jf_re.add(bu_send);
        jf_re.setVisible(true);
    }
}
