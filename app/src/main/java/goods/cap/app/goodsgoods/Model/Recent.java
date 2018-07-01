package goods.cap.app.goodsgoods.Model;

public class Recent {
    //고유 번호, 이미지 경로, 메인 설명, 상세 정보(태그 목록, 약초 효능)
    private String ctnno, imgUrl, summary, cntnt;
    //식단, 약초 구분
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
