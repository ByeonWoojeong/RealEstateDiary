package app.cosmos.ghrealestatediary.DTO;

public class FaQ {

    public String title;
    public String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public FaQ(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public String toString() {
        return "FaQ{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
