package com.samuigroup.originalanimewallpaper.models.entities;

public class Category {
    private String iconUrl;
    private String name;
    private String tag;
    private String description;

    public Category(String name, String tag, String iconUrl, String description) {
        this.name = name;
        this.tag = tag;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
