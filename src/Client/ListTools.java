
package Client;

import java.util.ArrayList;
import java.util.List;

public class ListTools {

    private static List<NetClient>ncList=new ArrayList();

    private ListTools(){
    }

    /*
     *移除用户
     */
    public static void RemoveClient(String name){
        for(int i=ncList.size();i>0;i--){
            NetClient nc=ncList.get(i-1);
            if(nc.name.equals(name)){
                ncList.remove(i-1);
                break;
            }
        }
    }

    public static void RemoveClient(int index){
        ncList.remove(index);
    }

    /*
     *  添加用户
     */
    public static void addClient(NetClient nc){
        ncList.add(nc);
    }

    /*
     *  获取队列
     */
    public static List<NetClient> getAllClient(){
        return ncList;
    }
}
