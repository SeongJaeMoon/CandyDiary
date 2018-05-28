package goods.cap.app.goodsgoods.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Food {

    @SerializedName("ROW_NUM")
    @Expose
    private Integer rowNum; //출력순서
    @SerializedName("RECIPE_ID")
    @Expose
    private Integer recipeId; //레시피 코드
    @SerializedName("COOKING_NO")
    @Expose
    private Integer cookingNo; //요리설명순서
    @SerializedName("COOKING_DC")
    @Expose
    private String cookingDC; //요리설명
    @SerializedName("STRE_STEP_IMAGE_URL")
    @Expose
    private String stepImgURL; //과정 이미지 url
    @SerializedName("STEP_TIP")
    @Expose
    private String stepTIP; //과정팁

    public Food(){

    }

    public Food(Integer rowNum, Integer recipeId, Integer cookingNo, String cookingDC, String stepImgURL, String stepTIP) {
        this.rowNum = rowNum;
        this.recipeId = recipeId;
        this.cookingNo = cookingNo;
        this.cookingDC = cookingDC;
        this.stepImgURL = stepImgURL;
        this.stepTIP = stepTIP;
    }

    public Integer getRowNum() {
        return rowNum;
    }

    public void setRowNum(Integer rowNum) {
        this.rowNum = rowNum;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public Integer getCookingNo() {
        return cookingNo;
    }

    public void setCookingNo(Integer cookingNo) {
        this.cookingNo = cookingNo;
    }

    public String getCookingDC() {
        return cookingDC;
    }

    public void setCookingDC(String cookingDC) {
        this.cookingDC = cookingDC;
    }

    public String getStepImgURL() {
        return stepImgURL;
    }

    public void setStepImgURL(String stepImgURL) {
        this.stepImgURL = stepImgURL;
    }

    public String getStepTIP() {
        return stepTIP;
    }

    public void setStepTIP(String stepTIP) {
        this.stepTIP = stepTIP;
    }
}
