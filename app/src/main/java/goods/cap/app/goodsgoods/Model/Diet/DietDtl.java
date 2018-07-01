package goods.cap.app.goodsgoods.Model.Diet;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;

@Root(name="item", strict = false)
public class DietDtl {

    @Element(name = "ntrfsInfo", data = true, required = false)
    private String ntrfsInfo; //지질 정보
    @Element(name = "ckngMthInfo", data = true, required = false)
    private String ckngMthInfo; //조리 방법 정보
    @Element(name = "crfbInfo", data = true, required = false)
    private String crfbInfo; //조섬요 정보
    @Element(name = "vtmbInfo", data = true, required = false)
    private String vtmbInfo; //비타민 B 정보
    @Element(name = "cntntsChargerEsntlNm", data = true, required = false)
    private String cntntsChargerEsntlNm; //담당자 명
    @Element(name = "fdNm", data = true, required = false)
    private String fdNm; //음식
    @Element(name = "clciInfo", data = true, required = false)
    private String clciInfo; // 칼슘 정보
    @Element(name = "rtnThumbFileNm", data = true, required = false)
    private String rtnThumbFileNm; //썸네일 파일명
    @Element(name = "fdInfoFirst", data = true, required = false)
    private String fdInfoFirst; //음식 순서
    @Element(name = "vtmcInfo", data = true, required = false)
    private String vtmcInfo; //비타민 C 정보
    @Element(name = "matrlInfo", data = true, required = false)
    private String matrlInfo; //재료 정보
    @Element(name = "rtnImageDc", data = true, required = false)
    private String rtnImageDc; //이미지 설명
    @Element(name = "dietNm", data = true, required = false)
    private String dietNm; //식단 명
    @Element(name = "naInfo", data = true, required = false)
    private String naInfo; //나트륨 정
    @Element(name = "ircnInfo", data = true, required = false)
    private String ircnInfo; //철분 정보
    @Element(name = "rtnFileSn", data = true, required = false)
    private String rtnFileSn; //파일 순번
    @Element(name = "rtnFileCours", data = true, required = false)
    private String rtnFileCours; //파일 경로
    @Element(name = "crbhInfo", data = true, required = false)
    private String crbhInfo; //당질 정보
    @Element(name = "dietDtlNm", data = true, required = false)
    private String dietDtlNm; //식단 상세명
    @Element(name = "dietCn", data = true, required = false)
    private String dietCn; //식단 내용
    @Element(name = "chlsInfo", data = true, required = false)
    private String chlsInfo; //콜레스테롤 정보
    @Element(name = "rtnFileSeCode", data = true, required = false)
    private String rtnFileSeCode; //파일 구분 코드
    @Element(name = "cntntsRdcnt", data = true, required = false)
    private String cntntsRdcnt; //조회수
    @Element(name = "clriInfo", data = true, required = false)
    private String clriInfo; //칼로리 정보
    @Element(name = "cntntsNo", data = true, required = false)
    private String cntntsNo; //컨텐츠 번
    @Element(name = "frmlasaltEqvlntqyInfo", data = true, required = false)
    private String frmlasaltEqvlntqyInfo; //식염 상당량 정보
    @Element(name = "vtmaInfo", data = true, required = false)
    private String vtmaInfo; //비타민 A 정보
    @Element(name = "fdInfo", data = true, required = false)
    private String fdInfo; //음식 정보
    @Element(name = "rtnStreFileNm", data = true, required = false)
    private String rtnStreFileNm; //저장파일명
    @Element(name = "rtnImgSeCode", data = true, required = false)
    private String rtnImgSeCode; //이미지 구분 코드
    @Element(name = "cntntsSj", data = true, required = false)
    private String cntntsSj; //컨텐츠 제목
    @Element(name = "ntkQyInfo", data = true, required = false)
    private String ntkQyInfo; //섭취량 정보
    @Element(name = "dietNtrsmallInfo", data = true, required = false)
    private String dietNtrsmallInfo; //식단 영양소 정보
    @Element(name = "rtnOrginlFileNm", data = true, required = false)
    private String rtnOrginlFileNm; //원본 파일명
    @Element(name = "fdCntntsNo", data = true, required = false)
    private String fdCntntsNo; //음식 컨텐츠 번
    @Element(name = "protInfo", data = true, required = false)
    private String protInfo; //단백 정보
    @Element(name = "registDt", data = true, required = false)
    private String registDt; //등록 일시

    public String getNtrfsInfo() {
        return ntrfsInfo;
    }

    public void setNtrfsInfo(String ntrfsInfo) {
        this.ntrfsInfo = ntrfsInfo;
    }

    public String getCkngMthInfo() {
        return ckngMthInfo;
    }

    public void setCkngMthInfo(String ckngMthInfo) {
        this.ckngMthInfo = ckngMthInfo;
    }

    public String getCrfbInfo() {
        return crfbInfo;
    }

    public void setCrfbInfo(String crfbInfo) {
        this.crfbInfo = crfbInfo;
    }

    public String getVtmbInfo() {
        return vtmbInfo;
    }

    public void setVtmbInfo(String vtmbInfo) {
        this.vtmbInfo = vtmbInfo;
    }

    public String getCntntsChargerEsntlNm() {
        return cntntsChargerEsntlNm;
    }

    public void setCntntsChargerEsntlNm(String cntntsChargerEsntlNm) {
        this.cntntsChargerEsntlNm = cntntsChargerEsntlNm;
    }

    public String getFdNm() {
        return fdNm;
    }

    public void setFdNm(String fdNm) {
        this.fdNm = fdNm;
    }

    public String getClciInfo() {
        return clciInfo;
    }

    public void setClciInfo(String clciInfo) {
        this.clciInfo = clciInfo;
    }

    public String getRtnThumbFileNm() {
        return rtnThumbFileNm;
    }

    public void setRtnThumbFileNm(String rtnThumbFileNm) {
        this.rtnThumbFileNm = rtnThumbFileNm;
    }

    public String getFdInfoFirst() {
        return fdInfoFirst;
    }

    public void setFdInfoFirst(String fdInfoFirst) {
        this.fdInfoFirst = fdInfoFirst;
    }

    public String getVtmcInfo() {
        return vtmcInfo;
    }

    public void setVtmcInfo(String vtmcInfo) {
        this.vtmcInfo = vtmcInfo;
    }

    public String getMatrlInfo() {
        return matrlInfo;
    }

    public void setMatrlInfo(String matrlInfo) {
        this.matrlInfo = matrlInfo;
    }

    public String getRtnImageDc() {
        return rtnImageDc;
    }

    public void setRtnImageDc(String rtnImageDc) {
        this.rtnImageDc = rtnImageDc;
    }

    public String getDietNm() {
        return dietNm;
    }

    public void setDietNm(String dietNm) {
        this.dietNm = dietNm;
    }

    public String getNaInfo() {
        return naInfo;
    }

    public void setNaInfo(String naInfo) {
        this.naInfo = naInfo;
    }

    public String getIrcnInfo() {
        return ircnInfo;
    }

    public void setIrcnInfo(String ircnInfo) {
        this.ircnInfo = ircnInfo;
    }

    public String getRtnFileSn() {
        return rtnFileSn;
    }

    public void setRtnFileSn(String rtnFileSn) {
        this.rtnFileSn = rtnFileSn;
    }

    public String getRtnFileCours() {
        return rtnFileCours;
    }

    public void setRtnFileCours(String rtnFileCours) {
        this.rtnFileCours = rtnFileCours;
    }

    public String getCrbhInfo() {
        return crbhInfo;
    }

    public void setCrbhInfo(String crbhInfo) {
        this.crbhInfo = crbhInfo;
    }

    public String getDietDtlNm() {
        return dietDtlNm;
    }

    public void setDietDtlNm(String dietDtlNm) {
        this.dietDtlNm = dietDtlNm;
    }

    public String getDietCn() {
        return dietCn;
    }

    public void setDietCn(String dietCn) {
        this.dietCn = dietCn;
    }

    public String getChlsInfo() {
        return chlsInfo;
    }

    public void setChlsInfo(String chlsInfo) {
        this.chlsInfo = chlsInfo;
    }

    public String getRtnFileSeCode() {
        return rtnFileSeCode;
    }

    public void setRtnFileSeCode(String rtnFileSeCode) {
        this.rtnFileSeCode = rtnFileSeCode;
    }

    public String getCntntsRdcnt() {
        return cntntsRdcnt;
    }

    public void setCntntsRdcnt(String cntntsRdcnt) {
        this.cntntsRdcnt = cntntsRdcnt;
    }

    public String getClriInfo() {
        return clriInfo;
    }

    public void setClriInfo(String clriInfo) {
        this.clriInfo = clriInfo;
    }

    public String getCntntsNo() {
        return cntntsNo;
    }

    public void setCntntsNo(String cntntsNo) {
        this.cntntsNo = cntntsNo;
    }

    public String getFrmlasaltEqvlntqyInfo() {
        return frmlasaltEqvlntqyInfo;
    }

    public void setFrmlasaltEqvlntqyInfo(String frmlasaltEqvlntqyInfo) {
        this.frmlasaltEqvlntqyInfo = frmlasaltEqvlntqyInfo;
    }

    public String getVtmaInfo() {
        return vtmaInfo;
    }

    public void setVtmaInfo(String vtmaInfo) {
        this.vtmaInfo = vtmaInfo;
    }

    public String getFdInfo() {
        return fdInfo;
    }

    public void setFdInfo(String fdInfo) {
        this.fdInfo = fdInfo;
    }

    public String getRtnStreFileNm() {
        return rtnStreFileNm;
    }

    public void setRtnStreFileNm(String rtnStreFileNm) {
        this.rtnStreFileNm = rtnStreFileNm;
    }

    public String getRtnImgSeCode() {
        return rtnImgSeCode;
    }

    public void setRtnImgSeCode(String rtnImgSeCode) {
        this.rtnImgSeCode = rtnImgSeCode;
    }

    public String getCntntsSj() {
        return cntntsSj;
    }

    public void setCntntsSj(String cntntsSj) {
        this.cntntsSj = cntntsSj;
    }

    public String getNtkQyInfo() {
        return ntkQyInfo;
    }

    public void setNtkQyInfo(String ntkQyInfo) {
        this.ntkQyInfo = ntkQyInfo;
    }

    public String getDietNtrsmallInfo() {
        return dietNtrsmallInfo;
    }

    public void setDietNtrsmallInfo(String dietNtrsmallInfo) {
        this.dietNtrsmallInfo = dietNtrsmallInfo;
    }

    public String getRtnOrginlFileNm() {
        return rtnOrginlFileNm;
    }

    public void setRtnOrginlFileNm(String rtnOrginlFileNm) {
        this.rtnOrginlFileNm = rtnOrginlFileNm;
    }

    public String getFdCntntsNo() {
        return fdCntntsNo;
    }

    public void setFdCntntsNo(String fdCntntsNo) {
        this.fdCntntsNo = fdCntntsNo;
    }

    public String getProtInfo() {
        return protInfo;
    }

    public void setProtInfo(String protInfo) {
        this.protInfo = protInfo;
    }

    public String getRegistDt() {
        return registDt;
    }

    public void setRegistDt(String registDt) {
        this.registDt = registDt;
    }

    @Override
    public String toString() {
        return "DietDtl{" +
                "ntrfsInfo='" + ntrfsInfo + '\'' +
                ", ckngMthInfo='" + ckngMthInfo + '\'' +
                ", crfbInfo='" + crfbInfo + '\'' +
                ", vtmbInfo='" + vtmbInfo + '\'' +
                ", cntntsChargerEsntlNm='" + cntntsChargerEsntlNm + '\'' +
                ", fdNm='" + fdNm + '\'' +
                ", clciInfo='" + clciInfo + '\'' +
                ", rtnThumbFileNm='" + rtnThumbFileNm + '\'' +
                ", fdInfoFirst='" + fdInfoFirst + '\'' +
                ", vtmcInfo='" + vtmcInfo + '\'' +
                ", matrlInfo='" + matrlInfo + '\'' +
                ", rtnImageDc='" + rtnImageDc + '\'' +
                ", dietNm='" + dietNm + '\'' +
                ", naInfo='" + naInfo + '\'' +
                ", ircnInfo='" + ircnInfo + '\'' +
                ", rtnFileSn='" + rtnFileSn + '\'' +
                ", rtnFileCours='" + rtnFileCours + '\'' +
                ", crbhInfo='" + crbhInfo + '\'' +
                ", dietDtlNm='" + dietDtlNm + '\'' +
                ", dietCn='" + dietCn + '\'' +
                ", chlsInfo='" + chlsInfo + '\'' +
                ", rtnFileSeCode='" + rtnFileSeCode + '\'' +
                ", cntntsRdcnt='" + cntntsRdcnt + '\'' +
                ", clriInfo='" + clriInfo + '\'' +
                ", cntntsNo='" + cntntsNo + '\'' +
                ", frmlasaltEqvlntqyInfo='" + frmlasaltEqvlntqyInfo + '\'' +
                ", vtmaInfo='" + vtmaInfo + '\'' +
                ", fdInfo='" + fdInfo + '\'' +
                ", rtnStreFileNm='" + rtnStreFileNm + '\'' +
                ", rtnImgSeCode='" + rtnImgSeCode + '\'' +
                ", cntntsSj='" + cntntsSj + '\'' +
                ", ntkQyInfo='" + ntkQyInfo + '\'' +
                ", dietNtrsmallInfo='" + dietNtrsmallInfo + '\'' +
                ", rtnOrginlFileNm='" + rtnOrginlFileNm + '\'' +
                ", fdCntntsNo='" + fdCntntsNo + '\'' +
                ", protInfo='" + protInfo + '\'' +
                ", registDt='" + registDt + '\'' +
                '}';
    }
}
