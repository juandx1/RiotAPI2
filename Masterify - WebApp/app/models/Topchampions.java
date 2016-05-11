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
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import play.db.jpa.GenericModel;

/**
 *
 * @author Juan Manuel
 */
@Entity
@Table(name = "topchampions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Topchampions.findAll", query = "SELECT t FROM Topchampions t"),
    @NamedQuery(name = "Topchampions.findByChampionId", query = "SELECT t FROM Topchampions t WHERE t.topchampionsPK.championId = :championId"),
    @NamedQuery(name = "Topchampions.findByKills", query = "SELECT t FROM Topchampions t WHERE t.kills = :kills"),
    @NamedQuery(name = "Topchampions.findByDeaths", query = "SELECT t FROM Topchampions t WHERE t.deaths = :deaths"),
    @NamedQuery(name = "Topchampions.findByAssits", query = "SELECT t FROM Topchampions t WHERE t.assits = :assits"),
    @NamedQuery(name = "Topchampions.findByGold", query = "SELECT t FROM Topchampions t WHERE t.gold = :gold"),
    @NamedQuery(name = "Topchampions.findByWin", query = "SELECT t FROM Topchampions t WHERE t.win = :win"),
    @NamedQuery(name = "Topchampions.findByLost", query = "SELECT t FROM Topchampions t WHERE t.lost = :lost"),
    @NamedQuery(name = "Topchampions.findByGames", query = "SELECT t FROM Topchampions t WHERE t.games = :games"),
    @NamedQuery(name = "Topchampions.findBySummonerId", query = "SELECT t FROM Topchampions t WHERE t.topchampionsPK.summonerId = :summonerId")})
public class Topchampions extends GenericModel implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected TopchampionsPK topchampionsPK;
    @Basic(optional = false)
    @Column(name = "kills")
    private int kills;
    @Basic(optional = false)
    @Column(name = "deaths")
    private int deaths;
    @Basic(optional = false)
    @Column(name = "assits")
    private int assits;
    @Basic(optional = false)
    @Column(name = "gold")
    private int gold;
    @Basic(optional = false)
    @Column(name = "win")
    private int win;
    @Basic(optional = false)
    @Column(name = "lost")
    private int lost;
    @Basic(optional = false)
    @Column(name = "games")
    private int games;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "topchampions")
    private List<Builds> builds;
    @JoinColumn(name = "summonerId", referencedColumnName = "summonerId", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Player player;

    public Topchampions() {
        builds = new ArrayList<Builds>();
    }

    public Topchampions(TopchampionsPK topchampionsPK) {
        this.topchampionsPK = topchampionsPK;
    }

    public Topchampions(TopchampionsPK topchampionsPK, int kills, int deaths, int assits, int gold, int win, int lost, int games) {
        this.topchampionsPK = topchampionsPK;
        this.kills = kills;
        this.deaths = deaths;
        this.assits = assits;
        this.gold = gold;
        this.win = win;
        this.lost = lost;
        this.games = games;
    }

    public Topchampions(int championId, int summonerId) {
        this.topchampionsPK = new TopchampionsPK(championId, summonerId);
    }

    public TopchampionsPK getTopchampionsPK() {
        return topchampionsPK;
    }

    public void setTopchampionsPK(TopchampionsPK topchampionsPK) {
        this.topchampionsPK = topchampionsPK;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getAssits() {
        return assits;
    }

    public void setAssits(int assits) {
        this.assits = assits;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public List<Builds> getBuilds() {
        return builds;
    }

    public void setBuilds(List<Builds> builds) {
        this.builds = builds;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (topchampionsPK != null ? topchampionsPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Topchampions)) {
            return false;
        }
        Topchampions other = (Topchampions) object;
        if ((this.topchampionsPK == null && other.topchampionsPK != null) || (this.topchampionsPK != null && !this.topchampionsPK.equals(other.topchampionsPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "riot.Topchampions[ topchampionsPK=" + topchampionsPK + " ]";
    }
    
}
