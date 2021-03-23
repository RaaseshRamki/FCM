package com.pushengage.fcmlibrary.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddProfileIdRequest {

    @SerializedName("device_token_hash")
    @Expose
    private String deviceTokenHash;
    @SerializedName("profile_id")
    @Expose
    private String profileId;
    @SerializedName("site_id")
    @Expose
    private String siteId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;

    /**
     * No args constructor for use in serialization
     */
    public AddProfileIdRequest() {
    }

    /**
     * @param deviceType
     * @param profileId
     * @param siteId
     * @param deviceTokenHash
     */
    public AddProfileIdRequest(String deviceTokenHash, String profileId, String siteId, String deviceType) {
        super();
        this.deviceTokenHash = deviceTokenHash;
        this.profileId = profileId;
        this.siteId = siteId;
        this.deviceType = deviceType;
    }

    public String getDeviceTokenHash() {
        return deviceTokenHash;
    }

    public void setDeviceTokenHash(String deviceTokenHash) {
        this.deviceTokenHash = deviceTokenHash;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

}