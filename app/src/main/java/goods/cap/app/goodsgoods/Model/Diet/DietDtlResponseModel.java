package goods.cap.app.goodsgoods.Model.Diet;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class DietDtlResponseModel {

    @Element(name = "body")
    private Body body;

    @Element(name = "header", required = false)
    private Header header;

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public static class Body{

        @Element(name = "items")
        private DtlItems items;

        public DtlItems getItems() {
            return items;
        }
        public void setItems(DtlItems items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "items=" + items +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DietResponseModel{" +
                "body=" + body.toString() +
                ", header=" +
                '}';
    }
}
