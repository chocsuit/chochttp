package com.android.liuzhuang.chochttplibrary.utils;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by liuzhuang on 16/1/30.
 */
public class IOUtils {
    private static final int EOF = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private IOUtils(){
    }


    /**
     * 关闭closeable对象
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                throw new RuntimeException("IOException", e);
            }
        }
    }

    /**
     * 静默方式关闭closeable对象，即不抛出异常
     *
     * @param closeable
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }

    /**
     * 关闭一组 closeable对象
     *
     * @param closeables
     */
    public static void close(Closeable... closeables){
        for(Closeable closeable: closeables){
            close(closeable);
        }
    }

    /**
     * 静默方式关闭一组 closeable对象，即不抛出异常
     *
     * @param closeables
     */
    public static void closeQuietly(Closeable... closeables) {
        for(Closeable closeable: closeables){
            closeQuietly(closeable);
        }
    }

    /**
     * Copies chars from a large (over 2GB) <code>Reader</code> to a <code>Writer</code>.
     * <p>
     * This method uses the provided buffer, so there is no need to use a
     * <code>BufferedReader</code>.
     * <p>
     *
     * @param input  the <code>Reader</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @param buffer the buffer to be used for the copy
     * @return the number of characters copied
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since 2.2
     */
    public static long copyLarge(final Reader input, final Writer output, final char [] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Copies bytes from an <code>InputStream</code> to chars on a
     * <code>Writer</code> using the specified character encoding.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * This method uses {@link InputStreamReader}.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>Writer</code> to write to
     * @param inputEncoding  the encoding to use for the input stream, null means platform default
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since 2.3
     */
    public static void copy(final InputStream input, final Writer output, final Charset inputEncoding) throws IOException {
        final InputStreamReader in = new InputStreamReader(input, inputEncoding);
        copyLarge(in, output, new char[DEFAULT_BUFFER_SIZE]);
    }
}
