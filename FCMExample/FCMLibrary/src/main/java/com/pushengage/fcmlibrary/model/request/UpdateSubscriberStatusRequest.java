package com.pushengage.fcmlibrary.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateSubscriberStatusRequest {

    @SerializedName("site_id")
    @Expose
    private long siteId;
    @SerializedName("device_token_hash")
    @Expose
    private String deviceTokenHash;
    @SerializedName("IsUnSubscribed")
    @Expose
    private long isUnSubscribed;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateSubscriberStatusRequest() {
    }

    /**
     *
     * @param siteId
     * @param isUnSubscribed
     * @param deviceTokenHash
     */
    public UpdateSubscriberStatusRequest(long siteId, String deviceTokenHash, long isUnSubscribed) {
        super();
        this.siteId = siteId;
        this.deviceTokenHash = deviceTokenHash;
        this.isUnSubscribed = isUnSubscribed;
    }

    public long getSiteId() {
        return siteId;
    }

    public void setSiteId(long siteId) {
        this.siteId = siteId;
    }

    public String getDeviceTokenHash() {
        return deviceTokenHash;
    }

    public void setDeviceTokenHash(String deviceTokenHash) {
        this.deviceTokenHash = deviceTokenHash;
    }

    public long getIsUnSubscribed() {
        return isUnSubscribed;
    }

    public void setIsUnSubscribed(long isUnSubscribed) {
        this.isUnSubscribed = isUnSubscribed;
    }

}