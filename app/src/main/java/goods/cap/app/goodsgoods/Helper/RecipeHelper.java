package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.RecipeResponseModel;

public interface RecipeHelper {
    public abstract void success(RecipeResponseModel response);
    public abstract void failure(String message);
}
