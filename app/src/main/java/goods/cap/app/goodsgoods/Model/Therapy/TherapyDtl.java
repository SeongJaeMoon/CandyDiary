package goods.cap.app.goodsgoods.Model.Therapy;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class TherapyDtl {
    @Element(name = "hbdcNm", data = true, required = false)
    private String hbdcNm; //생약명
    @Element(name = "imgUrl1", data = true, required = false)
    private String imgUrl1; //이미지1
    @Element(name = "imgUrl3", data = true, required = false)
    private String imgUrl3; //이미지3
    @Element(name = "imgUrl2", data = true, required = false)
    private String imgUrl2; //이미지2
    @Element(name = "imgUrl5", data = true, required = false)
    private String imgUrl5; //이미지5
    @Element(name = "imgUrl4", data = true, required = false)
    private String imgUrl4; //이미지4
    @Element(name = "cntntsSj", data = true, required = false)
    private String cntntsSj; //명칭
    @Element(name = "useeRegn", data = true, required = false)
    private String useeRegn; //이용부위
    @Element(name = "imgUrl6", data = true, required = false)
    private String imgUrl6; //이미지6
    @Element(name = "prvateTherpy", data = true, required = false)
    private String prvateTherpy; //민간요법
    @Element(name = "cntntsNo", data = true, required = false)
    private String cntntsNo; //컨텐츠 번호
    @Element(name = "bneNm", data = true, required = false)
    private String bneNm; //학명
    @Element(name = "stle", data = true, required = false)
    private String stle; //형태

    public String getHbdcNm() {
        return hbdcNm;
    }

    public void setHbdcNm(String hbdcNm) {
        this.hbdcNm = hbdcNm;
    }

    public String getImgUrl1() {
        return imgUrl1;
    }

    public void setImgUrl1(String imgUrl1) {
        this.imgUrl1 = imgUrl1;
    }

    public String getImgUrl3() {
        return imgUrl3;
    }

    public void setImgUrl3(String imgUrl3) {
        this.imgUrl3 = imgUrl3;
    }

    public String getImgUrl2() {
        return imgUrl2;
    }

    public void setImgUrl2(String imgUrl2) {
        this.imgUrl2 = imgUrl2;
    }

    public String getImgUrl5() {
        return imgUrl5;
    }

    public void setImgUrl5(String imgUrl5) {
        this.imgUrl5 = imgUrl5;
    }

    public String getImgUrl4() {
        return imgUrl4;
    }

    public void setImgUrl4(String imgUrl4) {
        this.imgUrl4 = imgUrl4;
    }

    public String getCntntsSj() {
        return cntntsSj;
    }

    public void setCntntsSj(String cntntsSj) {
        this.cntntsSj = cntntsSj;
    }

    public String getUseeRegn() {
        return useeRegn;
    }

    public void setUseeRegn(String useeRegn) {
        this.useeRegn = useeRegn;
    }

    public String getImgUrl6() {
        return imgUrl6;
    }

    public void setImgUrl6(String imgUrl6) {
        this.imgUrl6 = imgUrl6;
    }

    public String getPrvateTherpy() {
        return prvateTherpy;
    }

    public void setPrvateTherpy(String prvateTherpy) {
        this.prvateTherpy = prvateTherpy;
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

    public String getStle() {
        return stle;
    }

    public void setStle(String stle) {
        this.stle = stle;
    }

    @Override
    public String toString() {
        return "TherapyDtl{" +
                "hbdcNm='" + hbdcNm + '\'' +
                ", imgUrl1='" + imgUrl1 + '\'' +
                ", imgUrl3='" + imgUrl3 + '\'' +
                ", imgUrl2='" + imgUrl2 + '\'' +
                ", imgUrl5='" + imgUrl5 + '\'' +
                ", imgUrl4='" + imgUrl4 + '\'' +
                ", cntntsSj='" + cntntsSj + '\'' +
                ", useeRegn='" + useeRegn + '\'' +
                ", imgUrl6='" + imgUrl6 + '\'' +
                ", prvateTherpy='" + prvateTherpy + '\'' +
                ", cntntsNo='" + cntntsNo + '\'' +
                ", bneNm='" + bneNm + '\'' +
                ", stle='" + stle + '\'' +
                '}';
    }
}
