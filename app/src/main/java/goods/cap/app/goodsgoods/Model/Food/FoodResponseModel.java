package goods.cap.app.goodsgoods.Model.Food;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import goods.cap.app.goodsgoods.Model.Food.FoodQuery;

public class FoodResponseModel {

    @SerializedName("Grid_20150827000000000228_1")
    @Expose
    private FoodQuery query;

    public FoodQuery getQuery() {
        return query;
    }

    public void setQuery(FoodQuery query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return "RecipeResponseModel{" +
                "query=" + query +
                '}';
    }
}
