package goods.cap.app.goodsgoods.Model.Health;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class Items {

    @ElementList(inline = true, required = false)
    private List<Health>healthList;

    public List<Health> getHealthList() {
        return healthList;
    }

    public void setHealthList(List<Health> healthList) {
        this.healthList = healthList;
    }
}
