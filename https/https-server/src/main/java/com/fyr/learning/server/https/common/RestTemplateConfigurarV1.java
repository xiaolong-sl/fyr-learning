package com.fyr.learning.server.https.common;// package com.fyr.learning.commons.http;
//
// import okhttp3.OkHttpClient;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.context.annotation.Bean;
// import org.springframework.core.io.support.ResourcePatternResolver;
// import org.springframework.stereotype.Component;
// import org.springframework.web.client.RestTemplate;
//
// import javax.net.ssl.*;
// import java.io.InputStream;
// import java.security.*;
// import java.security.cert.Certificate;
// import java.security.cert.CertificateException;
// import java.security.cert.CertificateFactory;
// import java.util.List;
// import java.util.concurrent.TimeUnit;
//
// @Component
// public class RestTemplateConfigurarV1 {
//
//     @Autowired
//     private ResourcePatternResolver resourcePatternResolver;
//
//     @Bean
//     public RestTemplate restTemplate() {
//         OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
//                 .connectTimeout(60, TimeUnit.SECONDS)
//                 .readTimeout(30, TimeUnit.SECONDS)
//                 .hostnameVerifier((urlHostName, sslSession) -> true)
//                 .sslSocketFactory(buildSSLSocketFactory(), new AuthX509TrustManager())
//                 .build();
//
//     }
//
//     /**
//      * 构建SSLSocketFactory
//      *
//      * @param keyStoreType
//      * @param keyFilePath
//      * @param keyPassword
//      * @param sslProtocols
//      * @param auth         是否需要client默认相信不安全证书
//      * @return
//      * @throws Exception
//      */
//     private SSLConnectionSocketFactory buildSSLSocketFactory(String keyStoreType, String keyFilePath,
//                                                              String keyPassword, List<String> sslProtocols, boolean auth) throws Exception {
//         //证书管理器，指定证书及证书类型
//         KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//         //KeyStore用于存放证书，创建对象时 指定交换数字证书的加密标准
//         KeyStore keyStore = KeyStore.getInstance(keyStoreType);
//         InputStream inputStream = resourcePatternResolver.getResource(keyFilePath).getInputStream();
//         try {
//             //添加证书
//             keyStore.load(inputStream, keyPassword.toCharArray());
//         } finally {
//             inputStream.close();
//         }
//         keyManagerFactory.init(keyStore, keyPassword.toCharArray());
//
//         // SSLContext sslContext = SSLContext.getInstance("TLS");
//         SSLContext sslContext = SSLContext.getInstance("SSL");
//         if (auth) {
//             // 设置信任证书（绕过TrustStore验证）
//             TrustManager[] trustManagers = new TrustManager[]{
//                     new AuthX509TrustManager()
//             };
//             sslContext.init(keyManagerFactory.getKeyManagers(), trustManagers, new SecureRandom());
//             HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//         } else {
//             //加载证书材料，构建sslContext
//             sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, keyPassword.toCharArray()).build();
//         }
//
//         SSLConnectionSocketFactory sslConnectionSocketFactory =
//                 new SSLConnectionSocketFactory(sslContext, sslProtocols.toArray(new String[sslProtocols.size()]),
//                         null,
//                         new HostnameVerifier() {
//                             // 这里不校验hostname
//                             @Override
//                             public boolean verify(String urlHostName, SSLSession session) {
//                                 return true;
//                             }
//                         });
//
//         return sslConnectionSocketFactory;
//     }
//
//     public void X509() throws CertificateException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
//
//         CertificateFactory factory = CertificateFactory.getInstance("X.509");//设置证书类型，X.509是一种格式标准
//
//         //证书类型
//         KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());//KeyStore 是一个存储了证书的文件。文件包含证书的私钥，公钥和对应的数字证书的信息。
//         keyStore.load(null, null);
//         InputStream stream;
//         Certificate certificate;//Certificate是证书信息封装的一个bean类
//
//         if (cerPathList != null && !cerPathList.isEmpty()) {
//             for (int i = 0; i < cerPathList.size(); i++) {
//                 stream = context.getAssets().open(cerPathList.get(i));
//                 certificate = factory.generateCertificate(stream);
//                 //证书类型
//                 keyStore.setCertificateEntry("alias" + i, certificate);//将每个证书封装类以键值对的方式存入KeyStore
//             }
//         }
//
//         TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
//         trustManagerFactory.init(keyStore);//通过keyStore得到信任管理器
//
//         KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
//         keyManagerFactory.init(keyStore, "pwd".toCharArray());//通过keyStore得到密匙管理器
//
//         SSLContext sslContext = SSLContext.getInstance("TLS");
//         sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
//         SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();//拿到SSLSocketFactory
//
//         TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
//         if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
//             return null;
//         }
//         X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
//         okHttpClient.sslSocketFactory(sslSocketFactory, trustManager)//设置ssl证书
//         okHttpClient.build();
//     }
// }
