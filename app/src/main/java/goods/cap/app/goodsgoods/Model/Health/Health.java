package goods.cap.app.goodsgoods.Model.Health;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict = false)
public class Health {

    @Element(name = "NTK_MTHD", data = true)
    private String NTK_MTHD; //섭취량/섭취방법
    @Element(name = "CSTDY_MTHD", data = true)
    private String CSTDY_MTHD; //보존 및 유통기준
    @Element(name = "PRDLST_NM", data = true)
    private String PRDLST_NM; //제품명
    @Element(name = "PRMS_DT", data = true)
    private String PRMS_DT; //등록일자
    @Element(name = "BSSH_NM", data = true)
    private String BSSH_NM; //수입업체
    @Element(name = "GU_PRDLST_MNF_MANAGE_NO", data = true)
    private String GU_PRDLST_MNF_MANAGE_NO; //품목제조관리번
    @Element(name = "IFTKN_ATNT_MATR_CN", data = true)
    private String IFTKN_ATNT_MATR_CN; //섭취시 주의사항
    @Element(name = "STDR_STND", data = true)
    private String STDR_STND; //기준 및 규격
    @Element(name = "DISPOS", data = true)
    private String DISPOS; //성상
    @Element(name = "PRIMARY_FNCLTY", data = true)
    private String PRIMARY_FNCLTY; //기능성 내용

    public Health(){}

    public String getNTK_MTHD() {
        return NTK_MTHD;
    }

    public void setNTK_MTHD(String NTK_MTHD) {
        this.NTK_MTHD = NTK_MTHD;
    }

    public String getCSTDY_MTHD() {
        return CSTDY_MTHD;
    }

    public void setCSTDY_MTHD(String CSTDY_MTHD) {
        this.CSTDY_MTHD = CSTDY_MTHD;
    }

    public String getPRDLST_NM() {
        return PRDLST_NM;
    }

    public void setPRDLST_NM(String PRDLST_NM) {
        this.PRDLST_NM = PRDLST_NM;
    }

    public String getPRMS_DT() {
        return PRMS_DT;
    }

    public void setPRMS_DT(String PRMS_DT) {
        this.PRMS_DT = PRMS_DT;
    }

    public String getBSSH_NM() {
        return BSSH_NM;
    }

    public void setBSSH_NM(String BSSH_NM) {
        this.BSSH_NM = BSSH_NM;
    }

    public String getGU_PRDLST_MNF_MANAGE_NO() {
        return GU_PRDLST_MNF_MANAGE_NO;
    }

    public void setGU_PRDLST_MNF_MANAGE_NO(String GU_PRDLST_MNF_MANAGE_NO) {
        this.GU_PRDLST_MNF_MANAGE_NO = GU_PRDLST_MNF_MANAGE_NO;
    }

    public String getIFTKN_ATNT_MATR_CN() {
        return IFTKN_ATNT_MATR_CN;
    }

    public void setIFTKN_ATNT_MATR_CN(String IFTKN_ATNT_MATR_CN) {
        this.IFTKN_ATNT_MATR_CN = IFTKN_ATNT_MATR_CN;
    }

    public String getSTDR_STND() {
        return STDR_STND;
    }

    public void setSTDR_STND(String STDR_STND) {
        this.STDR_STND = STDR_STND;
    }

    public String getDISPOS() {
        return DISPOS;
    }

    public void setDISPOS(String DISPOS) {
        this.DISPOS = DISPOS;
    }

    public String getPRIMARY_FNCLTY() {
        return PRIMARY_FNCLTY;
    }

    public void setPRIMARY_FNCLTY(String PRIMARY_FNCLTY) {
        this.PRIMARY_FNCLTY = PRIMARY_FNCLTY;
    }

    @Override
    public String toString() {
        return "Health{" +
                "NTK_MTHD='" + NTK_MTHD + '\'' +
                ", CSTDY_MTHD='" + CSTDY_MTHD + '\'' +
                ", PRDLST_NM='" + PRDLST_NM + '\'' +
                ", PRMS_DT='" + PRMS_DT + '\'' +
                ", BSSH_NM='" + BSSH_NM + '\'' +
                ", GU_PRDLST_MNF_MANAGE_NO='" + GU_PRDLST_MNF_MANAGE_NO + '\'' +
                ", IFTKN_ATNT_MATR_CN='" + IFTKN_ATNT_MATR_CN + '\'' +
                ", STDR_STND='" + STDR_STND + '\'' +
                ", DISPOS='" + DISPOS + '\'' +
                ", PRIMARY_FNCLTY='" + PRIMARY_FNCLTY + '\'' +
                '}';
    }
}
