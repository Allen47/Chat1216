package Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

/*
 * 服务器端管理界面程序
 * 1.启/停
 * 2.发布公告消息
 * 3.显示在线用户信息
 * 4.踢人
 * 5.对某一个人发消息
 *
 */
public class MainServerUI {

    private ChatServer cserver;// 服务器对象
    private JFrame jf;// 管理界面
    static JTable table_onlineUser;// 在线用户表
    private JTextField jta_msg;// 发送消息输入框
    private JTextField jta_port;// 服务器端口号输入端
    private JButton bu_control_chat;// 启动服务器的按钮
    private JButton bu_refresh_chat;//刷新表格的按钮

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        MainServerUI mu = new MainServerUI();
        mu.showUI();
    }

    // 初始化界面
    public void showUI() {
        jf = new JFrame("javaKe服务器管理程序");
        jf.setSize(500, 300);
        FlowLayout f1 = new FlowLayout();
        jf.setLayout(f1);

        JLabel la_port = new JLabel("服务器端口:");
        jf.add(la_port);
        jta_port = new JTextField(4);
        jta_port.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                int keyChar = e.getKeyChar();
                if (keyChar >= KeyEvent.VK_0 && keyChar <= KeyEvent.VK_9&&jta_port.getText().length()<=4) {

                } else {
                    e.consume();// 屏蔽掉非法输入
                }
            }
        });
        jf.add(jta_port);
        bu_control_chat = new JButton("启动服务器");
        jf.add(bu_control_chat);
        //启动的事件监听器
        bu_control_chat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actionServer();
            }
        });

        bu_refresh_chat=new JButton("刷新");
        jf.add(bu_refresh_chat);
        bu_refresh_chat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                table_onlineUser.updateUI();
            }
        });

        JLabel la_msg = new JLabel("要发送的消息");
        jf.add(la_msg);
        // 服务器要发送消息的输入框
        jta_msg = new JTextField(30);
        // 定义一个监听器对 象：发送广播消息
        ActionListener sendCaseMsgAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendAllMsg();
            }
        };

        // 给输入框加航事件监听器，按回车时发送
        jta_msg.addActionListener(sendCaseMsgAction);
        JButton bu_send = new JButton("Send");
        // 给按钮加上发送广播消息的监听器
        bu_send.addActionListener(sendCaseMsgAction);
        jf.add(jta_msg);
        jf.add(bu_send);

        // 界面上用以显示在线用户列表的表格
        table_onlineUser = new JTable();
        // 创建我们自己的Model对象：创建时，传入处理线程列表
        List<ServerThread> sts = ChatTools.getAllThread();
        UserInfoTableModel utm = new UserInfoTableModel(sts);
        table_onlineUser.setModel(utm);
        // 将表格放到滚动面板对象上
        JScrollPane scrollPane = new JScrollPane(table_onlineUser);
        // 设定表格在面板上的大小
        table_onlineUser.setPreferredScrollableViewportSize(new Dimension(400, 100));
        // 超出大小后，JScrollPane自动出现滚动条
        scrollPane.setAutoscrolls(true);
        jf.add(scrollPane);// 将scrollPane对象加到界面上

        // 取得表格上的弹出菜单对象,加到表格上
        JPopupMenu pop = getTablePop();
        table_onlineUser.setComponentPopupMenu(pop);
        jf.setVisible(true);
        jf.setDefaultCloseOperation(3);// 界面关闭时，程序退出

    }

    /*
     * 创建表格上的弹出菜单对象，实现发信，踢人功能
     */
    private JPopupMenu getTablePop() {
        JPopupMenu pop = new JPopupMenu();// 弹出菜单对象
        JMenuItem mi_send = new JMenuItem("发信");
        ;// 菜单项对象
        mi_send.setActionCommand("send");// 设定菜单命令关键字
        JMenuItem mi_del = new JMenuItem("踢掉");// 菜单项对象
        mi_del.setActionCommand("del");// 设定菜单命令关键字
        // 弹出菜单上的事件监听器对象
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = e.getActionCommand();
                // 哪个菜单项点击了，这个s就是其设定的ActionCommand
                popMenuAction(s);
            }
        };
        mi_send.addActionListener(al);
        mi_del.addActionListener(al);// 给菜单加上监听器
        pop.add(mi_send);
        pop.add(mi_del);
        return pop;
    }

    // 处理弹出菜单上的事件
    private void popMenuAction(String command) {
        // 得到在表格上选中的行
        final int selectIndex = table_onlineUser.getSelectedRow();
        if (selectIndex == -1) {
            JOptionPane.showMessageDialog(jf, "请选中一个用户");
            return;
        }
        if (command.equals("del")) {
            // 从线程中移除处理线程对象
            ChatTools.removeClient(selectIndex);
        } else if (command.equals("send")) {
            UserInfo user = ChatTools.getUser(selectIndex);
            final JDialog jd = new JDialog(jf, true);// 发送对话框
            jd.setLayout(new FlowLayout());
            jd.setTitle("您将对" + user.getName() + "发信息");
            jd.setSize(200, 100);
            final JTextField jtd_m = new JTextField(20);
            JButton jb = new JButton("发送!");
            jd.add(jtd_m);
            jd.add(jb);
            // 发送按钮的事件实现
            jb.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("发送了一条消息啊...");
                    String msg = "系统悄悄说:" + jtd_m.getText();
                    ChatTools.sendMsg2One(selectIndex, msg);
                    jtd_m.setText("");// 清空输入框
                    jd.dispose();
                }
            });
            jd.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(jf, "未知菜单:" + command);
        }
        // 刷新表格
        SwingUtilities.updateComponentTreeUI(table_onlineUser);
    }

    // 按下发送服务器消息的按钮，给所有在线用户发送消息
    private void sendAllMsg() {
        String msg = jta_msg.getText();// 得到输入框的消息
        UserInfo user = new UserInfo();
        user.setName("系统");
        user.setPwd("pwd");
        ChatTools.castMsg(user, msg); // 发送
        jta_msg.setText("");// 清空输入框

    }

    // 响应启动/停止按钮事件
    private void actionServer() {
        // 1.要得到服务器状态
        if (null == cserver) {
            String sPort = jta_port.getText();// 得到输入的端口
            int port = Integer.parseInt(sPort);
            cserver = new ChatServer(port);
            cserver.start();
            jf.setTitle("QQ服务器管理程序 :正在运行中");
            bu_control_chat.setText("Stop!");
        } else if (cserver.isRunning()) {// 己经在运行
            // 清除所有己在运行的客户端处理线程
            ChatTools.removeAllClient();
            cserver.stopchatServer();
            cserver = null;
            jf.setTitle("QQ服务器管理程序 :己停止");
            SwingUtilities.updateComponentTreeUI(table_onlineUser);
            bu_control_chat.setText("Start!");
        }

    }
}
