package cn.easyar.samples.helloar.beans;

import cn.easyar.samples.helloar.beans.render.Render;

/**
 * Created by Pants on 2017/4/7.
 */
public class Binder {

    private Target target;
    private Render render;

    public Binder(Target target, Render render) {
        this.target = target;
        this.render = render;
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
