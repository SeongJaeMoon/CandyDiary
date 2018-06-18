package goods.cap.app.goodsgoods.Model.Therapy;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict = false)
public class Therapy {

    @Element(name = "hbdcNm", data = true)
    private String hbdcNm; //생약명
    @Element(name = "imgUrl", data = true)
    private String imgUrl; //원본 이미지
    @Element(name = "thumbImgUrl", data = true)
    private String thumbImgUrl; //썸네일 이미지
    @Element(name = "cntntsSj", data = true)
    private String cntntsSj; //명칭
    @Element(name = "cntntsNo", data = true)
    private String cntntsNo; //컨텐츠 번호
    @Element(name = "bneNm", data = true)
    private String bneNm; //학명

    public String getHbdcNm() {
        return hbdcNm;
    }

    public void setHbdcNm(String hbdcNm) {
        this.hbdcNm = hbdcNm;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getThumbImgUrl() {
        return thumbImgUrl;
    }

    public void setThumbImgUrl(String thumbImgUrl) {
        this.thumbImgUrl = thumbImgUrl;
    }

    public String getCntntsSj() {
        return cntntsSj;
    }

    public void setCntntsSj(String cntntsSj) {
        this.cntntsSj = cntntsSj;
    }

    public String getCntntsNo() {
        return cntntsNo;
    }

    public void setCntntsNo(String cntntsNo) {
        this.cntntsNo = cntntsNo;
    }

    public String getBneNm() {
        return bneNm;
    }

    public void setBneNm(String bneNm) {
        this.bneNm = bneNm;
    }

    public Therapy(){}

    public Therapy(String cntntsNo, String imgUrl, String bneNm, String cntntsSj){
        this.cntntsNo = cntntsNo;
        this.imgUrl = imgUrl;
        this.bneNm = bneNm;
        this.cntntsSj = cntntsSj;
    }
    @Override
    public String toString() {
        return "Therapy{" +
                "hbdcNm='" + hbdcNm + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", thumbImgUrl='" + thumbImgUrl + '\'' +
                ", cntntsSj='" + cntntsSj + '\'' +
                ", cntntsNo='" + cntntsNo + '\'' +
                ", bneNm='" + bneNm + '\'' +
                '}';
    }
}
