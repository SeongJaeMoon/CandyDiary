package goods.cap.app.goodsgoods.Util;

import java.util.List;

public class Search {
    //검색 목록 키워드 -> 고유번호
    private int flag; //0: post, 1: diet, 2: therapy
    private String uid, fkey;
    private String mainText; //메인 이름
    private String userName; //포스팅의 경우 사용자 이름
    private List<String> tags; //태그
    private String image; //이미지 주소

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Search{" +
                "flag=" + flag +
                ", uid='" + uid + '\'' +
                ", fkey='" + fkey + '\'' +
                ", mainText='" + mainText + '\'' +
                ", userName='" + userName + '\'' +
                ", tags=" + tags +
                ", image='" + image + '\'' +
                '}';
    }
}
