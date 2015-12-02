package com.exercisevalidator.model;

/*
 * #%L
 * Exercise validator
 * %%
 * Copyright (C) 2015 Alexandre Lombard
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Store file meta info and data
 */
@JsonIgnoreProperties({"bytes"})
public class FileMeta implements Serializable {

    private String fileName;
    private String fileSize;
    private String fileType;

    private byte[] bytes;

    /**
     * Gets the file name
     * @return the file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the file size
     * @return the file size
     */
    public String getFileSize() {
        return fileSize;
    }

    /**
     * Sets the file size
     * @param fileSize the file size
     */
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * Gets the file type
     * @return the file type
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * Sets the file type
     * @param fileType the file type
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * Gets the file bytes
     * @return the file bytes
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Sets the file bytes
     * @param bytes the file bytes
     */
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}