LOCAL_PATH_TOP := $(call my-dir)
EASYAR_PACKAGE_PATH := $(LOCAL_PATH_TOP)/../../../../../../package

include $(CLEAR_VARS)
LOCAL_PATH := $(EASYAR_PACKAGE_PATH)/Android/libs/armeabi-v7a
LOCAL_MODULE:=libEasyAR
LOCAL_SRC_FILES:=libEasyAR.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_PATH := $(LOCAL_PATH_TOP)
LOCAL_C_INCLUDES += $(EASYAR_PACKAGE_PATH)/include
LOCAL_CPPFLAGS += -DANDROID
LOCAL_LDLIBS += -llog -lGLESv2
LOCAL_SHARED_LIBRARIES += libEasyAR
LOCAL_CPP_EXTENSION := .cc
LOCAL_MODULE := libHelloARNative
LOCAL_SRC_FILES := ar.cc helloar.cc render_box.cc render_video.cc
include $(BUILD_SHARED_LIBRARY)
