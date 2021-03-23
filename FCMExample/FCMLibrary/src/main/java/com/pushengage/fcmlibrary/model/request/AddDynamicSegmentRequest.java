package com.pushengage.fcmlibrary.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddDynamicSegmentRequest {

    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("site_id")
    @Expose
    private String siteId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("segments")
    @Expose
    private List<Segment> segments = null;

    /**
     * No args constructor for use in serialization
     */
    public AddDynamicSegmentRequest() {
    }

    /**
     * @param deviceType
     * @param siteId
     * @param deviceToken
     * @param segments
     */
    public AddDynamicSegmentRequest(String deviceToken, String siteId, String deviceType, List<Segment> segments) {
        super();
        this.deviceToken = deviceToken;
        this.siteId = siteId;
        this.deviceType = deviceType;
        this.segments = segments;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public class Segment {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("duration")
        @Expose
        private long duration;

        /**
         * No args constructor for use in serialization
         */
        public Segment() {
        }

        /**
         * @param duration
         * @param name
         */
        public Segment(String name, long duration) {
            super();
            this.name = name;
            this.duration = duration;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }

    }

}