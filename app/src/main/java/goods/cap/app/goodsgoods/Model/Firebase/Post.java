package goods.cap.app.goodsgoods.Model.Firebase;

import java.util.Map;

public class Post {
    private String desc, title, uid, date;
    private Map<String, Object> tagMap;
    private Map<String, Object> imageMap;

    public Post(){}

    public Post(String desc, String title, String uid, String date, Map<String, Object> tagMap, Map<String, Object> imageMap) {
        this.desc = desc;
        this.title = title;
        this.uid = uid;
        this.date = date;
        this.tagMap = tagMap;
        this.imageMap = imageMap;
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

    public Map<String, Object> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, Object> tagMap) {
        this.tagMap = tagMap;
    }

    public Map<String, Object> getImageMap() {
        return imageMap;
    }

    public void setImageMap(Map<String, Object> imageMap) {
        this.imageMap = imageMap;
    }
}
