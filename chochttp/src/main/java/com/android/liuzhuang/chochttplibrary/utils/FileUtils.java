package com.android.liuzhuang.chochttplibrary.utils;

import android.text.TextUtils;

import com.android.liuzhuang.chochttplibrary.utils.CheckUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhuang on 16/1/30.
 */
public class FileUtils {
    public static final String NEW_LINE = "\r\n";

    private final static String DOT = ".";

    private final static int BUFFER_SIZE = 1024 * 1024;

    private FileUtils() {

    }

    /**
     * 以默认字符集格式，读入指定文件的内容
     *
     * @param filePath 文件的绝对路径
     * @return 以默认字符集的方式来读取文件所有内容
     */
    public static String readFile(String filePath) throws IOException {
        return readFile(filePath, Charset.defaultCharset().name());
    }


    /**
     * 读入指定文件的内容
     *
     * @param filePath    文件的绝对路径
     * @param charsetName 文件读取的字符集方式
     * @return 读取文件的所有内容
     */
    public static String readFile(String filePath, String charsetName) throws IOException {
        File file = new File(filePath);
        StringBuilder sb = new StringBuilder();
        if (!file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            while ((line = reader.readLine()) != null) {
                sb.append(line).append(NEW_LINE);
            }
            //去除最后一行的换行字符
            if (sb.length() > NEW_LINE.length()) {
                sb.delete(sb.length() - NEW_LINE.length(), sb.length());
            }
        } finally {
            IOUtils.close(reader);
        }
        return sb.toString();
    }

    /**
     * 以默认字符集格式，读入指定文件的内容
     *
     * @param filePath
     * @return
     */
    public static List<String> readFile2List(String filePath) throws IOException {
        return readFile2List(filePath, Charset.defaultCharset().name());
    }

    /**
     * 读入指定文件的内容
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(String filePath, String charsetName) throws IOException {
        File file = new File(filePath);
        List<String> sb = new ArrayList<String>();
        if (!file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        String line;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            while ((line = reader.readLine()) != null) {
                sb.add(line);
            }
        } finally {
            IOUtils.close(reader);
        }
        return sb;
    }

    public static Object readObjectFromFile(String filePath) throws IOException, ClassNotFoundException {
        ObjectInputStream objectinputstream = null;
        FileInputStream streamIn = null;
        Object readCase = null;
        try {
            streamIn = new FileInputStream(filePath);
            objectinputstream = new ObjectInputStream(streamIn);
            readCase = objectinputstream.readObject();
        } finally {
            IOUtils.closeQuietly(objectinputstream, streamIn);
        }
        return readCase;
    }


    public static boolean writeFile(String filePath, Object object) throws IOException {
        FileOutputStream fout = null;
        ObjectOutputStream oos = null;
        try {
            fout = new FileOutputStream(filePath);
            oos = new ObjectOutputStream(fout);
            oos.writeObject(object);
            return true;
        } finally {
            IOUtils.closeQuietly(fout, oos);
        }
    }

    /**
     * 将指定字符串写入到文件中
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) throws IOException {
        return writeFile(filePath, content, false);
    }

    /**
     * 将指定字符串写入到文件中
     *
     * @param filePath
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFile(String filePath, String content, boolean append) throws IOException {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            createDir(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } finally {
            IOUtils.close(fileWriter);
        }
    }

    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param filePath
     * @param stream
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream) throws IOException {
        return writeFile(filePath, stream, false);
    }

    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param filePath
     * @param stream
     * @param append
     * @return
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) throws IOException {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param file
     * @param stream
     * @return
     */
    public static boolean writeFile(File file, InputStream stream) throws IOException {
        return writeFile(file, stream, false);
    }

    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param file
     * @param stream
     * @param append
     * @return
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) throws IOException {
        OutputStream o = null;
        try {
            createDir(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } finally {
            IOUtils.close(o, stream);
        }
    }


    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param filePath
     * @param list
     * @param append
     * @return
     */
    public static boolean writeFile(String filePath, List<String> list, boolean append) throws IOException {
        if (list == null || list.size() == 0) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            createDir(filePath);
            fileWriter = new FileWriter(filePath, append);
            int i = 0;
            for (String line : list) {
                if (i++ > 0) {
                    fileWriter.write(NEW_LINE);
                }
                fileWriter.write(line);
            }
            return true;
        } finally {
            IOUtils.close(fileWriter);
        }
    }

    /**
     * 文件移动
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    public static void moveFile(String sourceFilePath, String destFilePath) throws IOException {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw new RuntimeException("Both sourceFilePath and destFilePath cannot be null.");
        }
        moveFile(new File(sourceFilePath), new File(destFilePath));
    }

    /**
     * 文件移动
     *
     * @param srcFile
     * @param destFile
     */
    public static void moveFile(File srcFile, File destFile) throws IOException {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    public static void copyFile(File src, File des) throws IOException {
        InputStream fi = null;
        OutputStream fo = null;
        try {
            fi = new BufferedInputStream(new FileInputStream(src));
            fo = new BufferedOutputStream(new FileOutputStream(des));
            byte[] buf = new byte[2048];
            int i;
            while ((i = fi.read(buf)) != -1) {
                fo.write(buf, 0, i);
            }

        } finally {
            IOUtils.closeQuietly(fi, fo);
        }
    }

    /**
     * 文件拷贝
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return writeFile(destFilePath, inputStream);
    }


    public static void copyFolder(String srcDir, String desDir) throws IOException {
        createFolder(desDir);
        File[] files = (new File(srcDir)).listFiles();
        if (files == null || files.length == 0) {
            return;
        }
        for (File f : files) {
            if (f.isFile()) {
                File des = new File(desDir + File.separator + f.getName());
                copyFile(f, des);
            }
            if (f.isDirectory()) {
                copyFolder(srcDir + "/" + f.getName(), desDir + "/" + f.getName());
            }
        }
    }

    /**
     * 复制文件
     * <p/>
     * 通过该方式复制文件文件越大速度越是明显
     */
    public static boolean copyByChannel(String source, String target) throws IOException {
        FileInputStream fin = null;
        FileOutputStream fout = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fin = new FileInputStream(new File(source));
            fout = new FileOutputStream(new File(target));

            in = fin.getChannel();
            out = fout.getChannel();
            //设定缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            while (in.read(buffer) != -1) {
                //准备写入，防止其他读取，锁住文件
                buffer.flip();
                out.write(buffer);
                //准备读取。将缓冲区清理完毕，移动文件内部指针
                buffer.clear();
            }
        } finally {
            IOUtils.closeQuietly(in, out, fin, fout);
        }
        return false;
    }

    /**
     * 从文件路径中，提取文件夹的名称
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos < 0) ? "" : filePath.substring(0, filePos);
    }

    /**
     * 从文件路径中，提取文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePos = filePath.lastIndexOf(File.separator);
        return (filePos < 0) ? filePath : filePath.substring(filePos + 1);
    }

    /**
     * 从文件路径中，提取文件的扩展名
     *
     * @param filePath
     * @return
     */
    public static String getExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int exPos = filePath.lastIndexOf(DOT);
        if (exPos == -1) {
            return "";
        }

        return filePath.substring(exPos + 1);
    }

    /**
     * 从文件路径中，提取不包含扩展名的文件名
     *
     * @param filePath
     * @return
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int exPos = filePath.lastIndexOf(DOT);
        int filePos = filePath.lastIndexOf(File.separator);
        if (filePos == -1) {
            return (exPos == -1 ? filePath : filePath.substring(0, exPos));
        }
        if (exPos == -1) {
            return filePath.substring(filePos + 1);
        }
        return (filePos < exPos ? filePath.substring(filePos + 1, exPos) : filePath.substring(filePos + 1));
    }


    public static boolean createFolder(String filePath) {
        return createDir(filePath);
    }

    /**
     * 创建文件夹
     *
     * @param filePath
     * @return
     */
    public static boolean createDir(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) || folder.mkdirs();
    }

    /**
     * 创建文件
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static boolean createFile(String filePath) throws IOException {
        if (!createDir(filePath)) {
            return false;
        }
        File file = new File(filePath);
        return (file.exists() && file.isFile()) || file.createNewFile();
    }

    /**
     * 遍历删除文件或目录
     *
     * @param filePath
     * @return
     */
    public static boolean deleteFile(String filePath) {
        if (CheckUtil.isEmpty(filePath)) {
            return true;
        }
        return deleteFile(new File(filePath));
    }

    /**
     * 遍历删除文件或目录
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        if (file.isFile()) {
            return file.delete();
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return file.delete();
            }
            for (File f : childFiles) {
                deleteFile(f);
            }
            return file.delete();
        }
        return false;
    }

    /**
     * 判断指定路径的文件或者文件夹是否存在
     *
     * @param path
     * @return
     */
    public static boolean checkPath(String path) {
        if (CheckUtil.isEmpty(path)) {
            return false;
        }

        return new File(path).exists();
    }

    /**
     * 判断文件夹是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFolderExist(String filePath) {
        if (CheckUtil.isEmpty(filePath)) {
            return false;
        }

        File dir = new File(filePath);
        return (dir.exists() && dir.isDirectory());
    }


    /**
     * 判断文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (CheckUtil.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * 列举指定路径下的所有文件与文件夹
     *
     * @param dir
     * @return
     */
    public static String[] listFolderOrFile(String dir) {
        if (CheckUtil.isEmpty(dir)) {
            return null;
        }

        return new File(dir).list();
    }

    /**
     * 列举指定路径下的所有文件夹
     *
     * @param dir
     * @return
     */
    public static String[] listFolders(String dir) {
        if (CheckUtil.isEmpty(dir)) {
            return null;
        }

        String[] folders = new File(dir).list(
                new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String filename) {
                        return new File(dir, filename).isDirectory();
                    }
                });
        return folders;
    }

    /**
     * 列举指定路径下的所有文件
     *
     * @param dir
     * @return
     */
    public static String[] listFiles(String dir) {
        if (CheckUtil.isEmpty(dir)) {
            return null;
        }

        String[] files = new File(dir).list(
                new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String filename) {
                        return new File(dir, filename).isFile();
                    }
                });
        return files;
    }
}