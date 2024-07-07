package com.seekerhut.model.mysqlModel;

import java.io.Serializable;
import java.util.Date;

/**
 * character
 * @author 
 */
public class Character implements Serializable {
    private Integer characterId;

    private Integer characterUserid;

    private String characterName;

    private Date characterCreatetime;

    private Date characterUpdatetime;

    private String characterIsDeleted;

    private Date characterDeletetime;

    private String characterPrompt;

    private static final long serialVersionUID = 1L;

    public Integer getCharacterId() {
        return characterId;
    }

    public void setCharacterId(Integer characterId) {
        this.characterId = characterId;
    }

    public Integer getCharacterUserid() {
        return characterUserid;
    }

    public void setCharacterUserid(Integer characterUserid) {
        this.characterUserid = characterUserid;
    }

    public String getCharacterName() {
        return characterName;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public Date getCharacterCreatetime() {
        return characterCreatetime;
    }

    public void setCharacterCreatetime(Date characterCreatetime) {
        this.characterCreatetime = characterCreatetime;
    }

    public Date getCharacterUpdatetime() {
        return characterUpdatetime;
    }

    public void setCharacterUpdatetime(Date characterUpdatetime) {
        this.characterUpdatetime = characterUpdatetime;
    }

    public String getCharacterIsDeleted() {
        return characterIsDeleted;
    }

    public void setCharacterIsDeleted(String characterIsDeleted) {
        this.characterIsDeleted = characterIsDeleted;
    }

    public Date getCharacterDeletetime() {
        return characterDeletetime;
    }

    public void setCharacterDeletetime(Date characterDeletetime) {
        this.characterDeletetime = characterDeletetime;
    }

    public String getCharacterPrompt() {
        return characterPrompt;
    }

    public void setCharacterPrompt(String characterPrompt) {
        this.characterPrompt = characterPrompt;
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
        Character other = (Character) that;
        return (this.getCharacterId() == null ? other.getCharacterId() == null : this.getCharacterId().equals(other.getCharacterId()))
            && (this.getCharacterUserid() == null ? other.getCharacterUserid() == null : this.getCharacterUserid().equals(other.getCharacterUserid()))
            && (this.getCharacterName() == null ? other.getCharacterName() == null : this.getCharacterName().equals(other.getCharacterName()))
            && (this.getCharacterCreatetime() == null ? other.getCharacterCreatetime() == null : this.getCharacterCreatetime().equals(other.getCharacterCreatetime()))
            && (this.getCharacterUpdatetime() == null ? other.getCharacterUpdatetime() == null : this.getCharacterUpdatetime().equals(other.getCharacterUpdatetime()))
            && (this.getCharacterIsDeleted() == null ? other.getCharacterIsDeleted() == null : this.getCharacterIsDeleted().equals(other.getCharacterIsDeleted()))
            && (this.getCharacterDeletetime() == null ? other.getCharacterDeletetime() == null : this.getCharacterDeletetime().equals(other.getCharacterDeletetime()))
            && (this.getCharacterPrompt() == null ? other.getCharacterPrompt() == null : this.getCharacterPrompt().equals(other.getCharacterPrompt()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getCharacterId() == null) ? 0 : getCharacterId().hashCode());
        result = prime * result + ((getCharacterUserid() == null) ? 0 : getCharacterUserid().hashCode());
        result = prime * result + ((getCharacterName() == null) ? 0 : getCharacterName().hashCode());
        result = prime * result + ((getCharacterCreatetime() == null) ? 0 : getCharacterCreatetime().hashCode());
        result = prime * result + ((getCharacterUpdatetime() == null) ? 0 : getCharacterUpdatetime().hashCode());
        result = prime * result + ((getCharacterIsDeleted() == null) ? 0 : getCharacterIsDeleted().hashCode());
        result = prime * result + ((getCharacterDeletetime() == null) ? 0 : getCharacterDeletetime().hashCode());
        result = prime * result + ((getCharacterPrompt() == null) ? 0 : getCharacterPrompt().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", characterId=").append(characterId);
        sb.append(", characterUserid=").append(characterUserid);
        sb.append(", characterName=").append(characterName);
        sb.append(", characterCreatetime=").append(characterCreatetime);
        sb.append(", characterUpdatetime=").append(characterUpdatetime);
        sb.append(", characterIsDeleted=").append(characterIsDeleted);
        sb.append(", characterDeletetime=").append(characterDeletetime);
        sb.append(", characterPrompt=").append(characterPrompt);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}