package app.cosmos.ghrealestatediary.DTO;


public class Consult {

    String idx;
    String name;

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Consult(String idx, String name) {
        this.idx = idx;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Consult{" +
                "idx='" + idx + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
