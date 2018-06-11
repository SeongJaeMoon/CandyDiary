package goods.cap.app.goodsgoods.Model.Firebase;


public class Comment {
    //(comment > 키 : cntntsno - 자식 : 댓글, 이름, 날짜)
    private String fKey;
    private String uid;//uid
    private String name;//등록자명
    private String comment;//댓글
    private String regDate;//등록날짜
    private String pimage; //프로필 사진

    public Comment(){}

    public Comment(String uid, String pimage, String name, String comment, String regDate){
        this.uid = uid;
        this.pimage = pimage;
        this.name = name;
        this.comment = comment;
        this.regDate = regDate;
    }

    public String getfKey() { return fKey; }
    public void setfKey(String fKey) { this.fKey = fKey; }
    public String getPimage() { return pimage; }
    public void setPimage(String pimage) { this.pimage = pimage; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }
}
