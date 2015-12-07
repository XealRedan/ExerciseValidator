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

    private static final String ACCEPTED = "accepted";
    private static final String PENDING = "pending";
    private static final String REJECTED = "rejected";

    // File meta data
    private String name;
    private String size;
    private String type;
    private String url;
    private String thumbnailUrl;

    // File control data
    private String deleteUrl;
    private String deleteType = "DELETE";

    // File content
    private byte[] bytes;

    /**
     * Gets the file name
     * @return the file name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the file name
     * @param name the file name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the file size
     * @return the file size
     */
    public String getSize() {
        return size;
    }

    /**
     * Sets the file size
     * @param size the file size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * Gets the file type
     * @return the file type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the file type
     * @param type the file type
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDeleteUrl() {
        return this.deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
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