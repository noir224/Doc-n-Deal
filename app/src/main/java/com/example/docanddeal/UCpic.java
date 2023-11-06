package com.example.docanddeal;

public class UCpic {
    private int id;
    private String type,uri;
    private Object tag;

    public UCpic(int id, String type, String uri, Object tag) {
        this.id = id;
        this.type = type;
        this.uri = uri;
        this.tag = tag;
    }

    public UCpic() {
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
