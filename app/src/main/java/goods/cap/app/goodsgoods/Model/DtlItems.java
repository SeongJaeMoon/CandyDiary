package goods.cap.app.goodsgoods.Model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class DtlItems {

    @ElementList(inline = true, required = false)
    private List<DietDtl> dietDtlList;

    public List<DietDtl> getDietDtlList() {
        return dietDtlList;
    }

    public void setDietDtlList(List<DietDtl> dietDtlList) {
        this.dietDtlList = dietDtlList;
    }

    @Override
    public String toString() {
        return "DtlItems{" +
                "dietDtlList=" + dietDtlList +
                '}';
    }
}
