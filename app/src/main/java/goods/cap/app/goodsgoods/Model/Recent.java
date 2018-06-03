package goods.cap.app.goodsgoods.Model;

import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Food.Food;
import goods.cap.app.goodsgoods.Model.Grocery.Grocery;
import goods.cap.app.goodsgoods.Model.Health.Health;

public class Recent {

    private Diet diet;
    private Health health;

    public Recent(){

    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
    }

    public Health getHealth() {
        return health;
    }

    public void setHealth(Health health) {
        this.health = health;
    }
}
