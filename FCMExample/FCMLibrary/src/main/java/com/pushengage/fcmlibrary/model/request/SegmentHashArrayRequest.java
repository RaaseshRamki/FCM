package com.pushengage.fcmlibrary.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SegmentHashArrayRequest {

    @SerializedName("device_token_hash")
    @Expose
    private String deviceTokenHash;
    @SerializedName("site_id")
    @Expose
    private long siteId;
    @SerializedName("segment_id")
    @Expose
    private long segmentId;

    /**
     * No args constructor for use in serialization
     */
    public SegmentHashArrayRequest() {
    }

    /**
     * @param segmentId
     * @param siteId
     * @param deviceTokenHash
     */
    public SegmentHashArrayRequest(String deviceTokenHash, long siteId, long segmentId) {
        super();
        this.deviceTokenHash = deviceTokenHash;
        this.siteId = siteId;
        this.segmentId = segmentId;
    }

    public String getDeviceTokenHash() {
        return deviceTokenHash;
    }

    public void setDeviceTokenHash(String deviceTokenHash) {
        this.deviceTokenHash = deviceTokenHash;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public long getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(long segmentId) {
        this.segmentId = segmentId;
    }

}
