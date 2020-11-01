package org.gallery.demo.mo;

import org.jdbi.v3.core.mapper.reflect.ColumnName;

public class GalleryDetailMO {

    @ColumnName("id")
    private Integer id;
    @ColumnName("name")
    private String galleryName;
    @ColumnName("tags")
    private String galleryTags;

    public GalleryDetailMO(int id, String name, String tags) {
        this.id = id;
        this.galleryName = name;
        this.galleryTags = tags;
    }

    public GalleryDetailMO(String name, String tags) {
        this.galleryName = name;
        this.galleryTags = tags;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGalleryName() {
        return galleryName;
    }

    public void setGalleryName(String galleryName) {
        this.galleryName = galleryName;
    }

    public String getGalleryTags() {
        return galleryTags;
    }

    public void setGalleryTags(String galleryTags) {
        this.galleryTags = galleryTags;
    }
}
