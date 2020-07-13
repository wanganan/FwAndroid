package com.oplus.fwandroid.common.utils

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.oplus.fwandroid.common.widget.RoundedCornersTransformation
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：
 * version: 1.0
 */
object ImageUtil {
    fun isDestroy(activity: Activity?): Boolean {
        return activity == null || activity.isFinishing || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed
    }

    fun loadImageCrossFade(
        context: Context?,
        view: ImageView?,
        url: String?,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
            Glide
                .with(context!!)
                .load(url)
                .fallback(defaultImg) //url为空的时候,显示的图片
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .error(defaultImg).into(view!!)
        }
    }

    fun loadLocalImage(
        context: Context?,
        view: ImageView?,
        url: Int,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
            Glide
                .with(context!!)
                .load(url) //                .placeholder(defaultImg) //加载成功前显示的图片
                .fallback(defaultImg) //url为空的时候,显示的图片
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .error(defaultImg).into(view!!)
        }
    }

    fun loadImageFromUri(
        context: Context?,
        view: ImageView?,
        uri: Uri?
    ) {
        val drawableCrossFadeFactory =
            DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
        Glide
            .with(context!!)
            .load(uri) //                .placeholder(defaultImg) //加载成功前显示的图片
            //                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
            .into(view!!)
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
    fun loadImageCirclee(
        context: Context?,
        view: ImageView?,
        url: String?,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val options = RequestOptions.bitmapTransform(
                GlideCircleBorderTransform(
                    2f,
                    Color.parseColor("#FFFFFF")
                )
            )
                .diskCacheStrategy(DiskCacheStrategy.DATA)
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
            Glide.with(context!!)
                .load(url)
                .fallback(defaultImg) //url为空的时候,显示的图片
                .apply(options)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .error(defaultImg).into(view!!)
        }
    }


    fun loadImageCrossFadeRound(
        context: Context?,
        view: ImageView?,
        url: String?,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
            val px = DensityUtil.dp2px(context, 6f)
            val transformation = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_LEFT
            )
            //顶部右边圆角
            val transformation1 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_RIGHT
            )

            //组合各种Transformation,
            val mation =
                MultiTransformation( //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    CenterCrop(), transformation, transformation1
                )
            val roundedCorners = RoundedCorners(25)
            Glide.with(context!!).load(url).apply(RequestOptions.bitmapTransform(mation))
                .error(defaultImg).fallback(defaultImg).transition(
                    DrawableTransitionOptions.with(drawableCrossFadeFactory)
                ).into(view!!) //四周都是圆角的圆角矩形图片。
        }
    }

    fun loadImageCrossFadeRoundNoAnim(
        context: Context?,
        view: ImageView?,
        url: String?,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(0).setCrossFadeEnabled(false).build()
            val px = DensityUtil.dp2px(context, 6f)
            val transformation = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_LEFT
            )
            //顶部右边圆角
            val transformation1 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_RIGHT
            )

            //组合各种Transformation,
            val mation =
                MultiTransformation( //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    CenterCrop(), transformation, transformation1
                )
            val roundedCorners = RoundedCorners(25)
            Glide.with(context!!).load(url).apply(RequestOptions.bitmapTransform(mation))
                .error(defaultImg).fallback(defaultImg).transition(
                    DrawableTransitionOptions.with(drawableCrossFadeFactory)
                ).into(view!!) //四周都是圆角的圆角矩形图片。
        }
    }


    fun loadImageFullCrossFadeRound(
        context: Context?,
        view: ImageView?,
        url: String?,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
            val px = DensityUtil.dp2px(context, 6f)
            val transformation = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_LEFT
            )
            //顶部右边圆角
            val transformation1 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_RIGHT
            )
            val transformation2 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
            )
            val transformation3 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.BOTTOM_LEFT
            )

            //组合各种Transformation,
            val mation =
                MultiTransformation( //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    CenterCrop(), transformation, transformation1, transformation2, transformation3
                )
            val roundedCorners = RoundedCorners(25)
            Glide.with(context!!).load(url).apply(RequestOptions.bitmapTransform(mation))
                .error(defaultImg).fallback(defaultImg).transition(
                    DrawableTransitionOptions.with(drawableCrossFadeFactory)
                ).into(view!!) //四周都是圆角的圆角矩形图片。
        }
    }

    fun loadImageFullCrossFadeRound(
        context: Context?,
        view: ImageView?,
        url: String?,
        corverDp: Int,
        defaultImg: Int
    ) {
        if (!isDestroy(context as Activity?)) {
            val drawableCrossFadeFactory =
                DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build()
            val px =
                DensityUtil.dp2px(context, corverDp.toFloat())
            val transformation = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_LEFT
            )
            //顶部右边圆角
            val transformation1 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.TOP_RIGHT
            )
            val transformation2 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.BOTTOM_RIGHT
            )
            val transformation3 = RoundedCornersTransformation(
                px,
                0,
                RoundedCornersTransformation.CornerType.BOTTOM_LEFT
            )

            //组合各种Transformation,
            val mation =
                MultiTransformation( //Glide设置圆角图片后设置ImageVIew的scanType="centerCrop"无效解决办法,将new CenterCrop()添加至此
                    CenterCrop(), transformation, transformation1, transformation2, transformation3
                )
            val roundedCorners = RoundedCorners(25)
            Glide.with(context!!).load(url).apply(RequestOptions.bitmapTransform(mation))
                .error(defaultImg).fallback(defaultImg).transition(
                    DrawableTransitionOptions.with(drawableCrossFadeFactory)
                ).into(view!!) //四周都是圆角的圆角矩形图片。
        }
    }


    fun getRealFilePath(
        context: Context,
        uri: Uri?
    ): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null) data = uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(
                uri, arrayOf(
                    MediaStore.Images.ImageColumns.DATA
                ), null, null, null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    /**
     * 根据Uri获取文件真实地址
     */
    fun getRealFilePath2(
        context: Context,
        uri: Uri?
    ): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var realPath: String? = null
        if (scheme == null) realPath =
            uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            realPath = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(
                uri, arrayOf(
                    MediaStore.Images.ImageColumns.DATA
                ),
                null, null, null
            )
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        realPath = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                val uriString = uri.toString()
                val index = uriString.lastIndexOf("/")
                val imageName = uriString.substring(index)
                var storageDir: File?
                storageDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )
                val file = File(storageDir, imageName)
                if (file.exists()) {
                    realPath = file.absolutePath
                } else {
                    storageDir =
                        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    val file1 = File(storageDir, imageName)
                    realPath = file1.absolutePath
                }
            }
        }
        return realPath
    }


    fun getBitmap(bmp: Bitmap): ByteArray? {
        val bytes = bmp.byteCount
        val buf = ByteBuffer.allocate(bytes)
        bmp.copyPixelsToBuffer(buf)
        return buf.array()
    }


    private fun uploadMultiFile(url: String, imgUrl: String) {
        val imageType = "multipart/form-data"
        val file = File(imgUrl)
        val fileBody = RequestBody.create("image/jpg".toMediaTypeOrNull(), file)
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("file", "head_image", fileBody)
            .addFormDataPart("imagetype", imageType)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        val httpBuilder = OkHttpClient.Builder()
        val okHttpClient = httpBuilder
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
            }
        })
    }

    fun saveImageToGallery(
        context: Context,
        bitmap: Bitmap,
        fileName: String?
    ): Boolean {
        // 保存图片至指定路径
        val storePath = Environment.getExternalStorageDirectory()
            .absolutePath + File.separator + "fx"
        val appDir = File(storePath)
        if (!appDir.exists()) {
            appDir.mkdirs()
        }
        try {
            val file = File(appDir, fileName)
            if (!file.exists()) {
                file.createNewFile()
            }
            val fos = FileOutputStream(file)
            //通过io流的方式来压缩保存图片(80代表压缩20%)
            val isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos)
            fos.flush()
            fos.close()

            //发送广播通知系统图库刷新数据
            val uri = Uri.fromFile(file)
            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri))
            return if (isSuccess) {
                true
            } else {
                false
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 旋转图片角度
     *
     * @param angle
     * @param bitmap
     * @return
     */
    fun setRotateAngle(angle: Int, bitmap: Bitmap?): Bitmap? {
        var bitmap = bitmap
        if (bitmap != null) {
            val m = Matrix()
            m.postRotate(angle.toFloat())
            bitmap = Bitmap.createBitmap(
                bitmap, 0, 0, bitmap.width,
                bitmap.height, m, true
            )
            return bitmap
        }
        return bitmap
    }

    /**
     * 获取图片的旋转角度
     *
     * @param filePath
     * @return
     */
    fun getRotateAngle(filePath: String?): Int {
        var rotate_angle = 0
        try {
            val exifInterface = ExifInterface(filePath)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> rotate_angle = 90
                ExifInterface.ORIENTATION_ROTATE_180 -> rotate_angle = 180
                ExifInterface.ORIENTATION_ROTATE_270 -> rotate_angle = 270
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return rotate_angle
    }

    fun compressImage(filePath: String?): String? {

        //原文件
        val oldFile = File(filePath)


        //压缩文件路径 照片路径/
        val targetPath = oldFile.path
        val quality = 50 //压缩比例0-100
        var bm = getSmallBitmap(filePath) //获取一定尺寸的图片
        val degree = getRotateAngle(filePath) //获取相片拍摄角度
        if (degree != 0) { //旋转照片角度，防止头像横着显示
            bm = setRotateAngle(degree, bm)
        }
        val outputFile = File(targetPath)
        try {
            if (!outputFile.exists()) {
                outputFile.parentFile.mkdirs()
                //outputFile.createNewFile();
            } else {
                outputFile.delete()
            }
            val out = FileOutputStream(outputFile)
            bm!!.compress(Bitmap.CompressFormat.JPEG, quality, out)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return filePath
        }
        return outputFile.path
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    fun getSmallBitmap(filePath: String?): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true //只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options)
        // 计算缩放比
        options.inSampleSize = calculateInSampleSize(options, 480, 800)
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(filePath, options)
    }


    fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int, reqHeight: Int
    ): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val heightRatio =
                Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio =
                Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }
        return inSampleSize
    }

    fun convertViewToBitmap(view: View): Bitmap? {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }
}