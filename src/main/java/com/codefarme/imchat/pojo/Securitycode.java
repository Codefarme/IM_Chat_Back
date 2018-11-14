package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class Securitycode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3783745686391946132L;
	private Integer id;
	private String account;
	private String securitycode;
	private String time;
	private String timelimit;
	private String todycount;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getSecuritycode() {
		return securitycode;
	}
	public void setSecuritycode(String securitycode) {
		this.securitycode = securitycode;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTimelimit() {
		return timelimit;
	}
	public void setTimelimit(String timelimit) {
		this.timelimit = timelimit;
	}
	public String getTodycount() {
		return todycount;
	}
	public void setTodycount(String todycount) {
		this.todycount = todycount;
	}
	@Override
	public String toString() {
		return "Securitycode [id=" + id + ", account=" + account + ", securityCode=" + securitycode + ", time=" + time
				+ ", timelimit=" + timelimit + ", todycount=" + todycount + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((securitycode == null) ? 0 : securitycode.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((timelimit == null) ? 0 : timelimit.hashCode());
		result = prime * result + ((todycount == null) ? 0 : todycount.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Securitycode other = (Securitycode) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (securitycode == null) {
			if (other.securitycode != null)
				return false;
		} else if (!securitycode.equals(other.securitycode))
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (timelimit == null) {
			if (other.timelimit != null)
				return false;
		} else if (!timelimit.equals(other.timelimit))
			return false;
		if (todycount == null) {
			if (other.todycount != null)
				return false;
		} else if (!todycount.equals(other.todycount))
			return false;
		return true;
	}
	
	
}
