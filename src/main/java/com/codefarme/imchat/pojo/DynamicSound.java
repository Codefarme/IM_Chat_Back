package com.codefarme.imchat.pojo;

import java.io.Serializable;

public class DynamicSound implements Serializable {
    private Integer id;

    private Integer cid;

    private Integer time;

    private String voicepath;

    private Integer status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getVoicepath() {
        return voicepath;
    }

    public void setVoicepath(String voicepath) {
        this.voicepath = voicepath == null ? null : voicepath.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}