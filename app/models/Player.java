/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Juan Manuel
 */
@Entity
@Table(name = "player")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Player.findAll", query = "SELECT p FROM Player p"),
    @NamedQuery(name = "Player.findBySummonerId", query = "SELECT p FROM Player p WHERE p.summonerId = :summonerId")})
public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "summonerId")
    private Long summonerId;
    @JoinColumn(name = "platformId", referencedColumnName = "platformId")
    @ManyToOne(optional = false)
    private Region platformId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player")
    private List<Topchampions> topchampionsList;

    public Player() {
        topchampionsList = new ArrayList<>();
    }

    public Long getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(Long summonerId) {
        this.summonerId = summonerId;
    }

    public Region getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Region platformId) {
        this.platformId = platformId;
    }

    @XmlTransient
    public List<Topchampions> getTopchampionsList() {
        return topchampionsList;
    }

    public void setTopchampionsList(List<Topchampions> topchampionsList) {
        this.topchampionsList = topchampionsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (summonerId != null ? summonerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Player)) {
            return false;
        }
        Player other = (Player) object;
        if ((this.summonerId == null && other.summonerId != null) || (this.summonerId != null && !this.summonerId.equals(other.summonerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "riot.Player[ summonerId=" + summonerId + " ]";
    }
    
}
