package com.openwatchproject.watchfaceconverter.model;

public class OpenWatchWatchFaceMetadata {
    private String name;
    private String author;
    private int version;

    public OpenWatchWatchFaceMetadata(String name, String author, int version) {
        this.name = name;
        this.author = author;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public int getVersion() {
        return version;
    }
}
