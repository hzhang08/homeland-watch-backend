package com.homelandwatch.model;


/*
Request table
        request_id
        request_type(ride, shop,companion)
        elderly_id
        volunteer_id
        request_start_time
        request_end_time
        request_start_location
        request_destination_location
        request_status (open, accepted,inprogress,fulfilled)
        note*/


public class RequestDAO {

    int requestId;
    String requestType;
    int elderlyId;
    int volunteerId;
    long requestStartTime;
    long requestEndTime;
    long startLocationLongtitude;
    long startLocationLatitude;
    long endLocationLongtitude;
    long endLocationLatitude;
    RequestStatus requestStatus;

    public enum RequestStatus {
        Open,
        Accepted,
        InProgress,
        Fulfilled
    }

    public RequestDAO() {

    }

    public RequestDAO(int requestId, String requestType, int elderlyId,
                      int volunteerId, long requestStartTime, long requestEndTime,
                      long startLocationLongtitude, long startLocationLatitude, long endLocationLongtitude,
                      long endLocationLatitude, RequestStatus requestStatus) {
        this.requestId = requestId;
        this.requestType = requestType;
        this.elderlyId = elderlyId;
        this.volunteerId = volunteerId;
        this.requestStartTime = requestStartTime;
        this.requestEndTime = requestEndTime;
        this.startLocationLongtitude = startLocationLongtitude;
        this.startLocationLatitude = startLocationLatitude;
        this.endLocationLongtitude = endLocationLongtitude;
        this.endLocationLatitude = endLocationLatitude;
        this.requestStatus = requestStatus;
    }


    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public int getElderlyId() {
        return elderlyId;
    }

    public void setElderlyId(int elderlyId) {
        this.elderlyId = elderlyId;
    }

    public int getVolunteerId() {
        return volunteerId;
    }

    public void setVolunteerId(int volunteerId) {
        this.volunteerId = volunteerId;
    }

    public long getRequestStartTime() {
        return requestStartTime;
    }

    public void setRequestStartTime(long requestStartTime) {
        this.requestStartTime = requestStartTime;
    }

    public long getRequestEndTime() {
        return requestEndTime;
    }

    public void setRequestEndTime(long requestEndTime) {
        this.requestEndTime = requestEndTime;
    }

    public long getStartLocationLongtitude() {
        return startLocationLongtitude;
    }

    public void setStartLocationLongtitude(long startLocationLongtitude) {
        this.startLocationLongtitude = startLocationLongtitude;
    }

    public long getStartLocationLatitude() {
        return startLocationLatitude;
    }

    public void setStartLocationLatitude(long startLocationLatitude) {
        this.startLocationLatitude = startLocationLatitude;
    }

    public long getEndLocationLongtitude() {
        return endLocationLongtitude;
    }

    public void setEndLocationLongtitude(long endLocationLongtitude) {
        this.endLocationLongtitude = endLocationLongtitude;
    }

    public long getEndLocationLatitude() {
        return endLocationLatitude;
    }

    public void setEndLocationLatitude(long endLocationLatitude) {
        this.endLocationLatitude = endLocationLatitude;
    }

    public RequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(RequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }




}
