package Server;

import java.util.HashMap;
import java.util.Map;

public class DaoTools {
    //内存用户信息数据库
    private static Map<String, UserInfo>userDB=new HashMap();

    public void add(String name,String pwd){
        UserInfo user=new UserInfo();
        user.setName(name);
        user.setPwd(pwd);
        userDB.put(user.getName(),user);
    }

    public static boolean checkLogin(UserInfo user) {
        //验证用户名是否存在
        if(userDB.containsKey((user.getName()))){
            return true;
        }
        System.out.println("认证失败！:"+user.getName());
        return false;
    }

    static {
        for(int i=0;i<10;i++) {
            UserInfo user=new UserInfo();
            user.setName("user"+i);
            user.setPwd("pwd"+i);
            userDB.put(user.getName(), user);
        }
        UserInfo user=new UserInfo();
        user.setName("admin");
        user.setPwd("admin");
        userDB.put(user.getName(),user);
    }

}
