package com.seekerhut.model.mysqlModel;

import java.io.Serializable;
import java.util.Date;

/**
 * dialog
 * @author 
 */
public class Dialog implements Serializable {
    private Long dialogId;

    private Integer dialogCharacterid;

    private String dialogName;

    private Date dialogCreatetime;

    private Date dialogUpdatetime;

    private String dialogIsDeleted;

    private Date dialogDeletetime;

    private String dialogContent;

    private static final long serialVersionUID = 1L;

    public Long getDialogId() {
        return dialogId;
    }

    public void setDialogId(Long dialogId) {
        this.dialogId = dialogId;
    }

    public Integer getDialogCharacterid() {
        return dialogCharacterid;
    }

    public void setDialogCharacterid(Integer dialogCharacterid) {
        this.dialogCharacterid = dialogCharacterid;
    }

    public String getDialogName() {
        return dialogName;
    }

    public void setDialogName(String dialogName) {
        this.dialogName = dialogName;
    }

    public Date getDialogCreatetime() {
        return dialogCreatetime;
    }

    public void setDialogCreatetime(Date dialogCreatetime) {
        this.dialogCreatetime = dialogCreatetime;
    }

    public Date getDialogUpdatetime() {
        return dialogUpdatetime;
    }

    public void setDialogUpdatetime(Date dialogUpdatetime) {
        this.dialogUpdatetime = dialogUpdatetime;
    }

    public String getDialogIsDeleted() {
        return dialogIsDeleted;
    }

    public void setDialogIsDeleted(String dialogIsDeleted) {
        this.dialogIsDeleted = dialogIsDeleted;
    }

    public Date getDialogDeletetime() {
        return dialogDeletetime;
    }

    public void setDialogDeletetime(Date dialogDeletetime) {
        this.dialogDeletetime = dialogDeletetime;
    }

    public String getDialogContent() {
        return dialogContent;
    }

    public void setDialogContent(String dialogContent) {
        this.dialogContent = dialogContent;
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
        Dialog other = (Dialog) that;
        return (this.getDialogId() == null ? other.getDialogId() == null : this.getDialogId().equals(other.getDialogId()))
            && (this.getDialogCharacterid() == null ? other.getDialogCharacterid() == null : this.getDialogCharacterid().equals(other.getDialogCharacterid()))
            && (this.getDialogName() == null ? other.getDialogName() == null : this.getDialogName().equals(other.getDialogName()))
            && (this.getDialogCreatetime() == null ? other.getDialogCreatetime() == null : this.getDialogCreatetime().equals(other.getDialogCreatetime()))
            && (this.getDialogUpdatetime() == null ? other.getDialogUpdatetime() == null : this.getDialogUpdatetime().equals(other.getDialogUpdatetime()))
            && (this.getDialogIsDeleted() == null ? other.getDialogIsDeleted() == null : this.getDialogIsDeleted().equals(other.getDialogIsDeleted()))
            && (this.getDialogDeletetime() == null ? other.getDialogDeletetime() == null : this.getDialogDeletetime().equals(other.getDialogDeletetime()))
            && (this.getDialogContent() == null ? other.getDialogContent() == null : this.getDialogContent().equals(other.getDialogContent()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getDialogId() == null) ? 0 : getDialogId().hashCode());
        result = prime * result + ((getDialogCharacterid() == null) ? 0 : getDialogCharacterid().hashCode());
        result = prime * result + ((getDialogName() == null) ? 0 : getDialogName().hashCode());
        result = prime * result + ((getDialogCreatetime() == null) ? 0 : getDialogCreatetime().hashCode());
        result = prime * result + ((getDialogUpdatetime() == null) ? 0 : getDialogUpdatetime().hashCode());
        result = prime * result + ((getDialogIsDeleted() == null) ? 0 : getDialogIsDeleted().hashCode());
        result = prime * result + ((getDialogDeletetime() == null) ? 0 : getDialogDeletetime().hashCode());
        result = prime * result + ((getDialogContent() == null) ? 0 : getDialogContent().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", dialogId=").append(dialogId);
        sb.append(", dialogCharacterid=").append(dialogCharacterid);
        sb.append(", dialogName=").append(dialogName);
        sb.append(", dialogCreatetime=").append(dialogCreatetime);
        sb.append(", dialogUpdatetime=").append(dialogUpdatetime);
        sb.append(", dialogIsDeleted=").append(dialogIsDeleted);
        sb.append(", dialogDeletetime=").append(dialogDeletetime);
        sb.append(", dialogContent=").append(dialogContent);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}