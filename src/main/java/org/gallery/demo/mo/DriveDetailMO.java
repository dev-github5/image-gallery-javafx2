package org.gallery.demo.mo;

public class DriveDetailMO {

    private int id;
    private String mountPath;
    private String mountName;
    private int isMounted;

    public DriveDetailMO(int id, String mountName, String mountPath, int isMounted) {
        this.id = id;
        this.mountPath = mountPath;
        this.mountName = mountName;
        this.isMounted = isMounted;
    }

    public DriveDetailMO(String mountName, String mountPath, int isMounted) {
        this.mountPath = mountPath;
        this.mountName = mountName;
        this.isMounted = isMounted;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMountPath() {
        return mountPath;
    }

    public void setMountPath(String mountPath) {
        this.mountPath = mountPath;
    }

    public String getMountName() {
        return mountName;
    }

    public void setMountName(String mountName) {
        this.mountName = mountName;
    }

    public int getIsMounted() {
        return isMounted;
    }

    public void setIsMounted(int isMounted) {
        this.isMounted = isMounted;
    }
}
