/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Juan Manuel
 */
@Entity
@Table(name = "builds")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Builds.findAll", query = "SELECT b FROM Builds b"),
    @NamedQuery(name = "Builds.findByGameId", query = "SELECT b FROM Builds b WHERE b.buildsPK.gameId = :gameId"),
    @NamedQuery(name = "Builds.findByItem0", query = "SELECT b FROM Builds b WHERE b.item0 = :item0"),
    @NamedQuery(name = "Builds.findByItem1", query = "SELECT b FROM Builds b WHERE b.item1 = :item1"),
    @NamedQuery(name = "Builds.findByItem2", query = "SELECT b FROM Builds b WHERE b.item2 = :item2"),
    @NamedQuery(name = "Builds.findByItem3", query = "SELECT b FROM Builds b WHERE b.item3 = :item3"),
    @NamedQuery(name = "Builds.findByItem4", query = "SELECT b FROM Builds b WHERE b.item4 = :item4"),
    @NamedQuery(name = "Builds.findByItem5", query = "SELECT b FROM Builds b WHERE b.item5 = :item5"),
    @NamedQuery(name = "Builds.findByItem6", query = "SELECT b FROM Builds b WHERE b.item6 = :item6"),
    @NamedQuery(name = "Builds.findByChampionId", query = "SELECT b FROM Builds b WHERE b.buildsPK.championId = :championId"),
    @NamedQuery(name = "Builds.findBySummonerId", query = "SELECT b FROM Builds b WHERE b.buildsPK.summonerId = :summonerId")})
public class Builds implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected BuildsPK buildsPK;
    @Column(name = "item0")
    private Integer item0;
    @Column(name = "item1")
    private Integer item1;
    @Column(name = "item2")
    private Integer item2;
    @Column(name = "item3")
    private Integer item3;
    @Column(name = "item4")
    private Integer item4;
    @Column(name = "item5")
    private Integer item5;
    @Column(name = "item6")
    private Integer item6;
    @JoinColumns({
        @JoinColumn(name = "championId", referencedColumnName = "championId", insertable = false, updatable = false),
        @JoinColumn(name = "summonerId", referencedColumnName = "summonerId", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Topchampions topchampions;

    public Builds() {
    }

    public Builds(BuildsPK buildsPK) {
        this.buildsPK = buildsPK;
    }

    public Builds(int gameId, int championId, long summonerId) {
        this.buildsPK = new BuildsPK(gameId, championId, summonerId);
    }

    public BuildsPK getBuildsPK() {
        return buildsPK;
    }

    public void setBuildsPK(BuildsPK buildsPK) {
        this.buildsPK = buildsPK;
    }

    public Integer getItem0() {
        return item0;
    }

    public void setItem0(Integer item0) {
        this.item0 = item0;
    }

    public Integer getItem1() {
        return item1;
    }

    public void setItem1(Integer item1) {
        this.item1 = item1;
    }

    public Integer getItem2() {
        return item2;
    }

    public void setItem2(Integer item2) {
        this.item2 = item2;
    }

    public Integer getItem3() {
        return item3;
    }

    public void setItem3(Integer item3) {
        this.item3 = item3;
    }

    public Integer getItem4() {
        return item4;
    }

    public void setItem4(Integer item4) {
        this.item4 = item4;
    }

    public Integer getItem5() {
        return item5;
    }

    public void setItem5(Integer item5) {
        this.item5 = item5;
    }

    public Integer getItem6() {
        return item6;
    }

    public void setItem6(Integer item6) {
        this.item6 = item6;
    }

    public Topchampions getTopchampions() {
        return topchampions;
    }

    public void setTopchampions(Topchampions topchampions) {
        this.topchampions = topchampions;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (buildsPK != null ? buildsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Builds)) {
            return false;
        }
        Builds other = (Builds) object;
        if ((this.buildsPK == null && other.buildsPK != null) || (this.buildsPK != null && !this.buildsPK.equals(other.buildsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Builds[ buildsPK=" + buildsPK + " ]";
    }
    
}
