package cn.easyar.samples.helloar.tool;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Pants on 2017/4/6.
 */
public class FileUtils {


    public static File getTargetsDir(Context context) {
        return context.getExternalFilesDir("targets");
    }

    public static File getRendersDir(Context context) {
        return context.getExternalFilesDir("renders");
    }

    public static File getThumbDir(Context context) {
        return context.getExternalFilesDir("thumbs");
    }

    public static String getPostfix(String filePath) {
        String[] splits = filePath.split("\\.");
        return splits[splits.length - 1];
    }

    public static String getTargetFileName(String filePath) {
        return "t" + System.currentTimeMillis() + ".png";
    }

    public static String getRenderFileName(String filePath) {
        return "s" + System.currentTimeMillis() + ".png";
    }

    public static String getThumbFileName(int renderId) {
        return "thumb-" + renderId + ".png";
    }

    public static Intent selectImage(Context context) {
//        Intent intent=new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        context.startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");    //这个参数是确定要选择的内容为图片，
        intent.putExtra("crop", "circle");   //设置了参数，就会调用裁剪，如果不设置，就会跳过裁剪的过程。
        intent.putExtra("aspectX", 33);  //这个是裁剪时候的 裁剪框的 X 方向的比例。
        intent.putExtra("aspectY", 43);  //同上Y 方向的比例. (注意： aspectX, aspectY ，两个值都需要为 整数，如果有一个为浮点数，就会导致比例失效。)
//设置aspectX 与 aspectY 后，裁剪框会按照所指定的比例出现，放大缩小都不会更改。如果不指定，那么 裁剪框就可以随意调整了。
        intent.putExtra("outputX", 50);  //返回数据的时候的 X 像素大小。
        intent.putExtra("outputY", 100);  //返回的时候 Y 的像素大小。
//以上两个值，设置之后会按照两个值生成一个Bitmap, 两个值就是这个bitmap的横向和纵向的像素值，如果裁剪的图像和这个像素值不符合，那么空白部分以黑色填充。
        intent.putExtra("noFaceDetection", true); // 是否去除面部检测， 如果你需要特定的比例去裁剪图片，那么这个一定要去掉，因为它会破坏掉特定的比例。
        intent.putExtra("return-data", true);  //是否要返回值。 一般都要。我第一次忘加了，总是取得空值，囧！
        return intent;
    }

    public static Intent cameraImage(Uri toFile) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //写文件
        intent.putExtra(MediaStore.EXTRA_OUTPUT, toFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);
        intent.putExtra("return-data", false);
        return intent;
    }

    public static Intent cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        return intent;
    }

    public static Intent selectVideo(Context context) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("video/*");
        return intent;
    }

    public static File saveBitmap(Bitmap bitmap, File dir, String fileName) {
        File f = new File(dir, fileName);
        saveBitmap(bitmap, f);
        return f;
    }

    public static void saveBitmap(Bitmap bitmap, File file) {
        try {
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(InputStream srcFile, File tarFile) throws Exception {
        int length = 2097152;
        InputStream in = srcFile;
        FileOutputStream out = new FileOutputStream(tarFile);
        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
            } else {
                out.write(buffer, 0, ins);
            }
        }
    }

    public static void saveFile(File srcFile, File tarFile) throws Exception {
        int length = 2097152;
        FileInputStream in = new FileInputStream(srcFile);
        FileOutputStream out = new FileOutputStream(tarFile);
        byte[] buffer = new byte[length];
        while (true) {
            int ins = in.read(buffer);
            if (ins == -1) {
                in.close();
                out.flush();
                out.close();
                return;
            } else {
                out.write(buffer, 0, ins);
            }
        }
    }

    public static Bitmap decodeBitmapFromFile(String path, int reqWidth, int reqHeight) {
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            //计算缩放比例
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getVideoThumbnail(String filePath) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}
