package Server;

public class UserInfo {
    private String name;
    private String possward;
    private String loginTime;
    private String addres;

    public String getName(){
        return this.name;
    }
    public void setName(String name) {
        this.name=name;
    }

    public void setPwd(String possward) {
        this.possward=possward;
    }
}
