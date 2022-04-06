package com.fyr.learning.commons.http;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

@Component
public class RestTemplateConfigurar {
    private static final Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfigurar.class);

    @Autowired
    private ResourcePatternResolver resourcePatternResolver;

    // @Bean
    // public RestTemplate restTemplate() {
    //     OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
    //             .connectTimeout(60, TimeUnit.SECONDS)
    //             .readTimeout(30, TimeUnit.SECONDS)
    //             .hostnameVerifier((urlHostName, sslSession) -> true)
    //             .sslSocketFactory(buildSSLSocketFactory(), new AuthX509TrustManager())
    //             .build();
    //
    // }


    @Bean
    public RestTemplate restTemplate() {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");//设置证书类型，X.509是一种格式标准

            //证书类型
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());//KeyStore 是一个存储了证书的文件。文件包含证书的私钥，公钥和对应的数字证书的信息。
            keyStore.load(resourcePatternResolver.getResource("classpath:server.jks").getInputStream(), "server.jks".toCharArray());


            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);//通过keyStore得到信任管理器

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, "server.jks".toCharArray());//通过keyStore得到密匙管理器

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();//拿到SSLSocketFactory

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                return null;
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .hostnameVerifier((urlHostName, sslSession) -> true)
                    .sslSocketFactory(sslSocketFactory, trustManager)
                    .build();

            return new RestTemplate(new OkHttp3ClientHttpRequestFactory(okHttpClient));

        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        LOGGER.error("*** failed to initialize rest client to access http api ...");
        }

        LOGGER.error("*** failed to initialize rest client to access http api ...");
        return null;
    }
}
