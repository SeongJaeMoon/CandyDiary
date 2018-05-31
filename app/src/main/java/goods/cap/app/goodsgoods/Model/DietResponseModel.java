package goods.cap.app.goodsgoods.Model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(strict = false)
public class DietResponseModel {

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
        private Items items;

        public Items getItems() {
            return items;
        }
        public void setItems(Items items) {
            this.items = items;
        }

        @Override
        public String toString() {
            return "Body{" +
                    "items=" + items.getDietList() +
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
