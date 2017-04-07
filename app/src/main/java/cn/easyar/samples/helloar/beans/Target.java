package cn.easyar.samples.helloar.beans;

/**
 * Created by Pants on 2017/4/7.
 */
public class Target {

    private String image;
    private int srcType;
    private String srcUrl;

    public Target() {
        srcType = -1;
    }

    public Target(String image, int srcType, String srcUrl) {
        this.image = image;
        this.srcType = srcType;
        this.srcUrl = srcUrl;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSrc(int srcType, String srcUrl) {
        this.srcType = srcType;
        this.srcUrl = srcUrl;
    }

    public boolean isValid() {
        return image != null && srcType != -1 && srcUrl != null;
    }

    public String getImage() {
        return image;
    }

    public int getSrcType() {
        return srcType;
    }

    public String getSrcUrl() {
        return srcUrl;
    }
}
