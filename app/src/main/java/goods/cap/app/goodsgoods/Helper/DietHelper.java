package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.DietResponseModel;

public interface DietHelper {
    public abstract void success(DietResponseModel response);
    public abstract void failure(String message);
}
