package com.kc.netty;

import android.os.Handler;
import android.util.Log;

import com.kc.tool.AppConstant;
import com.kc.util.MyApp;
import com.kc.util.NetStatus;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @ModifiedBy 修改人
 * @Project NettyServer
 * @Desciption 客户端服务 1.建立连接 2.future用于传输数据
 * @Author SuFH
 * @Data 2016-4-29上午10:27:13
 */
public class NettyUtil {

    private static class HolderClass {
        private final static NettyUtil instance = new NettyUtil();
    }

    private NettyUtil() {
//        mHandler = PublicHandler.getInstant().getHandler();
    }

    public static NettyUtil getInstance() {
        return HolderClass.instance;
    }

    private String TAG = "NettyUtil";
    public static boolean sDebug = false;
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Bootstrap client = null;
    private int mConnectCount = 0;// 每个ip连接次数计数
    private final int mConnectLimit = 2;// 每个ip尝试连接2次
    // private boolean mReadConfig = false;
    // 表示连接状态，连接中或空闲中
    private final int STATE_CONNECTING = 0;
    private final int STATE_FREE = 1;
    private volatile int mCurrentState = STATE_FREE;
    // 当一个ip连接失败时累加，然后在下一个连接时根据累加值选择备用ip进行连接。
    private int mCurrentIpIndex = 1;

    private String mLanIp;// 局域网ip
    private String mIp;
    private int mPort;

    private Handler mHandler;
    //    private boolean mHasScsIpLast = false;//是否上次有成功连接的ip
    //用来发送数据
    public static Channel sChannel = null;

    /**
     * doConnect：连接到服务端 端口8000
     * <p/>
     * 启动app默认使用局域网ip进行连接。 连接成功后，全局变量ip保留成功连接的ip值，用作下次连接最先尝试连接的ip。
     * 若上一次连接成功的ip此次无法连接成功，则依照序号使用局域网->中继->本地配置ip尝试连接。
     * <p/>
     * 若上一次连接失败，当别处调用连接时，则依然按照序号使用局域网->中继->本地配置ip尝试连接。
     */
    public void doConnect() {
        // 当正在连接时或已经连接服务端则不允许调用
        if (mCurrentState == STATE_CONNECTING) {
            Log.e(TAG, "正处于连接中。取消请求...");
            return;
        }
        if (AcceptorIdleStateTrigger.sConnectedServer) {
            Log.e(TAG, "已连接服务端。取消请求...");
            return;
        }

        if (!NetStatus.isNetworkAvailable(MyApp.getContext())) {
//            PublicHandler.getInstant().getHandler().sendEmptyMessage(PublicHandler.NETWORK_INVALID);
            return;
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                connect();
            }
        }).start();
    }

    /*
     * 在连接成功后，但是不符合需要又需要切换ip进行连接的时候调用
     * */
    public void doConnectAfterChangeIp() {
        //断开旧链接
        disConnected();
        //切换ip
        mCurrentIpIndex++;
        //开始连接
        doConnect();
    }

    /*
     * 需要切换ip的时候调用
     */
    // public void doConnectAfterChangeIp() {
    // mCurrentIpIndex++;
    // doConnect();
    // }

    private void connect() {
        mCurrentState = STATE_CONNECTING;
        // 切换ip
        if (!changeIp()) {
            // 切换IP失败
            connectFailed();
            return;
        }

        if (mHandler == null) {
            new IllegalStateException("handler为空....");
            return;
        }
        if (client == null) {
            client = new Bootstrap();
            client.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.AUTO_READ, true).handler(new MyChannelInitializer(mHandler));

            client.option(ChannelOption.TCP_NODELAY, true);
            client.option(ChannelOption.SO_TIMEOUT, 4000);
            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        }

        mConnectCount++;
        Log.e(TAG, "正在连接服务器...ip=" + mIp + "," + mPort);
        final ChannelFuture future = client.connect(mIp, mPort);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture f) throws Exception {
                // TODO 自动生成的方法存根
                if (f.isSuccess()) {
                    connectSuccess();

                } else {
                    // f.channel().pipeline().fireChannelInactive();

                    // 同一个ip重连2次机制
                    if (mConnectCount < mConnectLimit) {
                        // 同一个ip再尝试一次！！！！！！
                        connect();
                    } else {
                        //上次无成功连接的ip
//                        mHasScsIpLast = false;
                        // 同一个ip连接次数重置
                        mConnectCount = 0;
                        // ip序号增加,表示下次将切换ip
                        mCurrentIpIndex++;
                        // 用新ip重连！！！！！！！
                        connect();
                    }
                }
            }

        });
    }

    private void connectSuccess() {
        Log.i(TAG, "连接成功！");
        //延迟发送通知，知道A类的连接状态改变
        // 发送连接成功消息
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
//                mHandler.sendEmptyMessage(PublicHandler.CONNECT_SERVER_SUCCESS);
            }
        }, 100);
        // 失败计数重置
        mConnectCount = 0;
        // 连接状态置为空闲
        mCurrentState = STATE_FREE;
        // ip序号重置
        mCurrentIpIndex = 1;
        //
//        mHasScsIpLast = true;
    }

    private void connectFailed() {
        Log.i(TAG, "连接失败...");
//        mHandler.sendEmptyMessage(PublicHandler.CONNECT_SERVER_FAILED);// 3个ip连接失败后才通知连接失败。
        // 设置连接为空闲状态
        mCurrentState = STATE_FREE;
        // ip序号置为2
        mCurrentIpIndex = 1;
        // 
//        mHasScsIpLast = false;
    }

    private boolean changeIp() {
        // 改变连接的ip
//        if (mHasScsIpLast) {
//            mCurrentIpIndex = 0;
//            return true;
//        }

        switch (mCurrentIpIndex) {
            case 1:
                mLanIp = AppConstant.IP_LAN;

                if (mLanIp != null) {
                    mIp = mLanIp;
                    mPort = AppConstant.PORT_LAN;
                } else {
                    Log.e(TAG, "切换ip时局域网ip为空...");
                    mCurrentIpIndex++;
                    mIp = AppConstant.IP_CLOUD;
                    mPort = AppConstant.PORT_CLOUD;
                }
                break;
            case 777:
                mIp = AppConstant.IP_CLOUD;
                mPort = AppConstant.PORT_CLOUD;
                break;
            case 888:
//            LocalConfig config = CommonUtils.getLocalConfig(FileUtil.FILE_NAME_SH_CONFIG, null);
//            if (config != null) {
//                mIp = config.getIp();
//                mPort = config.getPort();
//            } else {
//                LogUtil.e(NettyUtil.this.getClass(), "连接失败后读取配置文件失败...");
//                return false;
//            }
                break;
            default:
                return false;
        }
        return true;
    }

    /*
     * 一般在应用退出时调用
     */
    public void disConnected() {
        AcceptorIdleStateTrigger.sReConnect = false;
        if (sChannel != null) {
            sChannel.close();
        }
    }

    /*
     * 广播成功后设置局域网ip
     */
    public void setLanIp(String lanIp) {
        mLanIp = lanIp;
    }

}
