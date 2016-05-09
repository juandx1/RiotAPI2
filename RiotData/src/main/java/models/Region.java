/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "region")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Region.findAll", query = "SELECT r FROM Region r"),
    @NamedQuery(name = "Region.findByPlatformId", query = "SELECT r FROM Region r WHERE r.platformId = :platformId"),
    @NamedQuery(name = "Region.findByPlatform", query = "SELECT r FROM Region r WHERE r.platform = :platform"),
    @NamedQuery(name = "Region.findByHost", query = "SELECT r FROM Region r WHERE r.host = :host")})
public class Region implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "platformId")
    private String platformId;
    @Basic(optional = false)
    @Column(name = "platform")
    private String platform;
    @Basic(optional = false)
    @Column(name = "host")
    private String host;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "platformId")
    private List<Player> playerList;

    public Region() {
    }

    public Region(String platformId) {
        this.platformId = platformId;
    }

    public Region(String platformId, String platform, String host) {
        this.platformId = platformId;
        this.platform = platform;
        this.host = host;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @XmlTransient
    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (platformId != null ? platformId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Region)) {
            return false;
        }
        Region other = (Region) object;
        if ((this.platformId == null && other.platformId != null) || (this.platformId != null && !this.platformId.equals(other.platformId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "models.Region[ platformId=" + platformId + " ]";
    }
    
}
