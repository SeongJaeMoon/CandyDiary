package goods.cap.app.goodsgoods.Model.Firebase;

public class Calorie {
    private String id;
    private String date;
    private String cateorgy;
    private String name;
    private double kal;
    private Object cho_mg;
    private Object dan_g;
    private Object dang_g;
    private Object ji_g;
    private Object na_mg;
    private Object pho_g;
    private Object quanty;
    private Object tan_g;
    private Object trans_g;

    public Calorie(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCateorgy() {
        return cateorgy;
    }

    public void setCateorgy(String cateorgy) {
        this.cateorgy = cateorgy;
    }

    public double getKal() {
        return kal;
    }

    public void setKal(double kal) {
        this.kal = kal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCho_mg() {
        return cho_mg;
    }

    public void setCho_mg(Object cho_mg) {
        this.cho_mg = cho_mg;
    }

    public Object getDan_g() {
        return dan_g;
    }

    public void setDan_g(Object dan_g) {
        this.dan_g = dan_g;
    }

    public Object getDang_g() {
        return dang_g;
    }

    public void setDang_g(Object dang_g) {
        this.dang_g = dang_g;
    }

    public Object getJi_g() {
        return ji_g;
    }

    public void setJi_g(Object ji_g) {
        this.ji_g = ji_g;
    }

    public Object getNa_mg() {
        return na_mg;
    }

    public void setNa_mg(Object na_mg) {
        this.na_mg = na_mg;
    }

    public Object getPho_g() {
        return pho_g;
    }

    public void setPho_g(Object pho_g) {
        this.pho_g = pho_g;
    }

    public Object getQuanty() {
        return quanty;
    }

    public void setQuanty(Object quanty) {
        this.quanty = quanty;
    }

    public Object getTan_g() {
        return tan_g;
    }

    public void setTan_g(Object tan_g) {
        this.tan_g = tan_g;
    }

    public Object getTrans_g() {
        return trans_g;
    }

    public void setTrans_g(Object trans_g) {
        this.trans_g = trans_g;
    }

    @Override
    public String toString() {
        return "Calorie{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", cateorgy='" + cateorgy + '\'' +
                ", cho_mg=" + cho_mg +
                ", dan_g=" + dan_g +
                ", dang_g=" + dang_g +
                ", ji_g=" + ji_g +
                ", kal=" + kal +
                ", na_mg=" + na_mg +
                ", name='" + name + '\'' +
                ", pho_g=" + pho_g +
                ", quanty=" + quanty +
                ", tan_g=" + tan_g +
                ", trans_g=" + trans_g +
                '}';
    }
}
