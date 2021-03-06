package com.cherry.form;

/**
 * 协议适配表单验证
 * Created by Administrator on 2017/11/24.
 */
public class ProtocolAdaptForm {

    /**  用户名 */
    private String userName;
    /**  设备SN码 */
    private String snCode;
    /**  协议版本号 */
    private String protocolVersion;
    /**  协议具体内容 */
    private String protocolContent;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSnCode() {
        return snCode;
    }

    public void setSnCode(String snCode) {
        this.snCode = snCode;
    }

    public String getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(String protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getProtocolContent() {
        return protocolContent;
    }

    public void setProtocolContent(String protocolContent) {
        this.protocolContent = protocolContent;
    }

    @Override
    public String toString() {
        return "ProtocolAdaptForm{" +
                "userName='" + userName + '\'' +
                ", snCode='" + snCode + '\'' +
                ", protocolVersion='" + protocolVersion + '\'' +
                ", protocolContent='" + protocolContent + '\'' +
                '}';
    }
}
