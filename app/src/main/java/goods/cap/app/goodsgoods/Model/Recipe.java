package goods.cap.app.goodsgoods.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Recipe {
    @SerializedName("ROW_NUM")
    @Expose
    private Integer row_num;//출력순서
    @SerializedName("RECIPE_ID")
    @Expose
    private Integer recipe_id;//레시피 코드
    @SerializedName("RECIPE_NM_KO")
    @Expose
    private String recipe_nm_ko;//레시피 이름
    @SerializedName("SUMRY")
    @Expose
    private String sumry;//간략(요약) 소개
    @SerializedName("NATION_CODE")
    @Expose
    private String nation_code;//유형코드
    @SerializedName("NATION_NM")
    @Expose
    private String nation_nm;//유형분류
    @SerializedName("TY_CODE")
    @Expose
    private String ty_code;	//음식분류코드
    @SerializedName("TY_NM")
    @Expose
    private String ty_nm;//음식분류
    @SerializedName("COOKING_TIME")
    @Expose
    private String cooking_time;//조리시간
    @SerializedName("CALORIE")
    @Expose
    private String calorie;//칼로리
    @SerializedName("QNT")
    @Expose
    private String qnt;//분량
    @SerializedName("LEVEL_NM")
    @Expose
    private String level_nm;//난이도
    @SerializedName("IRDNT_CODE")
    @Expose
    private String irdnt_code;//재료별 분류명
    @SerializedName("PC_NM")
    @Expose
    private String pc_nm;//가격별 분류
    @SerializedName("IMG_URL")
    @Expose
    private String img_url;//대표이미지 URL
    @SerializedName("DET_URL")
    @Expose
    private String det_url;//상세 URL

    public Recipe(){}

    public Recipe(Integer row_num, Integer recipe_id, String recipe_nm_ko, String sumry, String nation_code, String nation_nm, String ty_code, String ty_nm, String cooking_time, String calorie, String qnt,
                  String level_nm, String irdnt_code, String pc_nm, String img_url, String det_url) {
        this.row_num = row_num;
        this.recipe_id = recipe_id;
        this.recipe_nm_ko = recipe_nm_ko;
        this.sumry = sumry;
        this.nation_code = nation_code;
        this.nation_nm = nation_nm;
        this.ty_code = ty_code;
        this.ty_nm = ty_nm;
        this.cooking_time = cooking_time;
        this.calorie = calorie;
        this.qnt = qnt;
        this.level_nm = level_nm;
        this.irdnt_code = irdnt_code;
        this.pc_nm = pc_nm;
        this.img_url = img_url;
        this.det_url = det_url;
    }

    public Integer getRow_num() {
        return row_num;
    }

    public void setRow_num(Integer row_num) {
        this.row_num = row_num;
    }

    public Integer getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Integer recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getRecipe_nm_ko() {
        return recipe_nm_ko;
    }

    public void setRecipe_nm_ko(String recipe_nm_ko) {
        this.recipe_nm_ko = recipe_nm_ko;
    }

    public String getSumry() {
        return sumry;
    }

    public void setSumry(String sumry) {
        this.sumry = sumry;
    }

    public String getNation_code() {
        return nation_code;
    }

    public void setNation_code(String nation_code) {
        this.nation_code = nation_code;
    }

    public String getNation_nm() {
        return nation_nm;
    }

    public void setNation_nm(String nation_nm) {
        this.nation_nm = nation_nm;
    }

    public String getTy_code() {
        return ty_code;
    }

    public void setTy_code(String ty_code) {
        this.ty_code = ty_code;
    }

    public String getTy_nm() {
        return ty_nm;
    }

    public void setTy_nm(String ty_nm) {
        this.ty_nm = ty_nm;
    }

    public String getCooking_time() {
        return cooking_time;
    }

    public void setCooking_time(String cooking_time) {
        this.cooking_time = cooking_time;
    }

    public String getCalorie() {
        return calorie;
    }

    public void setCalorie(String calorie) {
        this.calorie = calorie;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }

    public String getLevel_nm() {
        return level_nm;
    }

    public void setLevel_nm(String level_nm) {
        this.level_nm = level_nm;
    }

    public String getIrdnt_code() {
        return irdnt_code;
    }

    public void setIrdnt_code(String irdnt_code) {
        this.irdnt_code = irdnt_code;
    }

    public String getPc_nm() {
        return pc_nm;
    }

    public void setPc_nm(String pc_nm) {
        this.pc_nm = pc_nm;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getDet_url() {
        return det_url;
    }

    public void setDet_url(String det_url) {
        this.det_url = det_url;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "row_num=" + row_num +
                ", recipe_id='" + recipe_id + '\'' +
                ", recipe_nm_ko='" + recipe_nm_ko + '\'' +
                ", sumry='" + sumry + '\'' +
                ", nation_code='" + nation_code + '\'' +
                ", nation_nm='" + nation_nm + '\'' +
                ", ty_code='" + ty_code + '\'' +
                ", ty_nm='" + ty_nm + '\'' +
                ", cooking_time=" + cooking_time +
                ", calorie=" + calorie +
                ", qnt=" + qnt +
                ", level_nm='" + level_nm + '\'' +
                ", irdnt_code='" + irdnt_code + '\'' +
                ", pc_nm='" + pc_nm + '\'' +
                ", img_url='" + img_url + '\'' +
                ", det_url='" + det_url + '\'' +
                '}';
    }
}
