package org.gallery.demo.db.transactions;

public class DDLQueries {

    public static final String GALLERY_CREATE_DDL = "create table IF NOT EXISTS gallery_detail(id integer primary key  autoincrement,name text not null,tags text not null)";
    public static final String MEDIA_DEVICE_INFO_CREATE_DDL = "create table IF NOT EXISTS media_device_info(id integer primary key  autoincrement,mountName text not null,mountPath text not null unique,isMounted integer not null)";

}
