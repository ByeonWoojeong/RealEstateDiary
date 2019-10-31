package app.cosmos.ghrealestatediary.DTO;

public class ShareBuilding {

    String idx;
    String lat;
    String lng;
    String name;
    String my;
    String status;
    String title;

    public ShareBuilding(String idx, String lat, String lng, String name, String my, String status, String title) {
        this.idx = idx;
        this.lat = lat;
        this.lng = lng;
        this.name = name;
        this.my = my;
        this.status = status;
        this.title = title;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMy() {
        return my;
    }

    public void setMy(String my) {
        this.my = my;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
