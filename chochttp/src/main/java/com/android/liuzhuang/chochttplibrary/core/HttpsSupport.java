package com.android.liuzhuang.chochttplibrary.core;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * Created by liuzhuang on 16/4/24.
 */
public class HttpsSupport {
    private static SSLSocketFactory TRUSTED_FACTORY;

    /**
     * Configure HTTPS connection to trust all certificates
     * <p>
     * This method does nothing if the current request is not a HTTPS request
     *
     * @return this request
     * @throws IOException
     */
    public static void trustAllCerts(HttpURLConnection connection) throws IOException {
        if (connection instanceof HttpsURLConnection)
            ((HttpsURLConnection) connection)
                    .setSSLSocketFactory(getTrustedFactory());
    }

    private static SSLSocketFactory getTrustedFactory() throws IOException {
        if (TRUSTED_FACTORY == null) {
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // Intentionally left blank
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // Intentionally left blank
                }
            } };
            try {
                SSLContext context = SSLContext.getInstance("TLS");
                context.init(null, trustAllCerts, new SecureRandom());
                TRUSTED_FACTORY = context.getSocketFactory();
            } catch (GeneralSecurityException e) {
                IOException ioException = new IOException(
                        "Security exception configuring SSL context");
                ioException.initCause(e);
                throw ioException;
            }
        }

        return TRUSTED_FACTORY;
    }
}
