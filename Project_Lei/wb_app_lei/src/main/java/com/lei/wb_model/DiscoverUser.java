package com.lei.wb_model;

public class DiscoverUser {
    public String screen_name;
    public int followers_count;
    public long uid;

    public DiscoverUser(String screen_name, int followers_count, long uid) {
        this.screen_name = screen_name;
        this.followers_count = followers_count;
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "DiscoverUser{" +
                "screen_name='" + screen_name + '\'' +
                ", followers_count=" + followers_count +
                ", uid=" + uid +
                '}';
    }
}
