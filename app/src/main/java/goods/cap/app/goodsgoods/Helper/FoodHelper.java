package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.FoodResponseModel;

public interface FoodHelper {
    public abstract void success(FoodResponseModel response);
    public abstract void failure(String message);
}
