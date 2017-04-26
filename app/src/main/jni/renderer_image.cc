/**
* Copyright (c) 2015-2016 VisionStar Information Technology (Shanghai) Co., Ltd. All Rights Reserved.
* EasyAR is the registered trademark or trademark of VisionStar Information Technology (Shanghai) Co., Ltd in China
* and other countries for the augmented reality technology developed by VisionStar Information Technology (Shanghai) Co., Ltd.
*/

#include "renderer_image.hpp"
#if defined __APPLE__
#include <OpenGLES/ES2/gl.h>
#else
#include <GLES2/gl2.h>
#include <jni.h>
#include <android/bitmap.h>
#endif

#ifdef ANDROID
#include <android/log.h>
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, "EasyAR", __VA_ARGS__)
#else
#define LOGI(...) printf(__VA_ARGS__)
#endif

const char* box_image_vert="uniform mat4 trans;\n"
        "uniform mat4 proj;\n"
        "attribute vec4 coord;\n"
        "attribute vec2 texcoord;\n"
        "varying vec2 vtexcoord;\n"
        "\n"
        "void main(void)\n"
        "{\n"
        "    vtexcoord = texcoord;\n"
        "    gl_Position = proj*trans*coord;\n"
        "}\n"
        "\n"
;

const char* box_image_frag="#ifdef GL_ES\n"
        "precision highp float;\n"
        "#endif\n"
        "varying vec2 vtexcoord;\n"
        "uniform sampler2D texture;\n"
        "\n"
        "void main(void)\n"
        "{\n"
        "    gl_FragColor = texture2D(texture, vtexcoord);\n"
        "}\n"
        "\n"
;

namespace EasyAR{
namespace samples{

void ImageRenderer::init(JNIEnv *env)
{
    program_box = glCreateProgram();
    GLuint vertShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertShader, 1, &box_image_vert, 0);
    glCompileShader(vertShader);
    GLuint fragShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragShader, 1, &box_image_frag, 0);
    glCompileShader(fragShader);
    glAttachShader(program_box, vertShader);
    glAttachShader(program_box, fragShader);
    glLinkProgram(program_box);
    glUseProgram(program_box);
    pos_coord_box = glGetAttribLocation(program_box, "coord");
    pos_tex_box = glGetAttribLocation(program_box, "texcoord");
    pos_trans_box = glGetUniformLocation(program_box, "trans");
    pos_proj_box = glGetUniformLocation(program_box, "proj");

    glGenBuffers(1, &vbo_coord_box);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_coord_box);
    const GLfloat cube_vertices[4][3] = {{1.0f / 2, 1.0f / 2, 0.f},{1.0f / 2, -1.0f / 2, 0.f},{-1.0f / 2, -1.0f / 2, 0.f},{-1.0f / 2, 1.0f / 2, 0.f}};
    glBufferData(GL_ARRAY_BUFFER, sizeof(cube_vertices), cube_vertices, GL_DYNAMIC_DRAW);

    glGenBuffers(1, &vbo_tex_box);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_tex_box);
    const GLubyte cube_vertex_texs[4][2] = {{0, 0},{0, 1},{1, 1},{1, 0}};
    glBufferData(GL_ARRAY_BUFFER, sizeof(cube_vertex_texs), cube_vertex_texs, GL_STATIC_DRAW);

    glGenBuffers(1, &vbo_faces_box);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo_faces_box);
    const GLushort cube_faces[] = {3, 2, 1, 0};
    glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(cube_faces), cube_faces, GL_STATIC_DRAW);

    glUniform1i(glGetUniformLocation(program_box, "texture"), 0);
    glGenTextures(1, &texture_id);
    glBindTexture(GL_TEXTURE_2D, texture_id);
    LOGI("generated texture_id: %d", texture_id);
    //纹理滤镜
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    //纹理缠绕（边界）
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    mark = 1;

    env->ExceptionDescribe();

    AndroidBitmapInfo info;
    GLvoid* pixels;
    AndroidBitmap_getInfo(env, bitmap, &info);
    if(info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        LOGI("Bitmap format is not RGBA_8888! format: %d w:%d h:%d", info.format, info.width, info.height);
    } else {
        LOGI("Bitmap format is RGBA_8888! format: %d w:%d h:%d", info.format, info.width, info.height);
    }
    AndroidBitmap_lockPixels(env, bitmap, &pixels);
    // Now you can use the pixel array 'pixels', which is in RGBA format
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, info.width, info.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, pixels);
    AndroidBitmap_unlockPixels(env, bitmap);
    LOGI("Bitmap format --------------------------2");

    //aux扩展库
//    AUX_RGBImageRec* TextureImage[1];
//    TextureImage[0]=auxDIBImageLoad("1.bmp");
//    gluBuild2DMipmaps(GL_TEXTURE_2D, 3, TextureImage[0]->sizeX, TextureImage[0]->sizeY, GL_RGB, GL_UNSIGNED_BYTE, TextureImage[0]->data);
    //libpng解析图片得到pixels
//    glTexImage2D()
//    glTexSubImage2D()

}
    GLvoid* getBitmapPixels(JNIEnv* env, jobject bitmap)
    {
        AndroidBitmapInfo info;

        GLvoid* pixels;
        AndroidBitmap_getInfo(env, bitmap, &info);
        if(info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
            LOGI("Bitmap format is not RGBA_8888! format: %d", info.format);
            return NULL;
        }
        AndroidBitmap_lockPixels(env, bitmap, &pixels);
        // Now you can use the pixel array 'pixels', which is in RGBA format
        LOGI("Bitmap format --------------------------");
        AndroidBitmap_unlockPixels(env, bitmap);
        return pixels;
    }


void ImageRenderer::render(const Matrix44F& projectionMatrix, const Matrix44F& cameraview, Vec2F size)
{
    if(mark) {
        mark = 0;
    }

    glBindBuffer(GL_ARRAY_BUFFER, vbo_coord_box);
    const GLfloat cube_vertices[4][3] = {{size[0] / 2, size[1] / 2, 0.f},{size[0] / 2, -size[1] / 2, 0.f},{-size[0] / 2, -size[1] / 2, 0.f},{-size[0] / 2, size[1] / 2, 0.f}};
    glBufferData(GL_ARRAY_BUFFER, sizeof(cube_vertices), cube_vertices, GL_DYNAMIC_DRAW);

    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    glEnable(GL_DEPTH_TEST);
    glUseProgram(program_box);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_coord_box);
    glEnableVertexAttribArray(pos_coord_box);
    glVertexAttribPointer(pos_coord_box, 3, GL_FLOAT, GL_FALSE, 0, 0);
    glBindBuffer(GL_ARRAY_BUFFER, vbo_tex_box);
    glEnableVertexAttribArray(pos_tex_box);
    glVertexAttribPointer(pos_tex_box, 2, GL_UNSIGNED_BYTE, GL_FALSE, 0, 0);
    glUniformMatrix4fv(pos_trans_box, 1, 0, cameraview.data);
    glUniformMatrix4fv(pos_proj_box, 1, 0, projectionMatrix.data);
    glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vbo_faces_box);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texture_id);
    LOGI("using texture_id: %d", texture_id);
    glDrawElements(GL_TRIANGLE_FAN, 4, GL_UNSIGNED_SHORT, 0);
    glBindTexture(GL_TEXTURE_2D, 0);
}

    void ImageRenderer::setTexId(int texId)
    {
        texture_id = texId;
    }

    unsigned int ImageRenderer::texId()
    {
        return texture_id;
    }

    void ImageRenderer::setBitmap(jobject bitmap_)
    {
        bitmap = bitmap_;

//        AndroidBitmapInfo info;
//        GLvoid* pixels;
//        AndroidBitmap_getInfo(env, bitmap, &info);
//        if(info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
//            LOGI("Bitmap format is not RGBA_8888! format: %d w:%d h:%d", info.format, info.width, info.height);
//            return;
//        } else {
//            LOGI("Bitmap format is RGBA_8888! format: %d w:%d h:%d", info.format, info.width, info.height);
//        }
//        AndroidBitmap_lockPixels(env, bitmap, &pixels);
//        // Now you can use the pixel array 'pixels', which is in RGBA format
//        LOGI("Bitmap format --------------------------2");
//        AndroidBitmap_unlockPixels(env, bitmap);
    }

    void ImageRenderer::setPixels(GLvoid *pPixels)
    {
        pixels = pPixels;
    }
}
}
