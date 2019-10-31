package app.cosmos.ghrealestatediary.DTO;

public class Payment {

    private String pay_date;
    private String pay_price;

    public Payment(String pay_date, String pay_price) {
        this.pay_date = pay_date;
        this.pay_price = pay_price;
    }

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }
}
