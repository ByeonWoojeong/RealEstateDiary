package app.cosmos.ghrealestatediary.DTO;


/************************************************멤버**************************************************/

public class Member {

    String name;
    String phone;
    String idx;
    int type;


    String groupNumber;

    public Member(String name, String phone, String idx, int type, String groupNumber) {
        this.name = name;
        this.phone = phone;
        this.idx = idx;
        this.type = type;
        this.groupNumber = groupNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(String groupNumber) {
        this.groupNumber = groupNumber;
    }

    //    public Member(String name, String phone, String idx, int type) {
//        this.name = name;
//        this.phone = phone;
//        this.idx = idx;
//        this.type = type;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
//
//    public String getIdx() {
//        return idx;
//    }
//
//    public void setIdx(String idx) {
//        this.idx = idx;
//    }
//
//    public int getType() {
//        return type;
//    }
//
//    public void setType(int type) {
//        this.type = type;
//    }
}
