package goods.cap.app.goodsgoods.Model;

public class Recent {
    // Image URL
    private String url;
    private String summary;
    // recent -> 0: Recipe, 1: Diet, 2: Food, 3: Grocery
    private int recent;

    public Recent(String url, String summary,int recent) {
        this.url = url;
        this.summary = summary;
        this.recent = recent;
    }

    public String getSummary(){return summary;}

    public String getUrl() {
        return url;
    }

    public int getRecent() {
        return recent;
    }
}
