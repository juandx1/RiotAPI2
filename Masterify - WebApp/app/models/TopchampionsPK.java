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
public class TopchampionsPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "championId")
    private int championId;
    @Basic(optional = false)
    @Column(name = "summonerId")
    private long summonerId;

    public TopchampionsPK() {
    }

    public TopchampionsPK(int championId, long summonerId) {
        this.championId = championId;
        this.summonerId = summonerId;
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
        hash += (int) championId;
        hash += (int) summonerId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TopchampionsPK)) {
            return false;
        }
        TopchampionsPK other = (TopchampionsPK) object;
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
        return "riot.TopchampionsPK[ championId=" + championId + ", summonerId=" + summonerId + " ]";
    }
    
}
