package com.pushengage.fcmlibrary.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSubscriberRequest {


    @SerializedName("site_id")
    @Expose
    private String siteId;
    @SerializedName("browser_info")
    @Expose
    private BrowserInfo browserInfo;
    @SerializedName("subscription")
    @Expose
    private Subscription subscription;
    @SerializedName("subscription_url")
    @Expose
    private String subscriptionUrl;
    @SerializedName("geo_info")
    @Expose
    private GeoInfo geoInfo;
    @SerializedName("token_refresh")
    @Expose
    private boolean tokenRefresh;

    /**
     * No args constructor for use in serialization
     */
    public AddSubscriberRequest() {
    }

    /**
     * @param tokenRefresh
     * @param geoInfo
     * @param siteId
     * @param subscription
     * @param browserInfo
     * @param subscriptionUrl
     */
    public AddSubscriberRequest(String siteId, BrowserInfo browserInfo, Subscription subscription, String subscriptionUrl, GeoInfo geoInfo, boolean tokenRefresh) {
        super();
        this.siteId = siteId;
        this.browserInfo = browserInfo;
        this.subscription = subscription;
        this.subscriptionUrl = subscriptionUrl;
        this.geoInfo = geoInfo;
        this.tokenRefresh = tokenRefresh;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public BrowserInfo getBrowserInfo() {
        return browserInfo;
    }

    public void setBrowserInfo(BrowserInfo browserInfo) {
        this.browserInfo = browserInfo;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getSubscriptionUrl() {
        return subscriptionUrl;
    }

    public void setSubscriptionUrl(String subscriptionUrl) {
        this.subscriptionUrl = subscriptionUrl;
    }

    public GeoInfo getGeoInfo() {
        return geoInfo;
    }

    public void setGeoInfo(GeoInfo geoInfo) {
        this.geoInfo = geoInfo;
    }

    public boolean isTokenRefresh() {
        return tokenRefresh;
    }

    public void setTokenRefresh(boolean tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }

    public class BrowserInfo {

        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("browser_version")
        @Expose
        private String browserVersion;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("device")
        @Expose
        private String device;

        /**
         * No args constructor for use in serialization
         */
        public BrowserInfo() {
        }

        /**
         * @param deviceType
         * @param browserVersion
         * @param language
         * @param device
         */
        public BrowserInfo(String deviceType, String browserVersion, String language, String device) {
            super();
            this.deviceType = deviceType;
            this.browserVersion = browserVersion;
            this.language = language;
            this.device = device;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getBrowserVersion() {
            return browserVersion;
        }

        public void setBrowserVersion(String browserVersion) {
            this.browserVersion = browserVersion;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

    }

    public class GeoInfo {

        @SerializedName("geobytestimezone")
        @Expose
        private String geobytestimezone;
        @SerializedName("geobytescountry")
        @Expose
        private String geobytescountry;
        @SerializedName("geobytesregion")
        @Expose
        private String geobytesregion;
        @SerializedName("geobytescity")
        @Expose
        private String geobytescity;

        /**
         * No args constructor for use in serialization
         */
        public GeoInfo() {
        }

        /**
         * @param geobytestimezone
         * @param geobytescity
         * @param geobytescountry
         * @param geobytesregion
         */
        public GeoInfo(String geobytestimezone, String geobytescountry, String geobytesregion, String geobytescity) {
            super();
            this.geobytestimezone = geobytestimezone;
            this.geobytescountry = geobytescountry;
            this.geobytesregion = geobytesregion;
            this.geobytescity = geobytescity;
        }

        public String getGeobytestimezone() {
            return geobytestimezone;
        }

        public void setGeobytestimezone(String geobytestimezone) {
            this.geobytestimezone = geobytestimezone;
        }

        public String getGeobytescountry() {
            return geobytescountry;
        }

        public void setGeobytescountry(String geobytescountry) {
            this.geobytescountry = geobytescountry;
        }

        public String getGeobytesregion() {
            return geobytesregion;
        }

        public void setGeobytesregion(String geobytesregion) {
            this.geobytesregion = geobytesregion;
        }

        public String getGeobytescity() {
            return geobytescity;
        }

        public void setGeobytescity(String geobytescity) {
            this.geobytescity = geobytescity;
        }

    }

    public class Subscription {

        @SerializedName("endpoint")
        @Expose
        private String endpoint;
        @SerializedName("project_id")
        @Expose
        private String projectId;

        /**
         * No args constructor for use in serialization
         */
        public Subscription() {
        }

        /**
         * @param endpoint
         * @param projectId
         */
        public Subscription(String endpoint, String projectId) {
            super();
            this.endpoint = endpoint;
            this.projectId = projectId;
        }

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getProjectId() {
            return projectId;
        }

        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }

    }

}