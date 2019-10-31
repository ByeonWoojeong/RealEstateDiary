package app.cosmos.ghrealestatediary.DTO;


public class Address {

    String name;
    boolean checkBox;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheckBox() {
        return checkBox;
    }

    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public Address(String name, boolean checkBox) {
        this.name = name;
        this.checkBox = checkBox;
    }

    @Override
    public String toString() {
        return "Address{" +
                "name='" + name + '\'' +
                ", checkBox=" + checkBox +
                '}';
    }
}
