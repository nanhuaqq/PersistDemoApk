package com.qwx.persist.utils;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.blankj.utilcode.util.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class FileUtil {

    /**
     * Regular expression for safe filenames: no spaces or metacharacters
     */
    private static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");

    private FileUtil() {

    }

    /**
     * Check if a filename is "safe" (no metacharacters or spaces).
     *
     * @param file The file to check
     */
    public static boolean isFilenameSafe(File file) {
        // Note, we check whether it matches what's known to be safe,
        // rather than what's known to be unsafe. Non-ASCII, control
        // characters, etc. are all unsafe by default.
        return SAFE_FILENAME_PATTERN.matcher(file.getPath()).matches();
    }


    // 复制文件
    public static void copyFile(String src, String dest) {

        FileInputStream in = openFileInputStream(src);
        if (in == null) {
            return;
        }
        try {
            IOUtils.copyStream(in, new File(dest));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static void copyFile(File src, File dest) throws IOException {

        if (src.exists()) {
            FileChannel channel1=null;
            FileChannel channel2=null;
            try{
                channel1= new FileInputStream(src).getChannel();
                channel2 = new FileOutputStream(dest).getChannel();
                channel1.transferTo(0, channel1.size(), channel2);
            }finally {
                IOUtils.closeQuietly(channel1,channel2);
            }
        }
    }

    public static void copyDirectory(File src, File dest) throws IOException {

        if (src.exists()) {
            dest.mkdirs();
            File[] files = src.listFiles();
            if (files == null) {
                return;
            }
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (file.isDirectory()) {
                    copyDirectory(file, new File(dest, file.getName()));
                } else {
                    copyFile(file, new File(dest, file.getName()));
                }
            }
        }
    }

    public static void ensureDir(File file) {

        if (file == null) {

            return;
        }
        if (file.exists()) {

            if (file.isFile()) {

                file.delete();
                file.mkdirs();
            }
        } else {

            file.mkdirs();
        }
    }

    public static boolean ensureMkdir(final File dir) {

        if (dir == null) {
            return false;
        }
        File tempDir = dir;
        int i = 1;
        while (tempDir.exists()) {
            tempDir = new File(dir.getParent(), dir.getName() + "(" + i + ")");
            i++;
        }
        return tempDir.mkdir();
    }

    public static void ensureParent(final File file) {

        if (null != file) {
            final File parentFile = file.getParentFile();
            if (null != parentFile && !parentFile.exists()) {
                parentFile.mkdirs();
            }
        }
    }

    /**
     * Retrieve the main file name.
     *
     * @param path the file name.
     * @return the main file name without the extension.
     */
    public static String getFileNameWithoutExtensionByPath(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getFileNameWithoutExtension(new File(path));
    }

    /**
     * Retrieve the main file name.
     *
     * @param file the file.
     * @return the main file name without the extension.
     */
    public static String getFileNameWithoutExtension(File file) {

        if (file == null) {
            return null;
        }
        String fileName = file.getName();
        return getFileNameWithoutExtension(fileName);
    }

    /**
     * Helper method to get a filename without its extension
     *
     * @param fileName String
     * @return String
     */
    public static String getFileNameWithoutExtension(String fileName) {

        String name = fileName;

        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            name = fileName.substring(0, index);
        }

        return name;
    }

    /**
     * Retrieve the main file name.
     *
     * @param path the file name.
     * @return the extension of the file.
     */
    public static String getExtension(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return getExtension(new File(path));
    }

    /**
     * Retrieve the extension of the file.
     *
     * @param file the file.
     * @return the extension of the file.
     */
    public static String getExtension(File file) {

        if (null == file) {
            return null;
        }
        final String name = file.getName();
        final int index = name.lastIndexOf('.');
        String extension = "";
        if (index >= 0) {
            extension = name.substring(index + 1);
        }
        return extension;
    }

    public static boolean existFile(String path) {

        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return existFile(new File(path));
    }

    public static boolean existFile(File file) {

        return file != null && file.exists() && file.isFile();
    }

    public static boolean deleteFileIfExist(String path) {

        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    public static boolean deleteFileIfExist(File file) {

        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件，如果为文件夹则递归删除整体文件夹
     *
     * @param file
     */
    public static void deleteFile(File file) {

        if (file == null || !file.exists()) {

            return;
        }
        if (file.isDirectory()) {

            File[] files = file.listFiles();
            if (files == null || files.length == 0) {

                return;
            }
            for (File child : files) {

                deleteFile(child);
            }
        }
        deleteFileIfExist(file);
    }

    public static void saveToFile(File file, String text) {

        saveToFile(file, text, false, "utf-8");
    }

    public static void saveToFile(File file, String text, boolean append) {

        saveToFile(file, text, append, "utf-8");
    }

    public static void saveToFile(File file, String text, String encoding) {

        saveToFile(file, text, false, encoding);
    }

    public static void saveToFile(File file, String text, boolean append, String encoding) {

        if (file == null || TextUtils.isEmpty(text)) {
            return;
        }
        ensureParent(file);
        OutputStreamWriter writer = null;
        try {
            writer = new OutputStreamWriter(new FileOutputStream(file, append), encoding);
            writer.write(text);
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(writer);
        }
    }

    public static void saveToFile(File file, byte[] data) {

        ensureParent(file);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }

    public static String readFile(String path) {

        if (TextUtils.isEmpty(path)) {
            return null;
        }
        return readFile(new File(path));
    }

    public static String readFile(File file) {

        String text = null;
        if (existFile(file)) {
            FileInputStream fis = openFileInputStream(file);
            if (fis != null) {
                try {
                    text = IOUtils.loadContent(fis);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(fis);
                }
            }
        }
        return text;
    }

    public static byte[] readFileBytes(File file) {

        byte[] data = null;
        FileInputStream fis = openFileInputStream(file);
        if (fis != null) {
            data = IOUtils.loadBytes(fis);
            IOUtils.closeQuietly(fis);
        }
        return data;
    }

    public static Map<String, String> readConfig(File file) {

        Map<String, String> map = new HashMap<String, String>();
        String text = readFile(file);
        if (TextUtils.isEmpty(text)) {
            return map;
        }

        String[] lines = text.split("\n");
        for (String line : lines) {
            line = line.trim();
            if (TextUtils.isEmpty(line)) {
                continue;
            } else if (line.startsWith("#")) {
                continue;
            }
            String[] array = line.split("=", 2);
            map.put(array[0].trim(), array[1].trim());
        }
        return map;
    }

    private static FileInputStream openFileInputStream(String path) {

        try {
            return new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static FileInputStream openFileInputStream(File file) {

        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static FileOutputStream openFileOutputStream(String path) {

        try {
            return new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static FileOutputStream openNewFileOutput(File file) throws IOException {

        deleteFileIfExist(file);
        ensureParent(file);
        file.createNewFile();
        return new FileOutputStream(file);
    }

    public static File getUserDir() {

        String path = System.getProperty("user.dir");
        return new File(path);
    }

    public static File getUserHome() {

        String path = System.getProperty("user.home");
        return new File(path);
    }

    public static String getRealPathFromUri(Context context, Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && DocumentsContract.isDocumentUri(context, uri)) {
            return getRealPathForKitkat(context, uri);
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getRealPathForKitkat(Context context, Uri uri) {

        // ExternalStorageProvider
        if (isExternalStorageDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            if ("primary".equalsIgnoreCase(type)) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }

            // TODO handle non-primary volumes
        }
        // DownloadsProvider
        else if (isDownloadsDocument(uri)) {

            final String id = DocumentsContract.getDocumentId(uri);
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

            return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];

            Uri contentUri = null;
            if ("image".equals(type)) {
                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(type)) {
                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(type)) {
                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }

            final String selection = "_id=?";
            final String[] selectionArgs = new String[]{split[1]};
            return getDataColumn(context, contentUri, selection, selectionArgs);
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {

        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {

        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {

        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * 绝对路径转为file://开头的uri
     *
     * @param path
     * @return
     */
    public static Uri parseUri(String path) {

        return Uri.parse("file://" + path);
    }

    /**
     * 本地绝对路径转换为file://开头的uri
     *
     * @param path
     * @return
     */
    public static String getUriString(String path) {

        return "file://" + path;
    }

    /**
     * 获取文件或文件夹的大小
     *
     * @return
     */
    public static long getFileSize(File file) {

        long size = 0;
        if (file == null || !file.exists()) {

            return 0;
        }
        if (file.isDirectory()) {

            File[] childs = file.listFiles();
            if (childs == null || childs.length == 0) {

                return 0;
            }
            for (File child : childs) {

                size += getFileSize(child);
            }
        }
        FileInputStream fileInputStream = null;
        try {

            fileInputStream = new FileInputStream(file);
            size = fileInputStream.available();
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {

            IOUtilities.closeStream(fileInputStream);
        }
        return size;
    }

    public static void copyAssets(Context context, String oldPath, String newPath) {
        try {
                InputStream is = context.getAssets().open(oldPath);
            try {
                IOUtils.copyStream(is, new File(newPath));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String loadFromPath(String path) {
        String result= null;
        try {
            File f=new File(path);
            int length=(int)f.length();
            byte[] buff=new byte[length];
            FileInputStream fin=new FileInputStream(f);
            fin.read(buff);
            fin.close();
            result=new String(buff,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
