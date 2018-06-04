package goods.cap.app.goodsgoods.Model;

import goods.cap.app.goodsgoods.Model.Diet.Diet;

public class Recent {

    private String ctnno, imgUrl, summary, cntnt;
    private int flag;

    public Recent(){

    }
    public Recent(String ctnno, String imgUrl, String summary, String cntnt, int flag) {
        this.ctnno = ctnno;
        this.imgUrl = imgUrl;
        this.summary = summary;
        this.cntnt = cntnt;
        this.flag = flag;
    }

    public String getCtnno() {
        return ctnno;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSummary() {
        return summary;
    }

    public String getCntnt() {
        return cntnt;
    }

    public int getFlag() {
        return flag;
    }
}
