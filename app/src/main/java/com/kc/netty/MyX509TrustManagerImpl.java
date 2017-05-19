package com.kc.netty;

import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * 验证服务器端的
 *
 * @author clear
 */
public class MyX509TrustManagerImpl implements X509TrustManager {

    /*
    * The default X509TrustManager returned by SunX509.  We'll delegate
    * decisions to it, and fall back to the logic in this class if the
    * default X509TrustManager doesn't trust it.
    */
    X509TrustManager sunJSSEX509TrustManager;

    public MyX509TrustManagerImpl() throws Exception {
        // create a "default" JSSE X509TrustManager.
        KeyStore ks = KeyStore.getInstance("BKS");
//        ks.load(JYApplication.serverInputStream,
//                "123456".toCharArray());
//        JYApplication.serverInputStream.close();

        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//        TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        TrustManager tms[] = tmf.getTrustManagers();
        /*
         * Iterate over the returned trustmanagers, look
         * for an instance of X509TrustManager.  If found,
         * use that as our "default" trust manager.
         */
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                sunJSSEX509TrustManager = (X509TrustManager) tms[i];
                return;
            }
        }
        /*
         * Find some other way to initialize, or else we have to fail the
         * constructor.
         */
        throw new Exception("Couldn't initialize");
    }

    /*
     * Delegate to the default trust manager.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
//        try {
//            sunJSSEX509TrustManager.checkClientTrusted(chain, authType);
//        } catch (CertificateException excep) {
//            // do any special handling here, or rethrow exception.
//        }

        System.out.print("checkClientTrusted");
    }

    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        System.out.print("checkServerTrusted");

//    	 throw new CertificateException();
        try {
            sunJSSEX509TrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {
            /*
             * Possibly pop up a dialog box asking whether to trust the
             * cert chain.
             */
            excep.printStackTrace();
        }
    }

    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return sunJSSEX509TrustManager.getAcceptedIssuers();
    }

}