package com.blueboxmicrosystems.abaco.model.entity;

import java.io.Serializable;
import java.util.Objects;

/**
 * Created by MARAUJO on 1/4/2017.
 */

public class Tag implements Serializable {
    Integer id;
    Integer sync;
    String name;
    String description;
    String icon;
    Integer color;

    public Tag() {
        this.id = 0;
        this.sync = 0;
        this.name = "";
        this.description = "";
        this.icon = "";
        this.color = 0;
    }

    public Tag(Integer id, Integer sync, String name, String description, String icon, Integer color) {
        this.id = id;
        this.sync = sync;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.color = color;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSync() {
        return sync;
    }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(id, tag.id) &&
                Objects.equals(sync, tag.sync) &&
                Objects.equals(name, tag.name) &&
                Objects.equals(description, tag.description) &&
                Objects.equals(icon, tag.icon) &&
                Objects.equals(color, tag.color);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sync, name, description, icon, color);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", sync=" + sync +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", icon='" + icon + '\'' +
                ", color=" + color +
                '}';
    }
}
