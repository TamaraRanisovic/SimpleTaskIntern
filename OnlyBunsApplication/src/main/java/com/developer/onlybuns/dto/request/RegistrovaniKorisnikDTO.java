package com.developer.onlybuns.dto.request;

public class RegistrovaniKorisnikDTO {
    private String korisnickoIme;

    private Integer num_followers;

    private Integer num_following;

    private  Integer num_posts;

    public RegistrovaniKorisnikDTO() {}

    public RegistrovaniKorisnikDTO(String korisnickoIme, Integer num_followers, Integer num_following, Integer num_posts) {
        this.korisnickoIme = korisnickoIme;
        this.num_followers = num_followers;
        this.num_following = num_following;
        this.num_posts = num_posts;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public Integer getNum_followers() {
        return num_followers;
    }

    public void setNum_followers(Integer num_followers) {
        this.num_followers = num_followers;
    }

    public Integer getNum_following() {
        return num_following;
    }

    public void setNum_following(Integer num_following) {
        this.num_following = num_following;
    }

    public Integer getNum_posts() {
        return num_posts;
    }

    public void setNum_posts(Integer num_posts) {
        this.num_posts = num_posts;
    }
}
