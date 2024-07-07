package com.seekerhut.model.mysqlModel;

import java.io.Serializable;
import java.util.Date;

/**
 * user
 * @author 
 */
public class User implements Serializable {
    private Integer userId;

    private String userName;

    private String userQq;

    private String userPass;

    private String userActivateToken;

    private Byte userIsActivate;

    private Date userCreatetime;

    private Date userUpdatetime;

    private Byte userIsDeleted;

    private Date userDeletetime;

    private static final long serialVersionUID = 1L;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserQq() {
        return userQq;
    }

    public void setUserQq(String userQq) {
        this.userQq = userQq;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserActivateToken() {
        return userActivateToken;
    }

    public void setUserActivateToken(String userActivateToken) {
        this.userActivateToken = userActivateToken;
    }

    public Byte getUserIsActivate() {
        return userIsActivate;
    }

    public void setUserIsActivate(Byte userIsActivate) {
        this.userIsActivate = userIsActivate;
    }

    public Date getUserCreatetime() {
        return userCreatetime;
    }

    public void setUserCreatetime(Date userCreatetime) {
        this.userCreatetime = userCreatetime;
    }

    public Date getUserUpdatetime() {
        return userUpdatetime;
    }

    public void setUserUpdatetime(Date userUpdatetime) {
        this.userUpdatetime = userUpdatetime;
    }

    public Byte getUserIsDeleted() {
        return userIsDeleted;
    }

    public void setUserIsDeleted(Byte userIsDeleted) {
        this.userIsDeleted = userIsDeleted;
    }

    public Date getUserDeletetime() {
        return userDeletetime;
    }

    public void setUserDeletetime(Date userDeletetime) {
        this.userDeletetime = userDeletetime;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        User other = (User) that;
        return (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getUserName() == null ? other.getUserName() == null : this.getUserName().equals(other.getUserName()))
            && (this.getUserQq() == null ? other.getUserQq() == null : this.getUserQq().equals(other.getUserQq()))
            && (this.getUserPass() == null ? other.getUserPass() == null : this.getUserPass().equals(other.getUserPass()))
            && (this.getUserActivateToken() == null ? other.getUserActivateToken() == null : this.getUserActivateToken().equals(other.getUserActivateToken()))
            && (this.getUserIsActivate() == null ? other.getUserIsActivate() == null : this.getUserIsActivate().equals(other.getUserIsActivate()))
            && (this.getUserCreatetime() == null ? other.getUserCreatetime() == null : this.getUserCreatetime().equals(other.getUserCreatetime()))
            && (this.getUserUpdatetime() == null ? other.getUserUpdatetime() == null : this.getUserUpdatetime().equals(other.getUserUpdatetime()))
            && (this.getUserIsDeleted() == null ? other.getUserIsDeleted() == null : this.getUserIsDeleted().equals(other.getUserIsDeleted()))
            && (this.getUserDeletetime() == null ? other.getUserDeletetime() == null : this.getUserDeletetime().equals(other.getUserDeletetime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getUserName() == null) ? 0 : getUserName().hashCode());
        result = prime * result + ((getUserQq() == null) ? 0 : getUserQq().hashCode());
        result = prime * result + ((getUserPass() == null) ? 0 : getUserPass().hashCode());
        result = prime * result + ((getUserActivateToken() == null) ? 0 : getUserActivateToken().hashCode());
        result = prime * result + ((getUserIsActivate() == null) ? 0 : getUserIsActivate().hashCode());
        result = prime * result + ((getUserCreatetime() == null) ? 0 : getUserCreatetime().hashCode());
        result = prime * result + ((getUserUpdatetime() == null) ? 0 : getUserUpdatetime().hashCode());
        result = prime * result + ((getUserIsDeleted() == null) ? 0 : getUserIsDeleted().hashCode());
        result = prime * result + ((getUserDeletetime() == null) ? 0 : getUserDeletetime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", userId=").append(userId);
        sb.append(", userName=").append(userName);
        sb.append(", userQq=").append(userQq);
        sb.append(", userPass=").append(userPass);
        sb.append(", userActivateToken=").append(userActivateToken);
        sb.append(", userIsActivate=").append(userIsActivate);
        sb.append(", userCreatetime=").append(userCreatetime);
        sb.append(", userUpdatetime=").append(userUpdatetime);
        sb.append(", userIsDeleted=").append(userIsDeleted);
        sb.append(", userDeletetime=").append(userDeletetime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}