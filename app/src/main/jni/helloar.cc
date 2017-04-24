/**
* Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
* EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
* and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
*/

#include "ar.hpp"
#include "renderer_box.hpp"
#include "renderer_video.hpp"
#include <jni.h>
#include <GLES2/gl2.h>

#define JNIFUNCTION_NATIVE(sig) Java_cn_easyar_samples_helloar_ar_ARModel_##sig

#ifdef ANDROID
#include <android/log.h>
#include <cstdlib>

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "EasyAR", __VA_ARGS__)
#else
#define LOGI(...) printf(__VA_ARGS__)
#endif

#define TYPE_TXT 0
#define TYPE_IMAGE 1
#define TYPE_VIDEO 2

extern "C" {
    JNIEXPORT jboolean JNICALL JNIFUNCTION_NATIVE(nativeInit(JNIEnv* env, jobject object));
    JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeDestory(JNIEnv* env, jobject object));
    JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeInitGL(JNIEnv* env, jobject object));
    JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeResizeGL(JNIEnv* env, jobject object, jint w, jint h));
    JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeRender(JNIEnv* env, jobject obj));
    JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeRotationChange(JNIEnv* env, jobject obj, jboolean portrait));
    JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeLoadTarget(JNIEnv* env, jobject obj, jstring path, jstring uid));
};

namespace EasyAR {
namespace samples {

class HelloAR : public AR
{
public:
    HelloAR();
    ~HelloAR();
    virtual void initGL();
    virtual void resizeGL(int width, int height);
    virtual void render();
    virtual bool clear();
private:
    Vec2I view_size;
    //box
    BoxRenderer boxRenderer;
    //video
    VideoRenderer* videoRenderers[3];
    int tracked_target;
    int active_target;
    int texid[3];
    ARVideo* video;
    VideoRenderer* video_renderer;
};

HelloAR::HelloAR()
{
    view_size[0] = -1;
    //video
    tracked_target = 0;
    active_target = 0;
    for(int i = 0; i < 3; ++i) {
        texid[i] = 0;
        videoRenderers[i] = new VideoRenderer;
    }
    video = NULL;
    video_renderer = NULL;
}
    HelloAR::~HelloAR()
    {
        for(int i = 0; i < 3; ++i) {
            delete videoRenderers[i];
        }
    }

void HelloAR::initGL()
{
    augmenter_ = Augmenter();
    augmenter_.attachCamera(camera_);
    //box
    boxRenderer.init();
    //video
    for(int i = 0; i < 3; ++i) {
        videoRenderers[i]->init();
        texid[i] = videoRenderers[i]->texId();
    }
}

void HelloAR::resizeGL(int width, int height)
{
    view_size = Vec2I(width, height);
}

void HelloAR::render()
{
    glClearColor(0.f, 0.f, 0.f, 1.f);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    Frame frame = augmenter_.newFrame();
    if(view_size[0] > 0){
        AR::resizeGL(view_size[0], view_size[1]);
        if(camera_ && camera_.isOpened())
            view_size[0] = -1;
    }
    augmenter_.setViewPort(viewport_);
    augmenter_.drawVideoBackground();
    glViewport(viewport_[0], viewport_[1], viewport_[2], viewport_[3]);

    for (int i = 0; i < frame.targets().size(); ++i) {
        Target target1 = frame.targets()[i].target();
        std::string uid = target1.uid();

        long index1 = uid.find_first_of("type:");
        long index2 = uid.find_first_of("content:");
        long length = uid.length();
        std::string type = uid.substr(index1 + 5, index2 + 1);
        std::string content = uid.substr(index2 + 16, length);
        short _type = atoi(type.c_str());

        AugmentedTarget::Status status = frame.targets()[i].status();
        if(_type == TYPE_TXT || _type == TYPE_IMAGE) {
            if (status == AugmentedTarget::kTargetStatusTracked) {
                Matrix44F projectionMatrix = getProjectionGL(camera_.cameraCalibration(), 0.2f, 500.f);
                Matrix44F cameraview = getPoseGL(frame.targets()[i].pose());
                ImageTarget target = frame.targets()[i].target().cast_dynamic<ImageTarget>();
                boxRenderer.render(projectionMatrix, cameraview, target.size());
            }
        } else if (_type == TYPE_VIDEO) {
            LOGI("track_target1: %d  %d", tracked_target, status);
            if (status == AugmentedTarget::kTargetStatusTracked) {
                int id = frame.targets()[0].target().id();
                if (active_target && active_target != id) {
                    video->onLost();
                    delete video;
                    video = NULL;
                    tracked_target = 0;
                    active_target = 0;
                }
                if (!tracked_target) {
                    if (video == NULL) {
//                        if (frame.targets()[0].target().name() == std::string("argame") && texid[0]) {
                        if (texid[0]) {
                            video = new ARVideo;
                            video->openVideoFile(content, texid[0]);
                            video_renderer = videoRenderers[0];
                        }
                    }
//                else if(frame.targets()[0].target().name() == std::string("namecard") && texid[1]) {
//                    video = new ARVideo;
//                    video->openTransparentVideoFile("transparentvideo.mp4", texid[1]);
//                    video_renderer = renderer[1];
//                }
//                else if(frame.targets()[0].target().name() == std::string("idback") && texid[2]) {
//                    video = new ARVideo;
//                    video->openStreamingVideo("http://7xl1ve.com5.z0.glb.clouddn.com/sdkvideo/EasyARSDKShow201520.mp4", texid[2]);
//                    video_renderer = renderer[2];
//                }
                    if (video) {
                        video->onFound();
                        tracked_target = id;
                        active_target = id;
                    }
                }
                Matrix44F projectionMatrix = getProjectionGL(camera_.cameraCalibration(), 0.2f, 500.f);
                Matrix44F cameraview = getPoseGL(frame.targets()[0].pose());
                ImageTarget target = frame.targets()[0].target().cast_dynamic<ImageTarget>();
                LOGI("track_target2: %d", tracked_target);
                if (tracked_target) {
                    video->update();
                    video_renderer->render(projectionMatrix, cameraview, target.size());
                }
            } else {
                LOGI("track_target3: %d", tracked_target);
                if (tracked_target) {

                    video->onLost();
                    tracked_target = 0;
                }
            }
        }
    }
}

    bool HelloAR::clear()
    {
        AR::clear();
        if(video){
            delete video;
            video = NULL;
            tracked_target = 0;
            active_target = 0;
        }
        return true;
    }

}
}
EasyAR::samples::HelloAR ar;

JNIEXPORT jboolean JNICALL JNIFUNCTION_NATIVE(nativeInit(JNIEnv*, jobject))
{
    bool status = ar.initCamera();
//    ar.loadFromImage("namecard.jpg");
//    ar.loadFromJsonFile("targets.json", "argame");
//    ar.loadFromJsonFile("targets.json", "idback");
//    ar.loadFromJsonFile("targets2.json", "my_target");
//    ar.loadAllFromJsonFile("targets2.json");
    status &= ar.start();
    return status;
}

JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeDestory(JNIEnv*, jobject))
{
    ar.clear();
}

JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeInitGL(JNIEnv*, jobject))
{
    ar.initGL();
}

JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeResizeGL(JNIEnv*, jobject, jint w, jint h))
{
    ar.resizeGL(w, h);
}

JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeRender(JNIEnv*, jobject))
{
    ar.render();
}

JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeRotationChange(JNIEnv*, jobject, jboolean portrait))
{
    ar.setPortrait(portrait);
}

JNIEXPORT void JNICALL JNIFUNCTION_NATIVE(nativeLoadTarget(JNIEnv* env, jobject, jstring path, jstring uid))
{
    std::string str1 = env->GetStringUTFChars(path, false);
    std::string str2 = env->GetStringUTFChars(uid, false);

    ar.loadTarget(str1, str2);
}