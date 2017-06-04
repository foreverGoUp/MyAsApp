package com.kc.netty;

import android.os.Handler;

import com.kc.util.Log;

import java.util.concurrent.TimeUnit;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by Administrator on 2016/12/14.
 */
public class MyChannelInitializer extends ChannelInitializer<Channel> {

    private final String TAG = "MyChannelInitializer";
    private Handler mHandler = null;
//    private LoggingHandler mLoggingHandler = new LoggingHandler(LogLevel.DEBUG);
//    private IdleStateHandler mIdleStateHandler = new IdleStateHandler(0, 50, 0, TimeUnit.SECONDS);
//    private MessageEncoder mMessageEncoder = new MessageEncoder();
//    private MessageDecoder mMessageDecoder = new MessageDecoder();
//    private AcceptorIdleStateTrigger mAcceptorIdleStateTrigger = AcceptorIdleStateTrigger.getInstance();

    public MyChannelInitializer(Handler handler) {
        this.mHandler = handler;
    }
//    private static MyChannelInitializer INSTANCE;
//    public static MyChannelInitializer getInstance(Handler handler) {
//        if (INSTANCE == null) {
//            LogUtil.e(ChannelInitializer.class, "！！！new ChannelInitializer");
//            INSTANCE = new MyChannelInitializer(handler);
//        }
//        return INSTANCE;
//    }


    @Override
    protected void initChannel(Channel ch) throws Exception {
        // TODO 自动生成的方法存根
        Log.e(TAG, "initChannel---Channel hashCode = " + ch.hashCode());
        ChannelPipeline pipeline = ch.pipeline();
//
//							/* 1.获取信任的证书列表
//							 * 2.初始化key manager factory
//							 * 3.初始化ssl context
//							 * 4.创建ssl引擎把引擎加入handler
//							 */
//							System.setProperty("java.protocol.handler.pkgs", "javax.net.ssl");
//						     HostnameVerifier hv = new HostnameVerifier() {
//						    	 @Override
//						          public boolean verify(String urlHostName, SSLSession session) {
//						           return urlHostName.equals(session.getPeerHost());
//						          }
//						     };
//						     HttpsURLConnection.setDefaultHostnameVerifier(hv);
//
//
//						    String keyName = "client";
//						    char[] keyStorePwd = "123456".toCharArray();
//						    char[] keyPwd = "123456".toCharArray();
//						    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
//
//						    // 装载当前目录下的key store. 可用jdk中的keytool工具生成keystore
////						    InputStream in = NettyUtil.getResources().openRawResource(R.raw.client);
//						    Log.e("******", ""+WelcomeActivity.clientInputStream);
//						    keyStore.load(WelcomeActivity.clientInputStream, keyPwd);
//						    WelcomeActivity.clientInputStream.close();
//
//						    // 初始化key manager factory
//						    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
//						            .getDefaultAlgorithm());
//						    kmf.init(keyStore, keyPwd);
//
//						    // 初始化ssl context
//						    SSLContext context = SSLContext.getInstance("SSL");
//						    context.init(kmf.getKeyManagers(),
//						            new TrustManager[] {  new MyX509TrustManagerImpl()},
//						            new SecureRandom());
//
//						    SSLEngine engine = context.createSSLEngine();  //2
////						    engine.setEnabledCipherSuites(engine.getEnabledCipherSuites());
//					        engine.setUseClientMode(true); //3
//
//					        engine.setUseClientMode(false);
//					        engine.setSSLParameters(arg0)
//					        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast(new LoggingHandler(LogLevel.DEBUG));
//						    pipeline.addLast("sslHandler", new SslHandler(engine));
//						    pipeline.addFirst("sslHandler", new SslHandler(engine));
        Log.e(TAG, "！！！initChannel");
        pipeline.addLast(new IdleStateHandler(0, 50, 0, TimeUnit.SECONDS));
        pipeline.addLast("encoder", new MessageEncoder());
        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast(new AcceptorIdleStateTrigger());
        pipeline.addLast("handle", new ClientUserHandler(mHandler));
    }

}

