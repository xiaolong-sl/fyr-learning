package com.fyr.learning.server.https.common;

import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class AuthX509TrustManager implements X509TrustManager {
    /*
     * The default X509TrustManager returned by IbmX509. We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509TrustManager doesn't trust it.
     */
    X509TrustManager pkixTrustManager;

    public AuthX509TrustManager() {
        try {
            KeyStore keyStore = KeyStore.getInstance("JKS");
            // keyStore.load(resourcePatternResolver.getResource("classpath:server.jks").getInputStream(), "server.jks".toCharArray());


            keyStore.load(new FileInputStream(ResourceUtils.getFile("classpath:server.jks")), "server.jks".toCharArray());

            TrustManagerFactory tmf =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);

            TrustManager trustManagers[] = tmf.getTrustManagers();

            /*
             * Iterate over the returned trustManagers, look
             * for an instance of X509TrustManager. If found,
             * use that as our default trust manager.
             */
            for (int i = 0; i < trustManagers.length; i++) {
                if (trustManagers[i] instanceof X509TrustManager) {
                    pkixTrustManager = (X509TrustManager) trustManagers[i];
                    return;
                }
            }

            /*
             * Find some other way to initialize, or else we have to fail the
             * constructor.
             */
            throw new Exception("Couldn't initialize");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Delegate to the default trust manager.
     */
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            pkixTrustManager.checkClientTrusted(chain, authType);
        } catch (CertificateException excep) {
            // do any special handling here, or rethrow exception.
        }
    }

    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            pkixTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {
            /*
             * Possibly pop up a dialog box asking whether to trust the
             * cert chain.
             */
        }
    }

    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return pkixTrustManager.getAcceptedIssuers();
    }
}
