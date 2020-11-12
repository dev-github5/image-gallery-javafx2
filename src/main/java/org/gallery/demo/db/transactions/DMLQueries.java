package org.gallery.demo.db.transactions;

public class DMLQueries {
    public static final String GALLERY_INSERT_DDL = "insert into gallery_detail (name,tags) values(:name,:tags)";
    public static final String GALLERY_SELECT_DDL = "select * from gallery_detail";
    public static final String GALLERY_DELETE_DDL = "delete from gallery_detail where id=:id";;

    public static final String MEDIA_DEVICE_INFO_INSERT_DML = "insert into media_device_info (mountName,mountPath,isMounted) values(:mountName,:mountPath,:isMounted)";
    public static final String MEDIA_DEVICE_INFO_SELECT_DML = "select * from media_device_info";
    public static final String MEDIA_DEVICE_INFO_DELETE_DML = "delete from media_device_info where id=:id";;
    public static final String MEDIA_DEVICE_INFO_MOUNTED_DML = "select * from media_device_info where isMounted=1";;
    public static final String MEDIA_DEVICE_INFO_DELETE_ALL_DML = "delete from media_device_info";;


}
