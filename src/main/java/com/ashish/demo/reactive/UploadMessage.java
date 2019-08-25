package com.ashish.demo.reactive;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class UploadMessage implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fileName = null;
    private String assetId = null;
    private String affiliateId = null;
    private String folderId = null;

    public UploadMessage() {
    }

    public UploadMessage(UploadMessage message, String assetId) {
        this.affiliateId = message.affiliateId;
        this.assetId = assetId;
        this.fileName = message.fileName;
        this.folderId = message.folderId;
    }

    public UploadMessage(String affiliateId, String assetId, String folderId, String fileName) {
        this.affiliateId = affiliateId;
        this.assetId = assetId;
        this.fileName = fileName;
        this.folderId = folderId;
    }

    @Override
    public String toString() {
        return "UploadMessage [affiliateId=" + affiliateId + ", assetId=" + assetId + ", fileName=" + fileName
                + ", folderId=" + folderId + "]";
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getAffiliateId() {
        return affiliateId;
    }

    public void setAffiliateId(String affiliateId) {
        this.affiliateId = affiliateId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

}
