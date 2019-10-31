package app.cosmos.ghrealestatediary.DTO;


public class Billing {

    String idx;
    String detail;
    String cost;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public Billing(String idx, String detail, String cost) {
        this.idx = idx;
        this.detail = detail;
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Billing{" +
                "idx='" + idx + '\'' +
                ", detail='" + detail + '\'' +
                ", cost='" + cost + '\'' +
                '}';
    }
}
