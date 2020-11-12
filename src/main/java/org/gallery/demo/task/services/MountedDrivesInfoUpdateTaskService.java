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

                if (!mountedDrives.isEmpty()) {
                    imageGalleryService.removeDriveDetails();//Remove previous data.
                    for (DriveDetailMO driveDetailMO : mountedDrives) {
                        imageGalleryService.saveDriveDetail(driveDetailMO);
                    }
                }

                return null;
            }
        };
    }
}
