package app.cosmos.ghrealestatediary.DTO;


/************************************************그룹**************************************************/

public class Group {

    String groupName;
    String bossPhone;
    String groupNumber;
    int type;

    public Group(String groupNumber, String groupName, String bossPhone, int type) {
        this.groupName = groupName;
        this.bossPhone = bossPhone;
        this.groupNumber = groupNumber;
        this.type = type;
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

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
