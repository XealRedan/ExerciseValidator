package com.exercisevalidator.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * List of file meta
 */
public class FileMetaList implements Serializable {
    private List<FileMeta> files = new LinkedList<>();

    public List<FileMeta> getFiles() {
        return files;
    }

    public void setFiles(List<FileMeta> files) {
        this.files = files;
    }
}
