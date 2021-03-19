package com.pushengage.fcmlibrary.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubscriberHashResponse {

    @SerializedName("error_code")
    @Expose
    private Long errorCode;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("error_message")
    @Expose
    private String errorMessage;
    @SerializedName("error")
    @Expose
    private Error error;

    /**
     * No args constructor for use in serialization
     */
    public SubscriberHashResponse() {
    }

    /**
     * @param data
     * @param errorMessage
     * @param errorCode
     * @param error
     */
    public SubscriberHashResponse(Long errorCode, Data data, String errorMessage, Error error) {
        super();
        this.errorCode = errorCode;
        this.data = data;
        this.errorMessage = errorMessage;
        this.error = error;
    }

    public Long getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public class Data {


    }


    public class Error {

        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("code")
        @Expose
        private Long code;

        /**
         * No args constructor for use in serialization
         */
        public Error() {
        }

        /**
         * @param code
         * @param message
         */
        public Error(String message, Long code) {
            super();
            this.message = message;
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Long getCode() {
            return code;
        }

        public void setCode(Long code) {
            this.code = code;
        }

    }

}