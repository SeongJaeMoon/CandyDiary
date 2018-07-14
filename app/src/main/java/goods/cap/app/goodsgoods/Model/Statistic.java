package goods.cap.app.goodsgoods.Model;

public class Statistic{
    private String id;
    private String dietdate, names, whodiet, imgdiet, strtime, endtime;

    public Statistic(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDietdate() {
        return dietdate;
    }

    public void setDietdate(String dietdate) {
        this.dietdate = dietdate;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getWhodiet() {
        return whodiet;
    }

    public void setWhodiet(String whodiet) {
        this.whodiet = whodiet;
    }

    public String getImgdiet() {
        return imgdiet;
    }

    public void setImgdiet(String imgdiet) {
        this.imgdiet = imgdiet;
    }

    public String getStrtime() {
        return strtime;
    }

    public void setStrtime(String strtime) {
        this.strtime = strtime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "id=" + id +
                ", dietdate='" + dietdate + '\'' +
                ", names='" + names + '\'' +
                ", whodiet='" + whodiet + '\'' +
                ", imgdiet='" + imgdiet + '\'' +
                ", strtime='" + strtime + '\'' +
                ", endtime='" + endtime + '\'' +
                '}';
    }
}
