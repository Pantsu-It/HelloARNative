package cn.easyar.samples.helloar.beans;

import java.io.Serializable;

/**
 * Created by Pants on 2017/4/13.
 */
public class Target implements Serializable{

    private int targetId;
    private String imgUri;

    public Target(String imgUri) {
        targetId = -1;
        this.imgUri = imgUri;
    }

    public Target(int targetId, String imgUri) {
        this.targetId = targetId;
        this.imgUri = imgUri;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }
}
