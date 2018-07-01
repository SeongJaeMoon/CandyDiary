package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.Firebase.FireResponseModel;

public interface SearchHelper {
    public abstract void success(FireResponseModel response);
    public abstract void failure(String message);
}
