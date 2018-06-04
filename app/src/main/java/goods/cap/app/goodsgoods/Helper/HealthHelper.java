package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.Health.HealthResponseModel;

public interface HealthHelper {
    public abstract void success(HealthResponseModel response);
    public abstract void failure(String message);
}
