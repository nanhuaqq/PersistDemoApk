package com.qwx.persist.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CrashHandlerUtils implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "Util_CrashHandler";

    private Context mContext;

    private static CrashHandlerUtils INSTANCE = new CrashHandlerUtils();

    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandlerUtils getInstance() {
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx.getApplicationContext();
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread t, final Throwable e) {
        FileOutputStream fos=null;
        try {

            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            Throwable cause = e.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();

            File file = new File(getDiskCacheDir(mContext, "crash"), "crash.log");
            if (file.exists() && file.length() > 5 * 1024 * 1024) {     // <5M
                boolean delete = file.delete();
            }

            File folder = new File(getDiskCacheDir(mContext, "crash"));
            if (folder.exists() && folder.isDirectory()) {
                if (!file.exists()) {
                    boolean newFile = file.createNewFile();
                    if (!newFile) {
                        return;
                    }
                }
            } else {
                boolean mkDir = folder.mkdir();
                if (mkDir) {
                    boolean newFile = file.createNewFile();
                    if (!newFile) {
                        return;
                    }
                }
            }


            fos = new FileOutputStream(file, true);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
            String format = sdf.format(new Date());
            byte[] bytes = (format +        //追加Crash时间
                    "\n" + result +   //Crash内容
                    "\n" + "######## ~~~ T_T ~~~ ########" +
                    "\n").getBytes();
            fos.write(bytes);
            fos.flush();
        } catch (Exception exception) {
            exception.printStackTrace();
        }finally {
            IOUtils.closeQuietly(fos);
        }

        //crash后重启
        /*ComponentName componentName = new ComponentName("com.", "com..MyJXApplication");
        Intent schemIntent = new Intent();
        schemIntent.setComponent(componentName);
        schemIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(schemIntent);*/
        mDefaultHandler.uncaughtException(t, e);    //该代码不执行的话程序无法终止
        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
    }

    /**
     * 获得Crash文件路径
     * <p>
     * getCacheDir和getFilesDir是放在/data/data/packagename下的，
     * 所以这个目录中的内容必须是root的手机在文件操作系统中才能看到。当然
     * 如果在应用程序中清空数据或者卸载应用，那么这俩个目录下的文件也将会
     * 被清空的。getExternalCacheDir和getExternalFilesDir是存放
     * 在/storage/sdcard0/Android/data/packagename下面的，这个是放
     * 在外置存储卡的，这个目录下的内容 可以使用文件浏览系统查看到，但是如果
     * 清空数据或者卸载应用，俩个目录下的文件也将被清空。或者也可以理解为带
     * external这样的是存储在外置sd卡的，而直接使用getFilesDir这种是放
     * 在/data/data下面的。
     *
     * @param context context
     * @param dirName dirName
     * @return dir
     */
    public static String getDiskCacheDir(Context context, String dirName) {
        String cachePath = null;
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File externalCacheDir = context.getExternalCacheDir();
            if (externalCacheDir != null) {
                cachePath = externalCacheDir.getPath();
            }
        }
        if (cachePath == null) {
            File cacheDir = context.getCacheDir();
            if ((cacheDir != null) && (cacheDir.exists())) {
                cachePath = cacheDir.getPath();
            }
        }
        //0/emulate/Android/data/data/com.***.********/crash/crash.log
        return cachePath + File.separator + dirName;
    }
}
