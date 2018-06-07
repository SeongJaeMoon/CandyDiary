package goods.cap.app.goodsgoods.Helper;

import goods.cap.app.goodsgoods.Model.Therapy.TherapyDtlResponseModel;

public interface TherapyDtlHelper {
    public abstract void success(TherapyDtlResponseModel response);
    public abstract void failure(String message);
}
