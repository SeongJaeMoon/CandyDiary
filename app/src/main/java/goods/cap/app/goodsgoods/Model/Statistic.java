package goods.cap.app.goodsgoods.Model;

public class Statistic{
    private String date;
    private String brkfast, lunch, dinner, snack, dessert;
    private double total_cal;
    private String whobrkfast, imgbrkfast, wholunch, imglunch, whodinner, imgdinner, whosnack, imgsnack, whodessert, imgdessert;
    private String brkstrtime, brkendtime, lchstrtime, lchendtime, dinstrtime, dinendtime, snkstrtime, snkendtime, desstrtime, desendtime;

    public Statistic(){

    }

    public String getWhobrkfast() {
        return whobrkfast;
    }

    public void setWhobrkfast(String whobrkfast) {
        this.whobrkfast = whobrkfast;
    }

    public String getImgbrkfast() {
        return imgbrkfast;
    }

    public void setImgbrkfast(String imgbrkfast) {
        this.imgbrkfast = imgbrkfast;
    }

    public String getWholunch() {
        return wholunch;
    }

    public void setWholunch(String wholunch) {
        this.wholunch = wholunch;
    }

    public String getImglunch() {
        return imglunch;
    }

    public void setImglunch(String imglaunch) {
        this.imglunch = imglaunch;
    }

    public String getWhodinner() {
        return whodinner;
    }

    public void setWhodinner(String whodinner) {
        this.whodinner = whodinner;
    }

    public String getImgdinner() {
        return imgdinner;
    }

    public void setImgdinner(String imgdinner) {
        this.imgdinner = imgdinner;
    }

    public String getWhosnack() {
        return whosnack;
    }

    public void setWhosnack(String whosnack) {
        this.whosnack = whosnack;
    }

    public String getImgsnack() {
        return imgsnack;
    }

    public void setImgsnack(String imgsnack) {
        this.imgsnack = imgsnack;
    }

    public String getWhodessert() {
        return whodessert;
    }

    public void setWhodessert(String whodessert) {
        this.whodessert = whodessert;
    }

    public String getImgdessert() {
        return imgdessert;
    }

    public void setImgdessert(String imgdessert) {
        this.imgdessert = imgdessert;
    }

    public String getBrkstrtime() {
        return brkstrtime;
    }

    public void setBrkstrtime(String brkstrtime) {
        this.brkstrtime = brkstrtime;
    }

    public String getBrkendtime() {
        return brkendtime;
    }

    public void setBrkendtime(String brkendtime) {
        this.brkendtime = brkendtime;
    }

    public String getLchstrtime() {
        return lchstrtime;
    }

    public void setLchstrtime(String lchstrtime) {
        this.lchstrtime = lchstrtime;
    }

    public String getLchendtime() {
        return lchendtime;
    }

    public void setLchendtime(String lchendtime) {
        this.lchendtime = lchendtime;
    }

    public String getDinstrtime() {
        return dinstrtime;
    }

    public void setDinstrtime(String dinstrtime) {
        this.dinstrtime = dinstrtime;
    }

    public String getDinendtime() {
        return dinendtime;
    }

    public void setDinendtime(String dinendtime) {
        this.dinendtime = dinendtime;
    }

    public String getSnkstrtime() {
        return snkstrtime;
    }

    public void setSnkstrtime(String snkstrtime) {
        this.snkstrtime = snkstrtime;
    }

    public String getSnkendtime() {
        return snkendtime;
    }

    public void setSnkendtime(String snkendtime) {
        this.snkendtime = snkendtime;
    }

    public String getDesstrtime() {
        return desstrtime;
    }

    public void setDesstrtime(String desstrtime) {
        this.desstrtime = desstrtime;
    }

    public String getDesendtime() {
        return desendtime;
    }

    public void setDesendtime(String desendtime) {
        this.desendtime = desendtime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBrkfast() {
        return brkfast;
    }

    public void setBrkfast(String brkfast) {
        this.brkfast = brkfast;
    }

    public String getLunch() {
        return lunch;
    }

    public void setLaunch(String launch) {
        this.lunch = launch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public String getSnack() {
        return snack;
    }

    public void setSnack(String snack) {
        this.snack = snack;
    }

    public String getDessert() {
        return dessert;
    }

    public void setDessert(String dessert) {
        this.dessert = dessert;
    }

    public double getTotal_cal() {
        return total_cal;
    }

    public void setTotal_cal(double total_cal) {
        this.total_cal = total_cal;
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "date='" + date + '\'' +
                ", brkfast=" + brkfast +
                ", launch=" + lunch +
                ", dinner=" + dinner +
                ", snack=" + snack +
                ", dessert=" + dessert +
                ", total_cal=" + total_cal +
                ", whobrkfast='" + whobrkfast + '\'' +
                ", imgbrkfast='" + imgbrkfast + '\'' +
                ", wholunch='" + wholunch + '\'' +
                ", imglunch='" + imglunch + '\'' +
                ", whodinner='" + whodinner + '\'' +
                ", imgdinner='" + imgdinner + '\'' +
                ", whosnack='" + whosnack + '\'' +
                ", imgsnack='" + imgsnack + '\'' +
                ", whodessert='" + whodessert + '\'' +
                ", imgdessert='" + imgdessert + '\'' +
                ", brkstrtime='" + brkstrtime + '\'' +
                ", brkendtime='" + brkendtime + '\'' +
                ", lchstrtime='" + lchstrtime + '\'' +
                ", lchendtime='" + lchendtime + '\'' +
                ", dinstrtime='" + dinstrtime + '\'' +
                ", dinendtime='" + dinendtime + '\'' +
                ", snkstrtime='" + snkstrtime + '\'' +
                ", snkendtime='" + snkendtime + '\'' +
                ", desstrtime='" + desstrtime + '\'' +
                ", desendtime='" + desendtime + '\'' +
                '}';
    }
}
