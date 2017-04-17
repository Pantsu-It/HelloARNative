package cn.easyar.samples.helloar.beans.render;

/**
 * Created by Pants on 2017/4/17.
 */

public class RenderFactory {

    public static Render createText(String text) {
        return new Render(text, RenderType.TYPE_TEXT);
    }

    public static Render createImage(String path) {
        return new Render(path, RenderType.TYPE_IMAGE);
    }

    public static Render createVideo(String path) {
        return new Render(path, RenderType.TYPE_VIDEO);
    }
}
