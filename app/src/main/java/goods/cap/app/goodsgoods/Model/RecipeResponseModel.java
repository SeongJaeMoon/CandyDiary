package goods.cap.app.goodsgoods.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipeResponseModel {

    @SerializedName("Grid_20150827000000000226_1")
    @Expose
    private Query query;

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "RecipeResponseModel{" +
                "query=" + query +
                '}';
    }
}
