package goods.cap.app.goodsgoods.Model;


public class Recipe {

    private int row_num;//출력순서
    private String recipe_id;//레시피 코드
    private String irdnt_sn; //재료순번
    private String irdnt_nm; //재료명
    private int irdnt_cpcty; //재료용량
    private String irdnt_ty_code; //재료타입 코드
    private String irdnt_ty_nm; //재료타입명

    public Recipe(){

    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getIrdnt_sn() {
        return irdnt_sn;
    }

    public void setIrdnt_sn(String irdnt_sn) {
        this.irdnt_sn = irdnt_sn;
    }

    public String getIrdnt_nm() {
        return irdnt_nm;
    }

    public void setIrdnt_nm(String irdnt_nm) {
        this.irdnt_nm = irdnt_nm;
    }

    public int getIrdnt_cpcty() {
        return irdnt_cpcty;
    }

    public void setIrdnt_cpcty(int irdnt_cpcty) {
        this.irdnt_cpcty = irdnt_cpcty;
    }

    public String getIrdnt_ty_code() {
        return irdnt_ty_code;
    }

    public void setIrdnt_ty_code(String irdnt_ty_code) {
        this.irdnt_ty_code = irdnt_ty_code;
    }

    public String getIrdnt_ty_nm() {
        return irdnt_ty_nm;
    }

    public void setIrdnt_ty_nm(String irdnt_ty_nm) {
        this.irdnt_ty_nm = irdnt_ty_nm;
    }

    public int getRow_num() {
        return row_num;
    }

    public void setRow_num(int row_num) {
        this.row_num = row_num;
    }

}
