package com.cherry.dataobject;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 用户信息表
 * Created by Administrator on 2017/11/07.
 */
@Entity
public class UserInfo {

    /**  用户名 */
    @Id
    private String userName;
    /**  用户密码 */
    private String userPassword;
    /**  用户类型 */
    private Integer userClass;
    /**  用户职务 */
    private String userPost;
    /**  用户邮箱 */
    private String userMail;
    /**  用户公司 */
    private String userCompany;
    /**  用户手机号 */
    private String userTelephone;
    /**  用户真实姓名 */
    private String realName;
    /**  用户地址 */
    private String userAddress;
    /**  现场类型 */
    private String industryType;
    /**  用户启用标志 */
    private Integer isUsed;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserClass() {
        return userClass;
    }

    public void setUserClass(Integer userClass) {
        this.userClass = userClass;
    }

    public String getUserPost() {
        return userPost;
    }

    public void setUserPost(String userPost) {
        this.userPost = userPost;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserTelephone() {
        return userTelephone;
    }

    public void setUserTelephone(String userTelephone) {
        this.userTelephone = userTelephone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public Integer getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userClass=" + userClass +
                ", userPost='" + userPost + '\'' +
                ", userMail='" + userMail + '\'' +
                ", userCompany='" + userCompany + '\'' +
                ", userTelephone='" + userTelephone + '\'' +
                ", realName='" + realName + '\'' +
                ", userAddress='" + userAddress + '\'' +
                ", industryType='" + industryType + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
