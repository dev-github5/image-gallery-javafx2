package org.gallery.demo.service;

import org.gallery.demo.common.util.CommonUtil;
import org.gallery.demo.db.transactions.ImageGalleryDao;
import org.gallery.demo.disk.util.LinuxDiskUtil;
import org.gallery.demo.mo.DriveDetailMO;
import org.gallery.demo.mo.GalleryDetailMO;

import java.util.ArrayList;
import java.util.List;

public class ImageGalleryService {

    private ImageGalleryDao imageGalleryDao;

    public ImageGalleryService() {
        imageGalleryDao = new ImageGalleryDao();
    }

    public List<DriveDetailMO> getDriveDetails() {
        List<DriveDetailMO> driveDetailMOS = new ArrayList<>();

        if (CommonUtil.isLinuxSystem()) {
            List<String> drives = LinuxDiskUtil.getDrivesRootPath();
            if (drives != null && !drives.isEmpty()) {
                for (String drive : drives) {
                    String[] detail = drive.split(":");
                    DriveDetailMO driveDetailMO = new DriveDetailMO(detail[0],detail[1],1);
                    driveDetailMOS.add(driveDetailMO);
                }
            }
        }

        return driveDetailMOS;
    }

    public void createImageGalleryTable() {
        imageGalleryDao.createImageGalleryTable();
    }

    public void saveGalleryDetail(GalleryDetailMO galleryDetailMO){
        imageGalleryDao.saveGalleryDetail(galleryDetailMO);
    }

    public List<GalleryDetailMO> getGalleryDetails() {
        return imageGalleryDao.getAllGalleryDetails();
    }

    public void removeGalleryDetail(int id) {
        imageGalleryDao.removeGalleryDetail(id);
    }

    public void saveDriveDetail(DriveDetailMO driveDetailMO){
        imageGalleryDao.saveDriveDetail(driveDetailMO);
    }

    public List<DriveDetailMO> getAllMountedDriveDetails(){
        return imageGalleryDao.getAllMountedDriveDetails();
    }

    public boolean removeDriveDetails(){
        return imageGalleryDao.removeDriveDetails();
    }

    public void createDriveDetailTable() {
        imageGalleryDao.createDriveDetailTable();
    }
}
