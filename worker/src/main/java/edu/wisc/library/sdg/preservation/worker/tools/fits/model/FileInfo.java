package edu.wisc.library.sdg.preservation.worker.tools.fits.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class FileInfo {

    @JacksonXmlProperty(isAttribute = false, localName = "filepath")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> filePaths;

    @JacksonXmlProperty(isAttribute = false, localName = "filename")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> fileNames;

    @JacksonXmlProperty(isAttribute = false, localName = "size")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> size;

    @JacksonXmlProperty(isAttribute = false, localName = "md5checksum")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> md5Checksum;

    @JacksonXmlProperty(isAttribute = false, localName = "lastmodified")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> lastModified;

    @JacksonXmlProperty(isAttribute = false, localName = "fslastmodified")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> fsLastModified;

    @JacksonXmlProperty(isAttribute = false, localName = "created")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> created;

    @JacksonXmlProperty(isAttribute = false, localName = "creatingApplicationName")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> creatingApplicationName;

    @JacksonXmlProperty(isAttribute = false, localName = "creatingApplicationVersion")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> creatingApplicationVersion;

    @JacksonXmlProperty(isAttribute = false, localName = "inhibitorType")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> inhibitorType;

    @JacksonXmlProperty(isAttribute = false, localName = "inhibitorTarget")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> inhibitorTarget;

    @JacksonXmlProperty(isAttribute = false, localName = "rightsBasis")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> rightsBasis;

    @JacksonXmlProperty(isAttribute = false, localName = "copyrightBasis")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> copyrightBasis;

    @JacksonXmlProperty(isAttribute = false, localName = "copyrightNote")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> copyrightNote;

    @JacksonXmlProperty(isAttribute = false, localName = "creatingos")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<FitsValue> creatingOS;

    public List<FitsValue> getFilePaths() {
        return filePaths;
    }

    public void setFilePaths(List<FitsValue> filePaths) {
        this.filePaths = filePaths;
    }

    public List<FitsValue> getFileNames() {
        return fileNames;
    }

    public void setFileNames(List<FitsValue> fileNames) {
        this.fileNames = fileNames;
    }

    public List<FitsValue> getSize() {
        return size;
    }

    public void setSize(List<FitsValue> size) {
        this.size = size;
    }

    public List<FitsValue> getMd5Checksum() {
        return md5Checksum;
    }

    public void setMd5Checksum(List<FitsValue> md5Checksum) {
        this.md5Checksum = md5Checksum;
    }

    public List<FitsValue> getLastModified() {
        return lastModified;
    }

    public void setLastModified(List<FitsValue> lastModified) {
        this.lastModified = lastModified;
    }

    public List<FitsValue> getFsLastModified() {
        return fsLastModified;
    }

    public void setFsLastModified(List<FitsValue> fsLastModified) {
        this.fsLastModified = fsLastModified;
    }

    public List<FitsValue> getCreated() {
        return created;
    }

    public void setCreated(List<FitsValue> created) {
        this.created = created;
    }

    public List<FitsValue> getCreatingApplicationName() {
        return creatingApplicationName;
    }

    public void setCreatingApplicationName(List<FitsValue> creatingApplicationName) {
        this.creatingApplicationName = creatingApplicationName;
    }

    public List<FitsValue> getCreatingApplicationVersion() {
        return creatingApplicationVersion;
    }

    public void setCreatingApplicationVersion(List<FitsValue> creatingApplicationVersion) {
        this.creatingApplicationVersion = creatingApplicationVersion;
    }

    public List<FitsValue> getInhibitorType() {
        return inhibitorType;
    }

    public void setInhibitorType(List<FitsValue> inhibitorType) {
        this.inhibitorType = inhibitorType;
    }

    public List<FitsValue> getInhibitorTarget() {
        return inhibitorTarget;
    }

    public void setInhibitorTarget(List<FitsValue> inhibitorTarget) {
        this.inhibitorTarget = inhibitorTarget;
    }

    public List<FitsValue> getRightsBasis() {
        return rightsBasis;
    }

    public void setRightsBasis(List<FitsValue> rightsBasis) {
        this.rightsBasis = rightsBasis;
    }

    public List<FitsValue> getCopyrightBasis() {
        return copyrightBasis;
    }

    public void setCopyrightBasis(List<FitsValue> copyrightBasis) {
        this.copyrightBasis = copyrightBasis;
    }

    public List<FitsValue> getCopyrightNote() {
        return copyrightNote;
    }

    public void setCopyrightNote(List<FitsValue> copyrightNote) {
        this.copyrightNote = copyrightNote;
    }

    public List<FitsValue> getCreatingOS() {
        return creatingOS;
    }

    public void setCreatingOS(List<FitsValue> creatingOS) {
        this.creatingOS = creatingOS;
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "filePaths=" + filePaths +
                ", fileNames=" + fileNames +
                ", size=" + size +
                ", md5Checksum=" + md5Checksum +
                ", lastModified=" + lastModified +
                ", fsLastModified=" + fsLastModified +
                ", created=" + created +
                ", creatingApplicationName=" + creatingApplicationName +
                ", creatingApplicationVersion=" + creatingApplicationVersion +
                ", inhibitorType=" + inhibitorType +
                ", inhibitorTarget=" + inhibitorTarget +
                ", rightsBasis=" + rightsBasis +
                ", copyrightBasis=" + copyrightBasis +
                ", copyrightNote=" + copyrightNote +
                ", creatingOS=" + creatingOS +
                '}';
    }

}
