package com.zqz.dao.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class DbMovies implements Serializable {
    private Long id;

    private String movieId;

    private String directors;

    private String title;

    private String cover;

    private BigDecimal rate;

    private String casts;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date cTime;

    private Date uTime;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors == null ? null : directors.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover == null ? null : cover.trim();
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getCasts() {
        return casts;
    }

    public void setCasts(String casts) {
        this.casts = casts == null ? null : casts.trim();
    }

    public Date getcTime() {
        return cTime;
    }

    public void setcTime(Date cTime) {
        this.cTime = cTime;
    }

    public Date getuTime() {
        return uTime;
    }

    public void setuTime(Date uTime) {
        this.uTime = uTime;
    }

    @Override
    public String toString() {
        return "DbMovies{" +
                "id=" + id +
                ", movieId='" + movieId + '\'' +
                ", directors='" + directors + '\'' +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", rate=" + rate +
                ", casts='" + casts + '\'' +
                ", cTime=" + cTime +
                ", uTime=" + uTime +
                '}';
    }
}