package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.Grocery.GroceryResponseModel;

public interface GroceryHelper {
    public abstract void success(GroceryResponseModel response);
    public abstract void failure(String message);
}
