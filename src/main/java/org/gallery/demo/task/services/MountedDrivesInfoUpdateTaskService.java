package org.gallery.demo.task.services;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.gallery.demo.mo.DriveDetailMO;
import org.gallery.demo.service.ImageGalleryService;

import java.util.Date;
import java.util.List;

public class MountedDrivesInfoUpdateTaskService extends ScheduledService {

    private ImageGalleryService imageGalleryService;

    private boolean isMountedDriveFound;

    public MountedDrivesInfoUpdateTaskService(ImageGalleryService imageGalleryService) {
        this.imageGalleryService = imageGalleryService;
    }

    @Override
    protected Task createTask() {

        return new Task() {
            @Override
            protected Object call() throws Exception {
                setPeriod(Duration.minutes(1));
                System.err.println("Reading Mounted Drives Info ... " + new Date());
                List<DriveDetailMO> mountedDrives = imageGalleryService.getDriveDetails();
                flushPreviousMountedDriveDetails();

                if (!mountedDrives.isEmpty()) {
                    for (DriveDetailMO driveDetailMO : mountedDrives) {
                        imageGalleryService.saveDriveDetail(driveDetailMO);
                    }

                    isMountedDriveFound = true;
                } else {
                    isMountedDriveFound = false;
                }

                return null;
            }
        };
    }

    private void flushPreviousMountedDriveDetails() {
        List<DriveDetailMO> mountedDrives = imageGalleryService.getAllMountedDriveDetails();
        if (mountedDrives != null && mountedDrives.size() > 0) {
            imageGalleryService.removeDriveDetails();//Remove previous data.
        }
    }

    public boolean isAnyDriveMounted() {
        return isMountedDriveFound;
    }
}
