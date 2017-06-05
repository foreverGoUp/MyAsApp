/**
 *
 */
package com.kc.interf;

/**
 * @author ckc
 *         <p/>
 *         usage:用于全局消息的通知，在任何界面都有可能弹出
 */
public interface INotificationListener {
    void notice(int code);
}
