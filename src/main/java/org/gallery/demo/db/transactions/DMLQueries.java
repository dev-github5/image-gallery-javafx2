package org.gallery.demo.db.transactions;

public class DMLQueries {
    public static final String GALLERY_INSERT_DDL = "insert into gallery_detail (name,tags) values(:name,:tags)";
    public static final String GALLERY_SELECT_DDL = "select * from gallery_detail";

    public static final String GALLERY_DELETE_DDL = "delete from gallery_detail where id=:id";;
}
