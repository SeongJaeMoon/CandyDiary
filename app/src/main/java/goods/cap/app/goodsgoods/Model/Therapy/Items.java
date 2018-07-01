package goods.cap.app.goodsgoods.Model.Therapy;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class Items {
    @Element(name="totalCount")
    private String totalCount;

    @Element(name="pageNo")
    private String pageNo;

    @Element(name="numOfRows")
    private String numOfRows;

    @ElementList(inline = true, required = false)
    List<Therapy>therapyList;

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(String numOfRows) {
        this.numOfRows = numOfRows;
    }

    public List<Therapy> getTherapyList() {
        return therapyList;
    }

    public void setTherapyList(List<Therapy> therapyList) {
        this.therapyList = therapyList;
    }
}
