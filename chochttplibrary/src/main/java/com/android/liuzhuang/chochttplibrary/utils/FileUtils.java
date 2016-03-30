package com.android.liuzhuang.chochttplibrary.utils;


import java.io.*;
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
    public static String readFile(String filePath) {
        return readFile(filePath, Charset.defaultCharset().name());
    }


    /**
     * 读入指定文件的内容
     *
     * @param filePath    文件的绝对路径
     * @param charsetName 文件读取的字符集方式
     * @return 读取文件的所有内容
     */
    public static String readFile(String filePath, String charsetName) {
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
        } catch (IOException e) {
            throw new RuntimeException("IOException ", e);
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
    public static List<String> readFile2List(String filePath) {
        return readFile2List(filePath, Charset.defaultCharset().name());
    }

    /**
     * 读入指定文件的内容
     *
     * @param filePath
     * @param charsetName
     * @return
     */
    public static List<String> readFile2List(String filePath, String charsetName) {
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
        } catch (IOException e) {
            throw new RuntimeException("IOException ", e);
        } finally {
            IOUtils.close(reader);
        }
        return sb;
    }

    /**
     * 将指定字符串写入到文件中
     *
     * @param file
     * @param content
     * @return
     */
    public static boolean writeFile(File file, String content) {
        return writeFile(file.getAbsoluteFile(), content);
    }

    /**
     * 将指定字符串写入到文件中
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }

    /**
     * 将指定字符串写入到文件中
     *
     * @param file
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFile(File file, String content, boolean append) {
        return writeFile(file.getAbsoluteFile(), content, append);
    }

    /**
     * 将指定字符串写入到文件中
     *
     * @param filePath
     * @param content
     * @param append
     * @return
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        if (CheckUtil.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;
        try {
            createDir(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException.", e);
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
    public static boolean writeFile(String filePath, InputStream stream) {
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
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param file
     * @param stream
     * @return
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

//    public static void writeFile(String dir, Throwable throwable) {
//        File file = new File(dir);
//        PrintStream ps = null;
//        try {
//            ps = new PrintStream(file);
//            throwable.printStackTrace(ps);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (ps != null) {
//                ps.close();
//            }
//        }
//    }

    /**
     * 从输入流中获取字符串，并全部写入到文件中
     *
     * @param file
     * @param stream
     * @param append
     * @return
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
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
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException ", e);
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
    public static boolean writeFile(String filePath, List<String> list, boolean append) {
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
        } catch (IOException e) {
            throw new RuntimeException("IOException.", e);
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
    public static void moveFile(String sourceFilePath, String destFilePath) {
        if (CheckUtil.isEmpty(sourceFilePath) || CheckUtil.isEmpty(destFilePath)) {
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
    public static void moveFile(File srcFile, File destFile) {
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile.getAbsolutePath(), destFile.getAbsolutePath());
            deleteFile(srcFile.getAbsolutePath());
        }
    }

    public static void copyFile(File src, File des) {
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

        } catch (IOException e) {
            e.printStackTrace();
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
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException ", e);
        }
        return writeFile(destFilePath, inputStream);
    }


    public static void copyFolder(String srcDir, String desDir) {
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
    public static boolean copyByChannel(String source, String target) {
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
        } catch (IOException e) {
            e.printStackTrace();
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

        if (CheckUtil.isEmpty(filePath)) {
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
        if (CheckUtil.isEmpty(filePath)) {
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
        if (CheckUtil.isEmpty(filePath)) {
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
        if (CheckUtil.isEmpty(filePath)) {
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
        if (CheckUtil.isEmpty(folderName)) {
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

                    public boolean accept(File dir, String filename) {
                        return new File(dir, filename).isFile();
                    }
                });
        return files;
    }
}
