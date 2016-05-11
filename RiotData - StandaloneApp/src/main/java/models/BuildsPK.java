/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author Juan Manuel
 */
@Embeddable
public class BuildsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "gameId")
    private int gameId;
    @Basic(optional = false)
    @Column(name = "championId")
    private int championId;
    @Basic(optional = false)
    @Column(name = "summonerId")
    private long summonerId;

    public BuildsPK() {
    }

    public BuildsPK(int gameId, int championId, long summonerId) {
        this.gameId = gameId;
        this.championId = championId;
        this.summonerId = summonerId;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) gameId;
        hash += (int) championId;
        hash += (int) summonerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BuildsPK)) {
            return false;
        }
        BuildsPK other = (BuildsPK) object;
        if (this.gameId != other.gameId) {
            return false;
        }
        if (this.championId != other.championId) {
            return false;
        }
        if (this.summonerId != other.summonerId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.BuildsPK[ gameId=" + gameId + ", championId=" + championId + ", summonerId=" + summonerId + " ]";
    }
    
}
