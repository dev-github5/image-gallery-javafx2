package org.gallery.demo.db.transactions;

public class DDLQueries {

    public static final String GALLERY_CREATE_DDL = "create table IF NOT EXISTS gallery_detail(id integer primary key  autoincrement,name text not null,tags text not null)";
}
