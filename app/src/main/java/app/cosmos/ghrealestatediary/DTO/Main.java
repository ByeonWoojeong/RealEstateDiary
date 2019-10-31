package app.cosmos.ghrealestatediary.DTO;


public class Main {

    String idx;
    String la;
    String lo;
    String title;
    String area1;
    String area2;
    String my;
    String distance;
    String status;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getLa() {
        return la;
    }

    public void setLa(String la) {
        this.la = la;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArea1() {
        return area1;
    }

    public void setArea1(String area1) {
        this.area1 = area1;
    }

    public String getArea2() {
        return area2;
    }

    public void setArea2(String area2) {
        this.area2 = area2;
    }

    public String getMy() {
        return my;
    }

    public void setMy(String my) {
        this.my = my;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Main(String idx, String la, String lo, String title, String area1, String area2, String my, String distance, String status) {
        this.idx = idx;
        this.la = la;
        this.lo = lo;
        this.title = title;
        this.area1 = area1;
        this.area2 = area2;
        this.my = my;
        this.distance = distance;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Main{" +
                "idx='" + idx + '\'' +
                ", la='" + la + '\'' +
                ", lo='" + lo + '\'' +
                ", title='" + title + '\'' +
                ", area1='" + area1 + '\'' +
                ", area2='" + area2 + '\'' +
                ", my='" + my + '\'' +
                ", distance='" + distance + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
