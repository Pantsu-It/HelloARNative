package cn.easyar.samples.helloar.beans.render;

/**
 * Created by Pants on 2017/4/13.
 */
public class Render {

    private int renderId;
    private String fileUri;
    private int type;

    public Render(String fileUri, int type) {
        renderId = -1;
        this.fileUri = fileUri;
        this.type = type;
    }

    public Render(int renderId, String fileUri, int type) {
        this.renderId = renderId;
        this.fileUri = fileUri;
        this.type = type;
    }

    public int getRenderId() {
        return renderId;
    }

    public void setRenderId(int renderId) {
        this.renderId = renderId;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
