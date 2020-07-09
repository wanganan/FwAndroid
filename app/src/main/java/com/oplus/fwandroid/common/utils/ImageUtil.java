package com.oplus.fwandroid.common.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.oplus.fwandroid.common.widget.RoundedCornersTransformation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ImageUtil {



    public static boolean isDestroy(Activity activity) {
        if (activity == null || activity.isFinishing() || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        } else {
            return false;
        }
    }
    public static void loadImageCrossFade(Context context, ImageView view , String url, final int defaultImg){
        if(!isDestroy((Activity)context)){
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            Glide
                    .with(context)
                    .load(url)
                    .fallback( defaultImg) //url为空的时候,显示的图片
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .error(defaultImg).into(view);
        }
    }

    public static void loadLocalImage(Context context, ImageView view , int url, final int defaultImg){
        if(!isDestroy((Activity)context)){
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            Glide
                    .with(context)
                    .load(url)
//                .placeholder(defaultImg) //加载成功前显示的图片
                    .fallback( defaultImg) //url为空的时候,显示的图片
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .error(defaultImg).into(view);
        }
    }

    public static void loadImageFromUri(Context context, ImageView view , Uri uri){
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        Glide
                .with(context)
                .load(uri)
//                .placeholder(defaultImg) //加载成功前显示的图片
//                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
              .into(view);
    }

//    public static void loadImageForTarget(Context context,String url,int error_resid,Target<Bitmap> target){
//        byte[] decode = null;
//        if(isBase64Img(url))
//        {
//            url = url.split(",")[1];
//            decode = Base64.decode(url, Base64.DEFAULT);
//        }
//
//        BitmapTypeRequest bitmapTypeRequest = Glide.with(fragment).load(decode==null ?url:decode).asBitmap();
//        if(error_resid !=0){
////            load.error(error_resid);
//            bitmapTypeRequest.placeholder(error_resid);
//        }else{
//            bitmapTypeRequest.placeholder(DEFALT_ID);
//        }
//        bitmapTypeRequest.diskCacheStrategy(DiskCacheStrategy.RESULT);
//        bitmapTypeRequest.dontAnimate();
//        bitmapTypeRequest.into(target);
//    }

    public static void loadImageCirclee(Context context, ImageView view , String url, final int defaultImg){
        if(!isDestroy((Activity)context)) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .bitmapTransform(new GlideCircleBorderTransform(2, Color.parseColor("#FFFFFF")))
                    .diskCacheStrategy(DiskCacheStrategy.DATA);
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            Glide.with(context)
                    .load(url)
                    .fallback(defaultImg) //url为空的时候,显示的图片
                    .apply(options)
                    .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                    .error(defaultImg).into(view);
        }
    }


    public static void loadImageCrossFadeRound(Context context, ImageView view , String url, final int defaultImg) {
        if (!isDestroy((Activity) context)) {
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            int px = DensityUtil.dp2px(context, 6);
            RoundedCornersTransformation transformation = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
            //顶部右边圆角
            RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);

            //组合各种Transformation,
            MultiTransformation<Bitmap> mation = new MultiTransformation<>
                    //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    (new CenterCrop(), transformation, transformation1);

            RoundedCorners roundedCorners = new RoundedCorners(25);
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(mation)).error(defaultImg).fallback(defaultImg).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(view);//四周都是圆角的圆角矩形图片。
        }
    }

    public static void loadImageCrossFadeRoundNoAnim(Context context, ImageView view , String url, final int defaultImg) {
        if (!isDestroy((Activity) context)) {
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(0).setCrossFadeEnabled(false).build();
            int px = DensityUtil.dp2px(context, 6);
            RoundedCornersTransformation transformation = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
            //顶部右边圆角
            RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);

            //组合各种Transformation,
            MultiTransformation<Bitmap> mation = new MultiTransformation<>
                    //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    (new CenterCrop(), transformation, transformation1);

            RoundedCorners roundedCorners = new RoundedCorners(25);
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(mation)).error(defaultImg).fallback(defaultImg).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(view);//四周都是圆角的圆角矩形图片。
        }
    }


    public static void loadImageFullCrossFadeRound(Context context, ImageView view , String url, final int defaultImg) {
        if (!isDestroy((Activity) context)) {
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            int px = DensityUtil.dp2px(context, 6);
            RoundedCornersTransformation transformation = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
            //顶部右边圆角
            RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);

            RoundedCornersTransformation transformation2 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT);

            RoundedCornersTransformation transformation3 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT);

            //组合各种Transformation,
            MultiTransformation<Bitmap> mation = new MultiTransformation<>
                    //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    (new CenterCrop(), transformation, transformation1,transformation2,transformation3);

            RoundedCorners roundedCorners = new RoundedCorners(25);
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(mation)).error(defaultImg).fallback(defaultImg).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(view);//四周都是圆角的圆角矩形图片。
        }
    }

    public static void loadImageFullCrossFadeRound(Context context, ImageView view , String url, int corverDp, final int defaultImg) {
        if (!isDestroy((Activity) context)) {
            DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
            int px = DensityUtil.dp2px(context, corverDp);
            RoundedCornersTransformation transformation = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_LEFT);
            //顶部右边圆角
            RoundedCornersTransformation transformation1 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.TOP_RIGHT);

            RoundedCornersTransformation transformation2 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.BOTTOM_RIGHT);

            RoundedCornersTransformation transformation3 = new RoundedCornersTransformation
                    (px, 0, RoundedCornersTransformation.CornerType.BOTTOM_LEFT);

            //组合各种Transformation,
            MultiTransformation<Bitmap> mation = new MultiTransformation<>
                    //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    (new CenterCrop(), transformation, transformation1,transformation2,transformation3);

            RoundedCorners roundedCorners = new RoundedCorners(25);
            Glide.with(context).load(url).apply(RequestOptions.bitmapTransform(mation)).error(defaultImg).fallback(defaultImg).transition(DrawableTransitionOptions.with(drawableCrossFadeFactory)).into(view);//四周都是圆角的圆角矩形图片。
        }
    }



    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    /**
     *  根据Uri获取文件真实地址
     */
    public static String getRealFilePath2(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }
        return realPath;
    }



    public static byte[] getBitmap(Bitmap bmp){
        int bytes = bmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);
        byte[] byteArray = buf.array();
        return byteArray;
    }


    private void uploadMultiFile(String url, String imgUrl) {
        String imageType = "multipart/form-data";
        File file = new File(imgUrl);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "head_image", fileBody)
                .addFormDataPart("imagetype", imageType)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = httpBuilder
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }

    public static boolean saveImageToGallery(Context context, Bitmap bitmap, String fileName) {
        // 保存图片至指定路径
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "fx";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        try {
            File file = new File(appDir, fileName);
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片(80代表压缩20%)
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            //发送广播通知系统图库刷新数据
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 旋转图片角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap setRotateAngle(int angle, Bitmap bitmap) {

        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(angle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;

    }

    /**
     * 获取图片的旋转角度
     *
     * @param filePath
     * @return
     */
    public static int getRotateAngle(String filePath) {
        int rotate_angle = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate_angle = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate_angle = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate_angle = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate_angle;
    }

    public static String compressImage(String filePath) {

        //原文件
        File oldFile = new File(filePath);


        //压缩文件路径 照片路径/
        String targetPath = oldFile.getPath();
        int quality = 50;//压缩比例0-100
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = getRotateAngle(filePath);//获取相片拍摄角度

        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = setRotateAngle(degree,bm);
        }
        File outputFile = new File(targetPath);
        try {
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
                //outputFile.createNewFile();
            } else {
                outputFile.delete();
            }
            FileOutputStream out = new FileOutputStream(outputFile);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return filePath;
        }
        return outputFile.getPath();
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap convertViewToBitmap(View view){

        view.setDrawingCacheEnabled(true);

        view.buildDrawingCache();

        Bitmap bitmap=view.getDrawingCache();

        return bitmap;

    }
}
