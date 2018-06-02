package goods.cap.app.goodsgoods.Model;

import goods.cap.app.goodsgoods.Model.Diet.Diet;
import goods.cap.app.goodsgoods.Model.Food.Food;
import goods.cap.app.goodsgoods.Model.Grocery.Grocery;
import goods.cap.app.goodsgoods.Model.Health.Health;
import goods.cap.app.goodsgoods.Model.Pet.Pet;

public class Recent {

    private Diet diet;
    private Health health;
    private Pet pet;

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

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }
}
