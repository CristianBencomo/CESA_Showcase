package com.example.cesamockv3;

import com.google.gson.annotations.SerializedName;

public class VinDecode {
//
//    private int year;
//
//    private String make;
//
//    private String model;

    @SerializedName("data")
    private SubData data;

    @SerializedName("message")
    private SubD message;

    public SubData getData() {
        return this.data;
    }

    public SubD getMessage() {
        return this.message;
    }

    public class SubData {
        private String year;
        private String make;
        private String model;

        public String getYear() {
            return this.year;
        }

        public String getMake() {
            return this.make;
        }

        public String getModel() {
            return this.model;
        }
    }
    public class SubD {
        private String code;
        private String message;
        private String credentials;
        private String version;
        private String endpoint;
        private String count;

        public String getCode(){
            return this.code;
        }

        public String getMessage(){
            return this.message;
        }
        public String getCredentials(){
            return this.credentials;
        }

        public String getVersion(){
            return this.version;
        }

        public String getEndpoint(){
            return this.endpoint;
        }

        public String getCount(){
            return this.count;
        }
    }
//  private
//    public String getData(){
//        return data;
//    }

//    public int getYear() {
//        return year;
//    }
//
//    public String getMake() {
//        return make;
//    }
//
//    public String getModel() {
//        return model;
//    }
}
