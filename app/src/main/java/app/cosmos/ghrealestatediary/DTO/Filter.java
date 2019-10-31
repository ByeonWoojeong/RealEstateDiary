package app.cosmos.ghrealestatediary.DTO;



public class Filter {
    String filter_title;
    String start;
    String end;


    public Filter(String filter_title, String start, String end) {
        this.filter_title = filter_title;
        this.start = start;
        this.end = end;
    }

    public String getFilter_title() {
        return filter_title;
    }

    public void setFilter_title(String filter_title) {
        this.filter_title = filter_title;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
