package goods.cap.app.goodsgoods.Helper;


import goods.cap.app.goodsgoods.Model.Therapy.TherapyResponseModel;

public interface TherapyHelper {
    public abstract void success(TherapyResponseModel response);
    public abstract void failure(String message);
}
