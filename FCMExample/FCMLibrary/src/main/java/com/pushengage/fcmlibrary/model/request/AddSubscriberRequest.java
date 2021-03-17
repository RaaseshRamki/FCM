package com.pushengage.fcmlibrary.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddSubscriberRequest {
    @SerializedName("site_id")
    @Expose
    private Long siteId;
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
    private Boolean tokenRefresh;
    @SerializedName("optin_type")
    @Expose
    private Long optinType;

    /**
     * No args constructor for use in serialization
     */
    public AddSubscriberRequest() {
    }

    /**
     * @param tokenRefresh
     * @param geoInfo
     * @param optinType
     * @param siteId
     * @param subscription
     * @param browserInfo
     * @param subscriptionUrl
     */
    public AddSubscriberRequest(Long siteId, BrowserInfo browserInfo, Subscription subscription, String subscriptionUrl, GeoInfo geoInfo, Boolean tokenRefresh, Long optinType) {
        super();
        this.siteId = siteId;
        this.browserInfo = browserInfo;
        this.subscription = subscription;
        this.subscriptionUrl = subscriptionUrl;
        this.geoInfo = geoInfo;
        this.tokenRefresh = tokenRefresh;
        this.optinType = optinType;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(Long siteId) {
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

    public Boolean getTokenRefresh() {
        return tokenRefresh;
    }

    public void setTokenRefresh(Boolean tokenRefresh) {
        this.tokenRefresh = tokenRefresh;
    }

    public Long getOptinType() {
        return optinType;
    }

    public void setOptinType(Long optinType) {
        this.optinType = optinType;
    }


    public class BrowserInfo {

        @SerializedName("device_type")
        @Expose
        private String deviceType;
        @SerializedName("browser_version")
        @Expose
        private String browserVersion;
        @SerializedName("user_agent")
        @Expose
        private String userAgent;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("total_scr_width_height")
        @Expose
        private String totalScrWidthHeight;
        @SerializedName("available_scr_width_height")
        @Expose
        private String availableScrWidthHeight;
        @SerializedName("colour_resolution")
        @Expose
        private Long colourResolution;
        @SerializedName("host")
        @Expose
        private String host;
        @SerializedName("device")
        @Expose
        private String device;
        @SerializedName("pe_ref_url")
        @Expose
        private String peRefUrl;

        /**
         * No args constructor for use in serialization
         */
        public BrowserInfo() {
        }

        /**
         * @param deviceType
         * @param colourResolution
         * @param peRefUrl
         * @param browserVersion
         * @param host
         * @param userAgent
         * @param language
         * @param totalScrWidthHeight
         * @param availableScrWidthHeight
         * @param device
         */
        public BrowserInfo(String deviceType, String browserVersion, String userAgent, String language, String totalScrWidthHeight, String availableScrWidthHeight, Long colourResolution, String host, String device, String peRefUrl) {
            super();
            this.deviceType = deviceType;
            this.browserVersion = browserVersion;
            this.userAgent = userAgent;
            this.language = language;
            this.totalScrWidthHeight = totalScrWidthHeight;
            this.availableScrWidthHeight = availableScrWidthHeight;
            this.colourResolution = colourResolution;
            this.host = host;
            this.device = device;
            this.peRefUrl = peRefUrl;
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

        public String getUserAgent() {
            return userAgent;
        }

        public void setUserAgent(String userAgent) {
            this.userAgent = userAgent;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getTotalScrWidthHeight() {
            return totalScrWidthHeight;
        }

        public void setTotalScrWidthHeight(String totalScrWidthHeight) {
            this.totalScrWidthHeight = totalScrWidthHeight;
        }

        public String getAvailableScrWidthHeight() {
            return availableScrWidthHeight;
        }

        public void setAvailableScrWidthHeight(String availableScrWidthHeight) {
            this.availableScrWidthHeight = availableScrWidthHeight;
        }

        public Long getColourResolution() {
            return colourResolution;
        }

        public void setColourResolution(Long colourResolution) {
            this.colourResolution = colourResolution;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getPeRefUrl() {
            return peRefUrl;
        }

        public void setPeRefUrl(String peRefUrl) {
            this.peRefUrl = peRefUrl;
        }

    }

    public class GeoInfo {

        @SerializedName("geobytestimezone")
        @Expose
        private String geobytestimezone;
        @SerializedName("geobytescountry")
        @Expose
        private String geobytescountry;
        @SerializedName("geobytesinternet")
        @Expose
        private String geobytesinternet;
        @SerializedName("geobytesregion")
        @Expose
        private String geobytesregion;
        @SerializedName("geobytescode")
        @Expose
        private String geobytescode;
        @SerializedName("geobytescity")
        @Expose
        private String geobytescity;
        @SerializedName("geobytesfqcn")
        @Expose
        private String geobytesfqcn;
        @SerializedName("geobytesipaddress")
        @Expose
        private String geobytesipaddress;

        /**
         * No args constructor for use in serialization
         */
        public GeoInfo() {
        }

        /**
         * @param geobytestimezone
         * @param geobytesipaddress
         * @param geobytesinternet
         * @param geobytescity
         * @param geobytescountry
         * @param geobytesregion
         * @param geobytescode
         * @param geobytesfqcn
         */
        public GeoInfo(String geobytestimezone, String geobytescountry, String geobytesinternet, String geobytesregion, String geobytescode, String geobytescity, String geobytesfqcn, String geobytesipaddress) {
            super();
            this.geobytestimezone = geobytestimezone;
            this.geobytescountry = geobytescountry;
            this.geobytesinternet = geobytesinternet;
            this.geobytesregion = geobytesregion;
            this.geobytescode = geobytescode;
            this.geobytescity = geobytescity;
            this.geobytesfqcn = geobytesfqcn;
            this.geobytesipaddress = geobytesipaddress;
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

        public String getGeobytesinternet() {
            return geobytesinternet;
        }

        public void setGeobytesinternet(String geobytesinternet) {
            this.geobytesinternet = geobytesinternet;
        }

        public String getGeobytesregion() {
            return geobytesregion;
        }

        public void setGeobytesregion(String geobytesregion) {
            this.geobytesregion = geobytesregion;
        }

        public String getGeobytescode() {
            return geobytescode;
        }

        public void setGeobytescode(String geobytescode) {
            this.geobytescode = geobytescode;
        }

        public String getGeobytescity() {
            return geobytescity;
        }

        public void setGeobytescity(String geobytescity) {
            this.geobytescity = geobytescity;
        }

        public String getGeobytesfqcn() {
            return geobytesfqcn;
        }

        public void setGeobytesfqcn(String geobytesfqcn) {
            this.geobytesfqcn = geobytesfqcn;
        }

        public String getGeobytesipaddress() {
            return geobytesipaddress;
        }

        public void setGeobytesipaddress(String geobytesipaddress) {
            this.geobytesipaddress = geobytesipaddress;
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