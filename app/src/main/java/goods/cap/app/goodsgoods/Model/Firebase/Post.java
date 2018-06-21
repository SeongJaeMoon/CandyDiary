package goods.cap.app.goodsgoods.Model.Firebase;

import java.util.Map;

public class Post {
    private String desc, title, uid, date;
    private Map<String, Object> tags;
    private Map<String, Object> images;

    public Post(){}

    public Post(String desc, String title, String uid, String date, Map<String, Object> tags, Map<String, Object> images) {
        this.desc = desc;
        this.title = title;
        this.uid = uid;
        this.date = date;
        this.tags = tags;
        this.images = images;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Object> getTags() {
        return tags;
    }

    public void setTags(Map<String, Object> tags) {
        this.tags = tags;
    }

    public Map<String, Object> getImages() {
        return images;
    }

    public void setImages(Map<String, Object> images) {
        this.images = images;
    }
}
