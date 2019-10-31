package app.cosmos.ghrealestatediary.DTO;


public class GroupInvite {

    String groupNumber;
    String groupName;
    String bossPhone;
    int type;

    public GroupInvite(String groupNumber, String groupName, String bossPhone, int type) {
        this.groupNumber = groupNumber;
        this.groupName = groupName;
        this.bossPhone = bossPhone;
        this.type = type;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBossPhone() {
        return bossPhone;
    }

    public void setBossPhone(String bossPhone) {
        this.bossPhone = bossPhone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
