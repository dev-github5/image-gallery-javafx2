package org.gallery.demo.task.services;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.gallery.demo.mo.DriveDetailMO;
import org.gallery.demo.service.ImageGalleryService;

import java.io.File;
import java.util.*;

public class LoadImagesTaskService extends ScheduledService {
    private ImageGalleryService imageGalleryService;
    private List<File> fileList = new LinkedList<>();

    public LoadImagesTaskService(ImageGalleryService imageGalleryService) {
        this.imageGalleryService = imageGalleryService;
    }

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                setPeriod(Duration.minutes(2));
                System.err.println("Loading Images from drives..." + new Date());
                List<DriveDetailMO> mountedDrives = imageGalleryService.getAllMountedDriveDetails();
                if (mountedDrives != null && !mountedDrives.isEmpty()) {
                    fileList.clear();
                    for (DriveDetailMO driveDetailMO : mountedDrives) {
                        String path = driveDetailMO.getMountPath();
                        System.err.println("Drive : " + path);
                        fileList.addAll(readFiles(path));
                    }
                }

                return null;
            }
        };
    }


    private List<File> readFiles(String name) {
        Collection<File> files = FileUtils.listFiles(
                new File(name),
                new RegexFileFilter(".*\\.jpg"),
                DirectoryFileFilter.DIRECTORY
        );
        return new ArrayList<>(files);
    }

    public List<File> getImageFileList() {
        return fileList;
    }
}
