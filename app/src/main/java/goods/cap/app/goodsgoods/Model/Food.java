package goods.cap.app.goodsgoods.Model;

public class Food {

    private String fid; //음식 id
    private String foodName; //음식명
    private String foodDescripition; //설명
    private String detail; //상세설명

    public Food(){

    }

    public String getFid() { return fid; }

    public void setFid(String fid) { this.fid = fid; }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescripition() {
        return foodDescripition;
    }

    public void setFoodDescripition(String foodDescripition) { this.foodDescripition = foodDescripition; }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
