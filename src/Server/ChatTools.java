package Server;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class ChatTools {
    // 保存处理线程的队列对象
    private static List<ServerThread> stList = new ArrayList();

    private ChatTools() {
    }

    /*
     * 取得保存处理线程对象的队列
     */
    public static List<ServerThread> getAllThread() {
        return stList;
    }

    /*
     * 将一个客户对应的处理线程对象加入到队列中
     *
     * @param ct:处理线程对象
     */
    public static void addClient(ServerThread ct) {
        // 通知大家一下，有人上限了
        castMsg(ct.getOwerUser(), "我上线啦！！目前人数" + stList.size());
        stList.add(ct);
        SwingUtilities.updateComponentTreeUI(MainServerUI.table_onlineUser);
    }

    // 给队列中某一个用户发消息
    public static void sendMsg2One(int index, String msg) {
        stList.get(index).sendMsg2Me(msg);
    }

    // 根据表中选中索引，取得处理线程对象对应的用户对象
    public static UserInfo getUser(int index) {
        return stList.get(index).getOwerUser();
    }

    /*
     * 移除队列中指定位置的处理线程对象，界面踢人时调用
     *
     * @param index:要移除的位置
     */
    public static void removeClient(int index) {
        castMsg(stList.get(index).getOwerUser(),"A"+stList.get(index).getOwerUser().getName()+"被强制下线");
        stList.remove(index).closeMe();
    }

    /*
     * 从队列中移除一个用户对象对应的处理线程
     *
     * @param user:要移除的用户对象
     */
    public static void removeClient(UserInfo user) {
        System.out.println(user.getName());
        for (int i = stList.size(); i >=1; i--) {
            ServerThread ct = stList.get(i-1);
            System.out.println(ct.getOwerUser().getName());
            if(user.getName().equals(ct.getOwerUser().getName())) {
                System.out.println(user.getName()+"下线");
                stList.remove(i-1);
                ct.closeMe();
                castMsg(user, "A"+"我下线啦");
            }
        }
    }

    /*
     * 服务器关闭时，调用这个方法，移除，停止队列中所有处理线程对象
     *   上次改到这
     */
    public static void removeAllClient() {
        for (int i = stList.size(); i >0; i--) {
            ServerThread st = stList.get(i-1);
            st.sendMsg2Me("系统消息：服务器即将关闭");
            st.closeMe();
            stList.remove(i-1).closeMe();
            System.out.println("关闭了一个..." + i);
        }

    }

    /*
     * 将一条消息发送给队列中的其他客户机处理对象
     *
     * @param sender:发送者用户对象
     *
     * @param msg:要发送的消息内容
     *
     * 私聊形式@username#myname%msg
     * 注册形式！username pwd
     * 关闭形式#Close#
     */
    public static void castMsg(UserInfo sender, String msg) {
        System.out.println(msg);
        if(msg.charAt(0)=='@') {
            System.out.println("私聊");
            String s = msg.substring(msg.indexOf("@") + 1, msg.indexOf("#"));
            for (int i = 0; i < stList.size(); i++) {
                ServerThread st = stList.get(i);
                if (s.equals(st.getOwerUser().getName())) {
                    st.sendMsg2Me(msg.substring(msg.indexOf("#") + 1, msg.indexOf("%")) + "对你说：" + msg.substring(msg.indexOf("%") + 1));
                    System.out.println(msg.substring(msg.indexOf("#") + 1));
                    break;
                }
            }
        }else if(msg.charAt(0)=='!'){
            System.out.println("账号注册");
            String username=msg.substring(1,msg.indexOf(" "));
            String pwd=msg.substring(msg.indexOf(" ")+1);
            DaoTools at=new DaoTools();
            at.add(username,pwd);
        }
        else if(msg.charAt(0)=='A'){
            msg = sender.getName() + "说：" + msg.substring(1);
            System.out.println(msg);
            for (int i = 0; i < stList.size(); i++) {
                ServerThread st = stList.get(i);

                // ServerThread类中定义有一个将消息发送出去的方法
                st.sendMsg2Me(msg);// 发送给每一个客户机
            }
        }
        else if(msg.equals("#Close#")){
            String _msg="A"+sender.getName()+"说：下线了";
            System.out.println(_msg);
            for(int i=0;i<stList.size();i++){
                ServerThread st =stList.get(i);
                st.sendMsg2Me(_msg);
            }

        }
    }
}
