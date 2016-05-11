package models;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import play.db.jpa.GenericModel;

@Entity
@Table(name = "championstatics")
public class ChampionStatics extends GenericModel {
	@Id
	@Column(name = "champion_id")
	private Integer championId;
	@Basic(optional = false)
	@Column(name = "squareUrl")
	private String squareUrl;
	@Basic(optional = false)
	@Column(name = "splashArtUrl")
	private String splashArtUrl;
	@Basic(optional = false)
	@Column(name = "championName")
	private String championName;
	@Basic(optional = false)
	@Column(name = "rol")
	private String rol;
	
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public ChampionStatics() {
	}

	public Integer getChampionId() {
		return championId;
	}

	public void setChampionId(Integer championId) {
		this.championId = championId;
	}

	public String getSquareUrl() {
		return squareUrl;
	}

	public void setSquareUrl(String squareUrl) {
		this.squareUrl = squareUrl;
	}

	public String getSplashArtUrl() {
		return splashArtUrl;
	}

	public void setSplashArtUrl(String splashArtUrl) {
		this.splashArtUrl = splashArtUrl;
	}

	public String getChampionName() {
		return championName;
	}

	public void setChampionName(String championName) {
		this.championName = championName;
	}
}
