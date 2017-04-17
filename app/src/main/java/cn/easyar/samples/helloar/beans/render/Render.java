package cn.easyar.samples.helloar.beans.render;

import java.io.Serializable;

/**
 * Created by Pants on 2017/4/13.
 */
public class Render implements Serializable {

    private int renderId;
    private String content;
    private int type;

    public Render(String content, int type) {
        renderId = -1;
        this.content = content;
        this.type = type;
    }

    public Render(int renderId, String fileUri, int type) {
        this.renderId = renderId;
        this.content = fileUri;
        this.type = type;
    }

    public int getRenderId() {
        return renderId;
    }

    public void setRenderId(int renderId) {
        this.renderId = renderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
