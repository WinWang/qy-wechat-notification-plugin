package org.jenkinsci.plugins.qywechat.model;

import hudson.util.Secret;

import java.util.HashMap;

/**
 * 配置项
 *
 * @author jiaju
 */
public class NotificationConfig {

    /**
     * 企业微信WebHook地址
     */
    public String webhookUrl = "";
    /**
     * 通知用户ID
     */
    public String mentionedId = "";
    /**
     * 通知用户手机
     */
    public String mentionedMobile = "";
    /**
     * 主题名称
     */
    public String topicName = "";

    /**
     * 使用代理
     */
    public boolean useProxy = false;
    /**
     * 代理主机
     */
    public String proxyHost = "";
    /**
     * 代理端口
     */
    public int proxyPort = 8080;
    /**
     * 代理用户名
     */
    public String proxyUsername = "";
    /**
     * 代理密码
     */
    public Secret proxyPassword = null;

    /**
     * 仅在失败通知
     */
    public boolean failNotify = false;

    /**
     * 是否构架开始发送
     */
    public boolean isSendBuildBefore = false;

    /**
     * git提交记录
     */
    public String commitMessage = "";

    /**
     * 动态参数化构建参数
     */
    public String buildParam = "";


    /**
     * 构建参数对象
     */
    public HashMap<String, Object> buildHashMap = new HashMap();


    /**
     * 编译信息
     */
    public String buildMsg = "";


}
