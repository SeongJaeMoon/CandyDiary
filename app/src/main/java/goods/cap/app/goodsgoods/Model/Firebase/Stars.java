package goods.cap.app.goodsgoods.Model.Firebase;

import java.util.List;



public class Stars {
    private String fkey;
    private String title, user, img;
    private List<String>tags;

    public Stars(){ }

    public Stars(String fkey, String title, String user, String img, List<String> tags) {
        this.fkey = fkey;
        this.title = title;
        this.user = user;
        this.img = img;
        this.tags = tags;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public String getUser() {
        return user;
    }

    public String getImg() {
        return img;
    }

    public List<String> getTags() {
        return tags;
    }
}
