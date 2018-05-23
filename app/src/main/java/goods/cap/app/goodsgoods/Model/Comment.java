package goods.cap.app.goodsgoods.Model;

import java.time.LocalDate;

public class Comment {

    private int count; //등록번호
    private String uid; //등록id
    private String comment; //등록내용
    private LocalDate regDate; //등록날짜
    private int likes; //공감
    public Comment(){

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getRegDate() {
        return regDate;
    }

    public void setRegDate(LocalDate regDate) {
        this.regDate = regDate;
    }
}
