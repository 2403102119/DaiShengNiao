package com.lxkj.dsn.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 小火
 * Create time on  2017/3/22
 * My mailbox is 1403241630@qq.com
 */

public class StringUtils {
    /**
     * 判断字符串是否有内容
     */
    public static boolean isNotEmpty(CharSequence str) {
        return !TextUtils.isEmpty(str);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
        String num = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(num);
        }
    }
    /**
     * 判断密码格式是否正确
     * @param pwd
     * @return
     */
    public static boolean isPwd(String pwd) {
        String check = "^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,16})$";//
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(pwd);
        boolean isMatched = matcher.matches();
        return isMatched;
    }

    /**
     * 判断字符串是否符合密码的正则表达式规则
     */
    public static boolean isMatchesPwd(String pwd) {
        return pwd.matches("[\\da-zA-Z_]{6,20}");
    }


    /**
     * 判断字符串是否符合验证码的正则表达式规则
     */
    private static boolean isMatchesVerification(String text) {
        return text.matches("[\\d]{4}");
    }



    /**
     * @param oldString
     * @param unit
     * @return
     */
    public static String parseStringToNumber(String oldString, String unit) {
        if (TextUtils.isEmpty(oldString)) {
            return null;
        }
        if (!TextUtils.isEmpty(unit)) {
            int index = oldString.indexOf(unit);
            if (index != -1) {
                return oldString.substring(0, index);
            }
        }
        return oldString;
    }

    public static String modifyDataFormat(String str){
        String result;
        result = str.substring(0,4) + "."+ str.substring(5,7) + "."+ str.substring(8,10);
        return result;
    }
    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    /**
     * @param bmp 获取的bitmap数据
     * @param picName 自定义的图片名
     */
    public static String saveBmp2Gallery(Context context, Bitmap bmp, String picName) {
//        saveImageToGallery(bmp,picName);
        String fileName = null;
        //系统相册目录
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;


        // 声明文件对象
        File file = null;
        // 声明输出流
        FileOutputStream outStream = null;
        try {
            // 如果有目标文件，直接获得文件对象，否则创建一个以filename为名称的文件
            file = new File(galleryPath, picName + ".jpg");
            // 获得文件相对路径
            fileName = file.toString();
            // 获得输出流，如果文件中有内容，追加内容
            outStream = new FileOutputStream(fileName);
            if (null != outStream) {
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
            }
        }catch (Exception e) {
            e.getStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MediaStore.Images.Media.insertImage(context.getContentResolver(),bmp,fileName,null);
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        intent.setData(uri);
        context.sendBroadcast(intent);

        Toast.makeText(context,"图片保存成功",Toast.LENGTH_SHORT).show();
        return "1";
    }
}
