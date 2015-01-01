package in.spud.huixiang.util;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by spud on 15/1/1.
 */
public class FileUtils {


    private static final String INTERNAL_COMMON_PATH = "files";

    public static String loadInternalFile(Context ctx, String fileName) {
        File dir = ctx.getDir(INTERNAL_COMMON_PATH, Context.MODE_PRIVATE);
        try {
            if (dir != null) {
                byte[] data = getDataFromFile(dir, fileName);
                return new String(data, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveInternalFile(Context ctx, String dataStr, String fileName) {
        if (dataStr != null) {
            File dir = ctx.getDir(INTERNAL_COMMON_PATH, Context.MODE_PRIVATE);
            try {
                if (dir != null) {
                    byte[] data = dataStr.getBytes("utf-8");
                    saveFile(dir, fileName, data);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    private static void saveFile(File dir, String filename, byte[] data) {
        File file = new File(dir, filename);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] getDataFromFile(File dir, String fileName) {
        File file = new File(dir, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return readDataFromStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static byte[] readDataFromStream(FileInputStream stream) {

        if (stream == null) {
            return null;
        }

        byte[] data = null;
        BufferedInputStream bis;
        bis = new BufferedInputStream(stream);

        int length;
        try {
            length = bis.available();
            data = new byte[length];
            bis.read(data);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null)
                    bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
