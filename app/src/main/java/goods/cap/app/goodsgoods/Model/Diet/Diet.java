package goods.cap.app.goodsgoods.Model.Diet;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict = false)
public class Diet {

    @Element(name = "cntntsChargerEsntlNm", data = true)
    private String cntntsChargerEsntlNm; //담당자 명

    @Element(name = "cntntsNo", data = true)
    private String cntntsNo; //컨텐츠 번호

    @Element(name = "cntntsRdcnt", data = true)
    private String cntntsRdcnt; //조회수

    @Element(name = "cntntsSj", data = true)
    private String cntntsSj; //컨텐츠 제목

    @Element(name = "dietNm", data = true)
    private String dietNm; //식단 명

    @Element(name = "fdNm", data = true)
    private String fdNm; //음식 명

    @Element(name = "registDt", data = true)
    private String registDt; //등록 일시

    @Element(name = "rtnFileSeCode",  data = true)
    private String rtnFileSeCode; //파일 구분 코드

    @Element(name = "rtnFileSn", data = true)
    private String rtnFileSn; //파일 순번

    @Element(name = "rtnImageDc", data = true)
    private String rtnImageDc; //이미지 설명

    @Element(name = "rtnImgSeCode", data = true)
    private String rtnImgSeCode; //이미지 구분 코드

    @Element(name = "rtnOrginlFileNm", data = true)
    private String rtnOrginlFileNm; //원본 파일명

    @Element(name = "rtnStreFileNm", data = true)
    private String rtnStreFileNm; //저장 파일명

    @Element(name = "rtnThumbFileNm", data = true)
    private String rtnThumbFileNm; //썸네일 파일명

    private String filePath;

    public String getCntntsChargerEsntlNm() {
        return cntntsChargerEsntlNm;
    }

    public void setCntntsChargerEsntlNm(String cntntsChargerEsntlNm) {
        this.cntntsChargerEsntlNm = cntntsChargerEsntlNm;
    }

    //for Recent
    public Diet(){

    }

    public Diet(String cntntsNo, String filePath, String fdNm, String cntntsSj){
        this.cntntsNo = cntntsNo;
        this.filePath = filePath;
        this.fdNm = fdNm;
        this.cntntsSj = cntntsSj;
    }

    public String getCntntsNo() {
        return cntntsNo;
    }

    public void setCntntsNo(String cntntsNo) {
        this.cntntsNo = cntntsNo;
    }

    public String getCntntsRdcnt() {
        return cntntsRdcnt;
    }

    public void setCntntsRdcnt(String cntntsRdcnt) {
        this.cntntsRdcnt = cntntsRdcnt;
    }

    public String getCntntsSj() {
        return cntntsSj;
    }

    public void setCntntsSj(String cntntsSj) {
        this.cntntsSj = cntntsSj;
    }

    public String getDietNm() {
        return dietNm;
    }

    public void setDietNm(String dietNm) {
        this.dietNm = dietNm;
    }

    public String getFdNm() {
        return fdNm;
    }

    public void setFdNm(String fdNm) {
        this.fdNm = fdNm;
    }

    public String getRegistDt() {
        return registDt;
    }

    public void setRegistDt(String registDt) {
        this.registDt = registDt;
    }

    public String getRtnFileSeCode() {
        return rtnFileSeCode;
    }

    public void setRtnFileSeCode(String rtnFileSeCode) {
        this.rtnFileSeCode = rtnFileSeCode;
    }

    public String getRtnFileSn() {
        return rtnFileSn;
    }

    public void setRtnFileSn(String rtnFileSn) {
        this.rtnFileSn = rtnFileSn;
    }

    public String getRtnImageDc() {
        return rtnImageDc;
    }

    public void setRtnImageDc(String rtnImageDc) {
        this.rtnImageDc = rtnImageDc;
    }

    public String getRtnImgSeCode() {
        return rtnImgSeCode;
    }

    public void setRtnImgSeCode(String rtnImgSeCode) {
        this.rtnImgSeCode = rtnImgSeCode;
    }

    public String getRtnOrginlFileNm() {
        return rtnOrginlFileNm;
    }

    public void setRtnOrginlFileNm(String rtnOrginlFileNm) {
        this.rtnOrginlFileNm = rtnOrginlFileNm;
    }

    public String getRtnStreFileNm() {
        return rtnStreFileNm;
    }

    public void setRtnStreFileNm(String rtnStreFileNm) {
        this.rtnStreFileNm = rtnStreFileNm;
    }

    public String getRtnThumbFileNm() {
        return rtnThumbFileNm;
    }

    public void setRtnThumbFileNm(String rtnThumbFileNm) {
        this.rtnThumbFileNm = rtnThumbFileNm;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Diet{" +
                "cntntsChargerEsntlNm='" + cntntsChargerEsntlNm + '\'' +
                ", cntntsNo=" + cntntsNo +
                ", cntntsRdcnt=" + cntntsRdcnt +
                ", cntntsSj='" + cntntsSj + '\'' +
                ", dietNm='" + dietNm + '\'' +
                ", fdNm='" + fdNm + '\'' +
                ", registDt='" + registDt + '\'' +
                ", rtnFileSeCode=" + rtnFileSeCode +
                ", rtnFileSn=" + rtnFileSn +
                ", rtnImageDc='" + rtnImageDc + '\'' +
                ", rtnImgSeCode=" + rtnImgSeCode +
                ", rtnOrginlFileNm='" + rtnOrginlFileNm + '\'' +
                ", rtnStreFileNm='" + rtnStreFileNm + '\'' +
                ", rtnThumbFileNm='" + rtnThumbFileNm + '\'' +
                '}';
    }
}
