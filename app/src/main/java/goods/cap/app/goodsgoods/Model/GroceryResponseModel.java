package goods.cap.app.goodsgoods.Model;

import java.util.Arrays;
import java.util.List;

public class GroceryResponseModel {

    private String id;
    private List<Grocery> grocerie;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Grocery> getGrocerie() {
        return grocerie;
    }

    public void setGrocerie(List<Grocery> grocerie) {
        this.grocerie = grocerie;
    }

    @Override
    public String toString() {
        return "GroceryResponseModel{" +
                "id='" + id + '\'' +
                ", grocerie=" + grocerie.toString() +
                '}';
    }
}
