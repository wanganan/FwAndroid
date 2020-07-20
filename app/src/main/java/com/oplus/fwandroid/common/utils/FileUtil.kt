package com.oplus.fwandroid.common.utils

import android.content.Context
import android.content.Intent
import android.content.res.AssetManager
import android.database.Cursor
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.oplus.fwandroid.common.base.BaseApplication.Companion.instance
import java.io.*
import java.util.*

/**
 * @author Sinaan
 * @date 2020/7/13
 * GitHub：https://github.com/wanganan
 * email：waa182838@sina.com
 * description：File工具类，主要封装了一些对文件读写的操作
 * version: 1.0
 */
object FileUtil {
    const val FILE_EXTENSION_SEPARATOR = "."

    /**
     * URI类型：file
     */
    const val URI_TYPE_FILE = "file"

    /**
     * 判断SD卡是否可用
     *
     * @return SD卡可用返回true
     */
    fun hasSdcard(): Boolean {
        val status = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == status
    }

    /**
     * 获取外置sd卡路径
     * @return
     */
    fun getSDCardPath(): String? {
        return if (hasSdcard()) Environment.getExternalStorageDirectory()
            .absolutePath else null
    }

    /**
     * read file
     *
     * @param filePath    路径
     * @param charsetName The name of a supported [                    &lt;p/&gt;][                    <p/>]
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator
     *
     *
     * BufferedReader
     */
    fun readFile(
        filePath: String?,
        charsetName: String?
    ): StringBuilder? {
        val file = File(filePath)
        val fileContent = StringBuilder("")
        if (file == null || !file.isFile) {
            return null
        }
        var reader: BufferedReader? = null
        return try {
            val `is` = InputStreamReader(
                FileInputStream(file), charsetName
            )
            reader = BufferedReader(`is`)
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                if (fileContent.toString() != "") {
                    fileContent.append("\r\n")
                }
                fileContent.append(line)
            }
            fileContent
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(reader)
        }
    }


    /**
     * write file
     *
     * @param filePath 路径
     * @param content  上下文
     * @param append   is append, if true, write to the end of file, else clear
     *
     *
     * content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    fun writeFile(
        filePath: String,
        content: String?,
        append: Boolean
    ): Boolean {
        if (StringUtils.isEmpty(content)) {
            return false
        }
        var fileWriter: FileWriter? = null
        return try {
            makeDirs(filePath)
            fileWriter = FileWriter(filePath, append)
            fileWriter.write(content)
            true
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(fileWriter)
        }
    }


    /**
     * write file
     *
     * @param filePath    路径
     * @param contentList 集合
     * @param append      is append, if true, write to the end of file, else clear
     *
     *
     * content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    fun writeFile(
        filePath: String,
        contentList: List<String?>?,
        append: Boolean
    ): Boolean {
        if (contentList!!.size == 0 || null == contentList) {
            return false
        }
        var fileWriter: FileWriter? = null
        return try {
            makeDirs(filePath)
            fileWriter = FileWriter(filePath, append)
            var i = 0
            for (line in contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n")
                }
                fileWriter.write(line)
            }
            true
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(fileWriter)
        }
    }


    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath 地址
     * @param content  上下文
     * @return 是否写入成功
     */
    fun writeFile(filePath: String, content: String?): Boolean {
        return writeFile(filePath, content, false)
    }


    /**
     * write file, the string list will be written to the begin of the file
     *
     * @param filePath    地址
     * @param contentList 集合
     * @return 是否写入成功
     */
    fun writeFile(
        filePath: String,
        contentList: List<String?>?
    ): Boolean {
        return writeFile(filePath, contentList, false)
    }


    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath 路径
     * @param stream   输入流
     * @return 返回是否写入成功
     */
    fun writeFile(filePath: String?, stream: InputStream): Boolean {
        return writeFile(filePath, stream, false)
    }


    /**
     * write file
     *
     * @param filePath 路径
     * @param stream   the input stream
     * @param append   if `true`, then bytes will be written to the
     *
     *
     * end
     *
     *
     * of the file rather than the beginning
     * @return return true
     *
     *
     * FileOutputStream
     */
    fun writeFile(
        filePath: String?,
        stream: InputStream,
        append: Boolean
    ): Boolean {
        return writeFile(
            filePath?.let { File(it) }, stream,
            append
        )
    }


    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file   文件对象
     * @param stream 输入流
     * @return 返回是否写入成功
     */
    fun writeFile(file: File?, stream: InputStream): Boolean {
        return writeFile(file, stream, false)
    }


    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if `true`, then bytes will be written to the
     *
     *
     * end
     *
     *
     * of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator
     *
     *
     * FileOutputStream
     */
    fun writeFile(
        file: File?,
        stream: InputStream,
        append: Boolean
    ): Boolean {
        var o: OutputStream? = null
        return try {
            makeDirs(file!!.absolutePath)
            o = FileOutputStream(file, append)
            val data = ByteArray(1024)
            var length = -1
            while (stream.read(data).also { length = it } != -1) {
                o.write(data, 0, length)
            }
            o.flush()
            true
        } catch (e: FileNotFoundException) {
            throw RuntimeException("FileNotFoundException occurred. ", e)
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(o)
            close(stream)
        }
    }


    /**
     * move file
     *
     * @param sourceFilePath 资源路径
     * @param destFilePath   删除的路径
     */
    fun moveFile(sourceFilePath: String?, destFilePath: String?) {
        if (TextUtils.isEmpty(sourceFilePath) ||
            TextUtils.isEmpty(destFilePath)
        ) {
            throw RuntimeException(
                "Both sourceFilePath and destFilePath cannot be null."
            )
        }
        moveFile(File(sourceFilePath), File(destFilePath))
    }


    /**
     * move file
     *
     * @param srcFile  文件对象
     * @param destFile 对象
     */
    fun moveFile(srcFile: File, destFile: File) {
        val rename = srcFile.renameTo(destFile)
        if (!rename) {
            copyFile(srcFile.absolutePath, destFile.absolutePath)
            deleteFile(srcFile.absolutePath)
        }
    }


    /**
     * copy file
     *
     * @param sourceFilePath 资源路径
     * @param destFilePath   删除的文件
     * @return 返回是否成功
     * @throws RuntimeException if an error occurs while operator
     *
     *
     * FileOutputStream
     */
    fun copyFile(
        sourceFilePath: String?,
        destFilePath: String?
    ): Boolean {
        var inputStream: InputStream? = null
        inputStream = try {
            FileInputStream(sourceFilePath)
        } catch (e: FileNotFoundException) {
            throw RuntimeException("FileNotFoundException occurred. ", e)
        }
        return writeFile(destFilePath, inputStream)
    }


    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath    路径
     * @param charsetName The name of a supported [                    &lt;p/&gt;][                    <p/>]
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator
     *
     *
     * BufferedReader
     */
    fun readFileToList(
        filePath: String?,
        charsetName: String?
    ): List<String?>? {
        val file = File(filePath)
        val fileContent: MutableList<String?> =
            ArrayList()
        if (file == null || !file.isFile) {
            return null
        }
        var reader: BufferedReader? = null
        return try {
            val `is` = InputStreamReader(
                FileInputStream(file), charsetName
            )
            reader = BufferedReader(`is`)
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                fileContent.add(line)
            }
            fileContent
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(reader)
        }
    }


    /**
     * @param filePath 文件的路径
     * @return 返回文件的信息
     */
    fun getFileNameWithoutExtension(filePath: String): String? {
        if (StringUtils.isEmpty(filePath)) {
            return filePath
        }
        val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val filePosi = filePath.lastIndexOf(File.separator)
        if (filePosi == -1) {
            return if (extenPosi == -1) filePath else filePath.substring(0, extenPosi)
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1)
        }
        return if (filePosi < extenPosi) filePath.substring(
            filePosi + 1,
            extenPosi
        ) else filePath.substring(filePosi + 1)
    }


    /**
     * get file name from path, include suffix
     *
     *
     *
     *
     *
     *
     * <pre>
     *
     * getFileName(null)               =   null
     *
     * getFileName("")                 =   ""
     *
     * getFileName("   ")              =   "   "
     *
     * getFileName("a.mp3")            =   "a.mp3"
     *
     * getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *
     * getFileName("abc")              =   "abc"
     *
     * getFileName("c:\\")              =   ""
     *
     * getFileName("c:\\a")             =   "a"
     *
     * getFileName("c:\\a.b")           =   "a.b"
     *
     * getFileName("c:a.txt\\a")        =   "a"
     *
     * getFileName("/home/admin")      =   "admin"
     *
     * getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     *
    </pre> *
     *
     * @param filePath 路径
     * @return file name from path, include suffix
     */
    fun getFileName(filePath: String): String? {
        if (StringUtils.isEmpty(filePath)) {
            return filePath
        }
        val filePosi = filePath.lastIndexOf(File.separator)
        return if (filePosi == -1) filePath else filePath.substring(filePosi + 1)
    }


    /**
     * get folder name from path
     *
     *
     *
     *
     *
     *
     * <pre>
     *
     * getFolderName(null)               =   null
     *
     * getFolderName("")                 =   ""
     *
     * getFolderName("   ")              =   ""
     *
     * getFolderName("a.mp3")            =   ""
     *
     * getFolderName("a.b.rmvb")         =   ""
     *
     * getFolderName("abc")              =   ""
     *
     * getFolderName("c:\\")              =   "c:"
     *
     * getFolderName("c:\\a")             =   "c:"
     *
     * getFolderName("c:\\a.b")           =   "c:"
     *
     * getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *
     * getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *
     * getFolderName("/home/admin")      =   "/home"
     *
     * getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     *
    </pre> *
     *
     * @param filePath 路径
     * @return file name from path, include suffix
     */
    fun getFolderName(filePath: String): String {
        if (StringUtils.isEmpty(filePath)) {
            return filePath
        }
        val filePosi = filePath.lastIndexOf(File.separator)
        return if (filePosi == -1) "" else filePath.substring(0, filePosi)
    }


    /**
     * get suffix of file from path
     *
     *
     *
     *
     *
     *
     * <pre>
     *
     * getFileExtension(null)               =   ""
     *
     * getFileExtension("")                 =   ""
     *
     * getFileExtension("   ")              =   "   "
     *
     * getFileExtension("a.mp3")            =   "mp3"
     *
     * getFileExtension("a.b.rmvb")         =   "rmvb"
     *
     * getFileExtension("abc")              =   ""
     *
     * getFileExtension("c:\\")              =   ""
     *
     * getFileExtension("c:\\a")             =   ""
     *
     * getFileExtension("c:\\a.b")           =   "b"
     *
     * getFileExtension("c:a.txt\\a")        =   ""
     *
     * getFileExtension("/home/admin")      =   ""
     *
     * getFileExtension("/home/admin/a.txt/b")  =   ""
     *
     * getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     *
    </pre> *
     *
     * @param filePath 路径
     * @return 信息
     */
    fun getFileExtension(filePath: String): String? {
        if (StringUtils.isBlank(filePath)) {
            return filePath
        }
        val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val filePosi = filePath.lastIndexOf(File.separator)
        if (extenPosi == -1) {
            return ""
        }
        return if (filePosi >= extenPosi) "" else filePath.substring(extenPosi + 1)
    }


    /**
     * @param filePath 路径
     * @return 是否创建成功
     */
    fun makeDirs(filePath: String): Boolean {
        val folderName = getFolderName(filePath)
        if (StringUtils.isEmpty(folderName)) {
            return false
        }
        val folder = File(folderName)
        return folder.exists() && folder.isDirectory || folder.mkdirs()
    }


    /**
     * @param filePath 路径
     * @return 是否创建成功
     */
    fun makeFolders(filePath: String): Boolean {
        return makeDirs(filePath)
    }


    /**
     * @param filePath 路径
     * @return 是否存在这个文件
     */
    fun isFileExist(filePath: String?): Boolean {
        if (StringUtils.isBlank(filePath)) {
            return false
        }
        val file = File(filePath)
        return file.exists() && file.isFile
    }


    /**
     * @param directoryPath 路径
     * @return 是否有文件夹
     */
    fun isFolderExist(directoryPath: String?): Boolean {
        if (StringUtils.isBlank(directoryPath)) {
            return false
        }
        val dire = File(directoryPath)
        return dire.exists() && dire.isDirectory
    }

    /**
     * 删除指定目录中特定的文件
     *
     * @param dir
     * @param filter
     */
    fun delete(dir: String?, filter: FilenameFilter?) {
        if (TextUtils.isEmpty(dir)) return
        val file = File(dir)
        if (!file.exists()) return
        if (file.isFile) file.delete()
        if (!file.isDirectory) return
        var lists: Array<File>? = null
        lists = if (filter != null) file.listFiles(filter) else file.listFiles()
        if (lists == null) return
        for (f in lists) {
            if (f.isFile) {
                f.delete()
            }
        }
    }


    /**
     * @param path 路径
     * @return 是否删除成功
     */
    fun deleteFile(path: String?): Boolean {
        if (StringUtils.isBlank(path)) {
            return true
        }
        val file = File(path)
        if (!file.exists()) {
            return true
        }
        if (file.isFile) {
            return file.delete()
        }
        if (!file.isDirectory) {
            return false
        }
        for (f in file.listFiles()) {
            if (f.isFile) {
                f.delete()
            } else if (f.isDirectory) {
                deleteFile(f.absolutePath)
            }
        }
        return file.delete()
    }


    /**
     * @param path 路径
     * @return 返回文件大小
     */
    fun getFileSize(path: String?): Long {
        if (StringUtils.isBlank(path)) {
            return -1
        }
        val file = File(path)
        return if (file.exists() && file.isFile) file.length() else -1
    }


    /**
     * 保存多媒体数据为文件.
     *
     * @param data     多媒体数据
     * @param fileName 保存文件名
     * @return 保存成功或失败
     */
    fun save2File(data: InputStream, fileName: String?): Boolean {
        val file = File(fileName)
        var fos: FileOutputStream? = null
        return try {

            // 文件或目录不存在时,创建目录和文件.
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }


            // 写入数据
            fos = FileOutputStream(file)
            val b = ByteArray(1024)
            var len: Int
            while (data.read(b).also { len = it } > -1) {
                fos.write(b, 0, len)
            }
            fos.close()
            true
        } catch (ex: IOException) {
            false
        }
    }


    /**
     * 读取文件的字节数组.
     *
     * @param file 文件
     * @return 字节数组
     */
    fun readFile4Bytes(file: File): ByteArray? {


        // 如果文件不存在,返回空
        if (!file.exists()) {
            return null
        }
        var fis: FileInputStream? = null
        return try {

            // 读取文件内容.
            fis = FileInputStream(file)
            val arrData = ByteArray(file.length().toInt())
            fis.read(arrData)

            // 返回
            arrData
        } catch (e: IOException) {
            null
        } finally {
            if (fis != null) {
                try {
                    fis.close()
                } catch (e: IOException) {
                }
            }
        }
    }


    /**
     * 读取文本文件内容，以行的形式读取
     *
     * @param filePathAndName 带有完整绝对路径的文件名
     * @return String 返回文本文件的内容
     */
    fun readFileContent(filePathAndName: String?): String? {
        try {
            return readFileContent(filePathAndName, null, null, 1024)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    /**
     * 读取文本文件内容，以行的形式读取
     *
     * @param filePathAndName 带有完整绝对路径的文件名
     * @param encoding        文本文件打开的编码方式 例如 GBK,UTF-8
     * @param sep             分隔符 例如：#，默认为\n;
     * @param bufLen          设置缓冲区大小
     * @return String 返回文本文件的内容
     */
    fun readFileContent(
        filePathAndName: String?,
        encoding: String?,
        sep: String?,
        bufLen: Int
    ): String? {
        var sep = sep
        if (filePathAndName == null || filePathAndName == "") {
            return ""
        }
        if (sep == null || sep == "") {
            sep = "\n"
        }
        if (!File(filePathAndName).exists()) {
            return ""
        }
        val str = StringBuffer("")
        var fs: FileInputStream? = null
        var isr: InputStreamReader? = null
        var br: BufferedReader? = null
        try {
            fs = FileInputStream(filePathAndName)
            isr = if (encoding == null || encoding.trim { it <= ' ' } == "") {
                InputStreamReader(fs)
            } else {
                InputStreamReader(fs, encoding.trim { it <= ' ' })
            }
            br = BufferedReader(isr, bufLen)
            var data: String? = ""
            while (br.readLine().also { data = it } != null) {
                str.append(data).append(sep)
            }
        } catch (e: IOException) {
        } finally {
            try {
                br?.close()
                isr?.close()
                fs?.close()
            } catch (e: IOException) {
            }
        }
        return str.toString()
    }


    /**
     * 把Assets里的文件拷贝到sd卡上
     *
     * @param assetManager    AssetManager
     * @param fileName        Asset文件名
     * @param destinationPath 完整目标路径
     * @return 拷贝成功
     */
    fun copyAssetToSDCard(
        assetManager: AssetManager,
        fileName: String?,
        destinationPath: String?
    ): Boolean {
        try {
            val `is` = assetManager.open(fileName!!)
            val os = FileOutputStream(destinationPath)
            if (`is` != null && os != null) {
                val data = ByteArray(1024)
                var len: Int
                while (`is`.read(data).also { len = it } > 0) {
                    os.write(data, 0, len)
                }
                os.close()
                `is`.close()
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }


    /**
     * 调用系统方式打开文件.
     *
     * @param context 上下文
     * @param file    文件
     */
    fun openFile(context: Context, file: File) {
        try {

            // 调用系统程序打开文件.
            val intent = Intent(Intent.ACTION_VIEW)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.setDataAndType(
                Uri.fromFile(file), MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension(
                        MimeTypeMap
                            .getFileExtensionFromUrl(
                                file.path
                            )
                    )
            )
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(context, "打开失败.", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 根据文件路径，检查文件是否不大于指定大小
     *
     * @param filepath 文件路径
     * @param maxSize  最大
     * @return 是否
     */
    fun checkFileSize(filepath: String?, maxSize: Int): Boolean {
        val file = File(filepath)
        return if (!file.exists() || file.isDirectory) {
            false
        } else file.length() <= maxSize * 1024
    }


    /**
     * @param context 上下文
     * @param file    文件对象
     */
    fun openMedia(context: Context, file: File) {
        if (file.name.endsWith(".png") ||
            file.name.endsWith(".jpg") ||
            file.name.endsWith(".jpeg")
        ) {
            viewPhoto(context, file)
        } else {
            openFile(context, file)
        }
    }


    /**
     * 打开多媒体文件.
     *
     * @param context 上下文
     * @param file    多媒体文件
     */
    fun viewPhoto(context: Context, file: String?) {
        viewPhoto(context, File(file))
    }


    /**
     * 打开照片
     *
     * @param context 上下文
     * @param file    文件对象
     */
    fun viewPhoto(context: Context, file: File?) {
        try {

            // 调用系统程序打开文件.
            val intent = Intent(Intent.ACTION_VIEW)

            //			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "image/*")
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(context, "打开失败.", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 将字符串以UTF-8编码保存到文件中
     *
     * @param str      保存的字符串
     * @param fileName 文件的名字
     * @return 是否保存成功
     */
    fun saveStrToFile(str: String?, fileName: String?): Boolean {
        return saveStrToFile(str, fileName, "UTF-8")
    }


    /**
     * 将字符串以charsetName编码保存到文件中
     *
     * @param str         保存的字符串
     * @param fileName    文件的名字
     * @param charsetName 字符串编码
     * @return 是否保存成功
     */
    fun saveStrToFile(
        str: String?,
        fileName: String?,
        charsetName: String?
    ): Boolean {
        if (str == null || "" == str) {
            return false
        }
        var stream: FileOutputStream? = null
        return try {
            val file = File(fileName)
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            var b: ByteArray? = null
            b = if (charsetName != null && "" != charsetName) {
                str.toByteArray(charset(charsetName))
            } else {
                str.toByteArray()
            }
            stream = FileOutputStream(file)
            stream.write(b, 0, b.size)
            stream.flush()
            true
        } catch (e: Exception) {
            false
        } finally {
            if (stream != null) {
                try {
                    stream.close()
                    stream = null
                } catch (e: Exception) {
                }
            }
        }
    }


    /**
     * 将content://形式的uri转为实际文件路径
     *
     * @param context 上下文
     * @param uri     地址
     * @return uri转为实际文件路径
     */
    fun uriToPath(context: Context, uri: Uri): String? {
        var cursor: Cursor? = null
        try {
            if (uri.scheme.equals(URI_TYPE_FILE, ignoreCase = true)) {
                return uri.path
            }
            cursor = context.contentResolver
                .query(uri, null, null, null, null)
            if (cursor!!.moveToFirst()) {
                return cursor.getString(
                    cursor.getColumnIndex(
                        MediaStore.Images.Media.DATA
                    )
                ) //图片文件路径
            }
        } catch (e: Exception) {
            if (null != cursor) {
                cursor.close()
                cursor = null
            }
            return null
        }
        return null
    }


    /**
     * 打开多媒体文件.
     *
     * @param context 上下文
     * @param file    多媒体文件
     */
    fun playSound(context: Context, file: String?) {
        playSound(context, File(file))
    }


    /**
     * 打开多媒体文件.
     *
     * @param context 上下文
     * @param file    多媒体文件
     */
    fun playSound(context: Context, file: File?) {
        try {

            // 调用系统程序打开文件.
            val intent = Intent(Intent.ACTION_VIEW)

            //			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //			intent.setClassName("com.android.music", "com.android.music.MediaPlaybackActivity");
            intent.setDataAndType(Uri.fromFile(file), "audio/*")
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(context, "打开失败.", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 打开视频文件.
     *
     * @param context 上下文
     * @param file    视频文件
     */
    fun playVideo(context: Context, file: String?) {
        playVideo(context, File(file))
    }


    /**
     * 打开视频文件.
     *
     * @param context 上下文
     * @param file    视频文件
     */
    fun playVideo(context: Context, file: File?) {
        try {

            // 调用系统程序打开文件.
            val intent = Intent(Intent.ACTION_VIEW)

            //			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "video/*")
            context.startActivity(intent)
        } catch (ex: Exception) {
            Toast.makeText(context, "打开失败.", Toast.LENGTH_SHORT).show()
        }
    }


    /**
     * 文件重命名
     *
     * @param oldPath 旧的文件名字
     * @param newPath 新的文件名字
     */
    fun renameFile(oldPath: String, newPath: String) {
        try {
            if (!TextUtils.isEmpty(oldPath) && !TextUtils.isEmpty(newPath)
                && oldPath != newPath
            ) {
                val fileOld = File(oldPath)
                val fileNew = File(newPath)
                fileOld.renameTo(fileNew)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Close closable object and wrap [IOException] with [ &lt;p/&gt;][ <p/>]
     *
     * @param closeable closeable object
     */
    fun close(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            }
        }
    }

    /**
     * 将assets文件内容写入指定目录
     * @param context
     * @param rawName
     * @param dir
     */
    fun moveRawToDir(
        context: Context,
        rawName: String?,
        dir: String?
    ) {
        try {
            writeFile(dir, context.assets.open(rawName!!), true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 得到手机的缓存目录
     *
     * @return    内置或者外置内存的缓存文件，优先外置内存
     */
    fun getCacheDir(): File {
//        android.util.Log.e("getCacheDir", "cache sdcard state: " + Environment.getExternalStorageState());
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val cacheDir = instance.externalCacheDir
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
//                android.util.Log.e("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
                return cacheDir
            }
        }
        //        android.util.Log.e("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
        return instance.cacheDir
    }

    /**
     * 得到皮肤目录
     *
     * @param context
     * @return
     */
    fun getSkinDir(context: Context?): File {
        val skinDir = File(getCacheDir(), "skin")
        if (skinDir.exists()) {
            skinDir.mkdirs()
        }
        return skinDir
    }

    fun getSkinDirPath(context: Context?): String? {
        return getSkinDir(context).absolutePath
    }

    /**
     * 保存图片的文件夹
     * @return
     */
    fun getSaveImagePath(): File? {
        var path = getCacheDir().absolutePath
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            path = Environment.getExternalStorageDirectory().absolutePath
        }
        path = path + File.separator + "Pictures"
        val file = File(path)
        if (!file.exists()) {
            file.mkdir()
        }
        return file
    }

    fun getRootPath(): String? {
        return if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Environment.getExternalStorageDirectory().path
        } else {
            Environment.getRootDirectory().path
        }
    }

    /**
     * 写入本地文件
     * @param context
     * @param obj
     * @param fileName
     */
    fun write(
        context: Context,
        obj: Any?,
        fileName: String?
    ) {
        try {
            val bout = ByteArrayOutputStream()
            val oout = ObjectOutputStream(bout)
            oout.writeObject(obj)
            oout.flush()
            oout.close()
            bout.close()
            val b = bout.toByteArray()
            val file = File(context.filesDir, fileName)
            val out = FileOutputStream(file)
            out.write(b)
            out.flush()
            out.close()
        } catch (e: Exception) {
        } finally {
        }
    }

    /**
     * 从本地文件读取
     * @param context
     * @param fileName
     * @return
     */
    fun read(context: Context, fileName: String?): Any? {
        // 拿出持久化数据
        var obj: Any? = null
        try {
            val file = File(context.filesDir, fileName)
            val `in` = FileInputStream(file)
            val oin = ObjectInputStream(`in`)
            obj = oin.readObject()
            `in`.close()
            oin.close()
        } catch (e: Exception) {
        }
        return obj
    }

    //保存图片到相册
    fun saveImageToGallery(
        context: Context,
        path: String
    ) {
        val file = File(path)
        //其次把文件插入到系统图库
        try {
            val filename = path.substring(path.lastIndexOf('/') + 1)
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath, filename, null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        // 通知图库更新
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            MediaScannerConnection.scanFile(
                context,
                arrayOf(file.absolutePath),
                null
            ) { path, uri ->
                val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                mediaScanIntent.data = uri
                context.sendBroadcast(mediaScanIntent)
            }
        } else {
            val relationDir = file.parent
            val file1 = File(relationDir)
            context.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.fromFile(file1.absoluteFile)
                )
            )
        }
    }
}