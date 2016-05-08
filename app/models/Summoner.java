/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Juan Manuel
 */
@Entity
@Table(name = "summoner")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Summoner.findAll", query = "SELECT s FROM Summoner s"),
    @NamedQuery(name = "Summoner.findById", query = "SELECT s FROM Summoner s WHERE s.id = :id"),
    @NamedQuery(name = "Summoner.findByChampionId", query = "SELECT s FROM Summoner s WHERE s.championId = :championId"),
    @NamedQuery(name = "Summoner.findByMasteryPoints", query = "SELECT s FROM Summoner s WHERE s.masteryPoints = :masteryPoints"),
    @NamedQuery(name = "Summoner.findByMasteryRank", query = "SELECT s FROM Summoner s WHERE s.masteryRank = :masteryRank"),
    @NamedQuery(name = "Summoner.findByRegion", query = "SELECT s FROM Summoner s WHERE s.region = :region"),
    @NamedQuery(name = "Summoner.findBySummonerId", query = "SELECT s FROM Summoner s WHERE s.summonerId = :summonerId")})
public class Summoner implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "championId")
    private String championId;
    @Column(name = "masteryPoints")
    private Integer masteryPoints;
    @Column(name = "masteryRank")
    private Integer masteryRank;
    @Column(name = "region")
    private String region;
    @Column(name = "summonerId")
    private String summonerId;

    public Summoner() {
    }

    public Summoner(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChampionId() {
        return championId;
    }

    public void setChampionId(String championId) {
        this.championId = championId;
    }

    public Integer getMasteryPoints() {
        return masteryPoints;
    }

    public void setMasteryPoints(Integer masteryPoints) {
        this.masteryPoints = masteryPoints;
    }

    public Integer getMasteryRank() {
        return masteryRank;
    }

    public void setMasteryRank(Integer masteryRank) {
        this.masteryRank = masteryRank;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Summoner)) {
            return false;
        }
        Summoner other = (Summoner) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "riot.Summoner[ id=" + id + " ]";
    }
    
}
