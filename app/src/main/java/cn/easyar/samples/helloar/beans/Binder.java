package cn.easyar.samples.helloar.beans;

import java.io.Serializable;

import cn.easyar.samples.helloar.beans.render.Render;

/**
 * Created by Pants on 2017/4/7.
 */
public class Binder implements Serializable {

    private int binderId;
    private Target target;
    private Render render;

    public Binder(Target target, Render render) {
        this.target = target;
        this.render = render;
    }

    public Binder(int binderId, Target target, Render render) {
        this.binderId = binderId;
        this.target = target;
        this.render = render;
    }

    public int getBinderId() {
        return binderId;
    }

    public void setBinderId(int binderId) {
        this.binderId = binderId;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public Render getRender() {
        return render;
    }

    public void setRender(Render render) {
        this.render = render;
    }
}
