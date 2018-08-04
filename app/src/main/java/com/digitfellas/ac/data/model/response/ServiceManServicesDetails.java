package com.digitfellas.ac.data.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceManServicesDetails implements Parcelable
{

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("message")
    @Expose
    private String message;
    public final static Parcelable.Creator<ServiceManServicesDetails> CREATOR = new Creator<ServiceManServicesDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ServiceManServicesDetails createFromParcel(Parcel in) {
            return new ServiceManServicesDetails(in);
        }

        public ServiceManServicesDetails[] newArray(int size) {
            return (new ServiceManServicesDetails[size]);
        }

    }
            ;

    protected ServiceManServicesDetails(Parcel in) {
        this.success = ((Boolean) in.readValue((Boolean.class.getClassLoader())));
        this.data = ((Data) in.readValue((Data.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ServiceManServicesDetails() {
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(success);
        dest.writeValue(data);
        dest.writeValue(message);
    }

    public int describeContents() {
        return 0;
    }


    public static class Data implements Parcelable
    {

        @SerializedName("service_comments")
        @Expose
        private List<ServiceComment> serviceComments = null;
        @SerializedName("service")
        @Expose
        private Service service;
        @SerializedName("service_status")
        @Expose
        private List<ServiceStatus> serviceStatus = null;
        public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            public Data[] newArray(int size) {
                return (new Data[size]);
            }

        }
                ;

        protected Data(Parcel in) {
            in.readList(this.serviceComments, (ServiceComment.class.getClassLoader()));
            this.service = ((Service) in.readValue((Service.class.getClassLoader())));
            in.readList(this.serviceStatus, (ServiceStatus.class.getClassLoader()));
        }

        public Data() {
        }

        public List<ServiceComment> getServiceComments() {
            return serviceComments;
        }

        public void setServiceComments(List<ServiceComment> serviceComments) {
            this.serviceComments = serviceComments;
        }

        public Service getService() {
            return service;
        }

        public void setService(Service service) {
            this.service = service;
        }

        public List<ServiceStatus> getServiceStatus() {
            return serviceStatus;
        }

        public void setServiceStatus(List<ServiceStatus> serviceStatus) {
            this.serviceStatus = serviceStatus;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeList(serviceComments);
            dest.writeValue(service);
            dest.writeList(serviceStatus);
        }

        public int describeContents() {
            return 0;
        }



        public static class Service implements Parcelable
        {

            @SerializedName("id")
            @Expose
            private Integer id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("assigned_by")
            @Expose
            private Object assignedBy;
            @SerializedName("assigned_to")
            @Expose
            private String assignedTo;
            @SerializedName("customer_email")
            @Expose
            private String customerEmail;
            @SerializedName("address_1")
            @Expose
            private String address1;
            @SerializedName("address_2")
            @Expose
            private String address2;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("state_id")
            @Expose
            private String stateId;
            @SerializedName("zipcode")
            @Expose
            private String zipcode;
            @SerializedName("mobile_no")
            @Expose
            private String mobileNo;
            @SerializedName("brand_id")
            @Expose
            private String brandId;
            @SerializedName("model_name")
            @Expose
            private String modelName;
            @SerializedName("ton_capacity")
            @Expose
            private String tonCapacity;
            @SerializedName("problem_type_id")
            @Expose
            private String problemTypeId;
            @SerializedName("place_at_home")
            @Expose
            private String placeAtHome;
            @SerializedName("complaint")
            @Expose
            private Object complaint;
            @SerializedName("prefer_time_id")
            @Expose
            private String preferTimeId;
            @SerializedName("prefer_date")
            @Expose
            private String preferDate;
            @SerializedName("comments")
            @Expose
            private Object comments;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("lng")
            @Expose
            private Object lng;
            @SerializedName("lat")
            @Expose
            private Object lat;
            @SerializedName("created_at")
            @Expose
            private String createdAt;
            @SerializedName("updated_at")
            @Expose
            private String updatedAt;
            @SerializedName("brand_name")
            @Expose
            private String brandName;
            @SerializedName("state_name")
            @Expose
            private String stateName;
            @SerializedName("prefer_time")
            @Expose
            private String preferTime;
            @SerializedName("problem_type")
            @Expose
            private String problemType;
            public final static Parcelable.Creator<Service> CREATOR = new Creator<Service>() {


                @SuppressWarnings({
                        "unchecked"
                })
                public Service createFromParcel(Parcel in) {
                    return new Service(in);
                }

                public Service[] newArray(int size) {
                    return (new Service[size]);
                }

            }
                    ;

            protected Service(Parcel in) {
                this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
                this.userId = ((String) in.readValue((String.class.getClassLoader())));
                this.assignedBy = ((Object) in.readValue((Object.class.getClassLoader())));
                this.assignedTo = ((String) in.readValue((String.class.getClassLoader())));
                this.customerEmail = ((String) in.readValue((String.class.getClassLoader())));
                this.address1 = ((String) in.readValue((String.class.getClassLoader())));
                this.address2 = ((String) in.readValue((String.class.getClassLoader())));
                this.city = ((String) in.readValue((String.class.getClassLoader())));
                this.stateId = ((String) in.readValue((String.class.getClassLoader())));
                this.zipcode = ((String) in.readValue((String.class.getClassLoader())));
                this.mobileNo = ((String) in.readValue((String.class.getClassLoader())));
                this.brandId = ((String) in.readValue((String.class.getClassLoader())));
                this.modelName = ((String) in.readValue((String.class.getClassLoader())));
                this.tonCapacity = ((String) in.readValue((String.class.getClassLoader())));
                this.problemTypeId = ((String) in.readValue((String.class.getClassLoader())));
                this.placeAtHome = ((String) in.readValue((String.class.getClassLoader())));
                this.complaint = ((Object) in.readValue((Object.class.getClassLoader())));
                this.preferTimeId = ((String) in.readValue((String.class.getClassLoader())));
                this.preferDate = ((String) in.readValue((String.class.getClassLoader())));
                this.comments = ((Object) in.readValue((Object.class.getClassLoader())));
                this.status = ((String) in.readValue((String.class.getClassLoader())));
                this.lng = ((Object) in.readValue((Object.class.getClassLoader())));
                this.lat = ((Object) in.readValue((Object.class.getClassLoader())));
                this.createdAt = ((String) in.readValue((String.class.getClassLoader())));
                this.updatedAt = ((String) in.readValue((String.class.getClassLoader())));
                this.brandName = ((String) in.readValue((String.class.getClassLoader())));
                this.stateName = ((String) in.readValue((String.class.getClassLoader())));
                this.preferTime = ((String) in.readValue((String.class.getClassLoader())));
                this.problemType = ((String) in.readValue((String.class.getClassLoader())));
            }

            public Service() {
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public Object getAssignedBy() {
                return assignedBy;
            }

            public void setAssignedBy(Object assignedBy) {
                this.assignedBy = assignedBy;
            }

            public String getAssignedTo() {
                return assignedTo;
            }

            public void setAssignedTo(String assignedTo) {
                this.assignedTo = assignedTo;
            }

            public String getCustomerEmail() {
                return customerEmail;
            }

            public void setCustomerEmail(String customerEmail) {
                this.customerEmail = customerEmail;
            }

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getAddress2() {
                return address2;
            }

            public void setAddress2(String address2) {
                this.address2 = address2;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getStateId() {
                return stateId;
            }

            public void setStateId(String stateId) {
                this.stateId = stateId;
            }

            public String getZipcode() {
                return zipcode;
            }

            public void setZipcode(String zipcode) {
                this.zipcode = zipcode;
            }

            public String getMobileNo() {
                return mobileNo;
            }

            public void setMobileNo(String mobileNo) {
                this.mobileNo = mobileNo;
            }

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getModelName() {
                return modelName;
            }

            public void setModelName(String modelName) {
                this.modelName = modelName;
            }

            public String getTonCapacity() {
                return tonCapacity;
            }

            public void setTonCapacity(String tonCapacity) {
                this.tonCapacity = tonCapacity;
            }

            public String getProblemTypeId() {
                return problemTypeId;
            }

            public void setProblemTypeId(String problemTypeId) {
                this.problemTypeId = problemTypeId;
            }

            public String getPlaceAtHome() {
                return placeAtHome;
            }

            public void setPlaceAtHome(String placeAtHome) {
                this.placeAtHome = placeAtHome;
            }

            public Object getComplaint() {
                return complaint;
            }

            public void setComplaint(Object complaint) {
                this.complaint = complaint;
            }

            public String getPreferTimeId() {
                return preferTimeId;
            }

            public void setPreferTimeId(String preferTimeId) {
                this.preferTimeId = preferTimeId;
            }

            public String getPreferDate() {
                return preferDate;
            }

            public void setPreferDate(String preferDate) {
                this.preferDate = preferDate;
            }

            public Object getComments() {
                return comments;
            }

            public void setComments(Object comments) {
                this.comments = comments;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Object getLng() {
                return lng;
            }

            public void setLng(Object lng) {
                this.lng = lng;
            }

            public Object getLat() {
                return lat;
            }

            public void setLat(Object lat) {
                this.lat = lat;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getUpdatedAt() {
                return updatedAt;
            }

            public void setUpdatedAt(String updatedAt) {
                this.updatedAt = updatedAt;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getStateName() {
                return stateName;
            }

            public void setStateName(String stateName) {
                this.stateName = stateName;
            }

            public String getPreferTime() {
                return preferTime;
            }

            public void setPreferTime(String preferTime) {
                this.preferTime = preferTime;
            }

            public String getProblemType() {
                return problemType;
            }

            public void setProblemType(String problemType) {
                this.problemType = problemType;
            }

            public void writeToParcel(Parcel dest, int flags) {
                dest.writeValue(id);
                dest.writeValue(userId);
                dest.writeValue(assignedBy);
                dest.writeValue(assignedTo);
                dest.writeValue(customerEmail);
                dest.writeValue(address1);
                dest.writeValue(address2);
                dest.writeValue(city);
                dest.writeValue(stateId);
                dest.writeValue(zipcode);
                dest.writeValue(mobileNo);
                dest.writeValue(brandId);
                dest.writeValue(modelName);
                dest.writeValue(tonCapacity);
                dest.writeValue(problemTypeId);
                dest.writeValue(placeAtHome);
                dest.writeValue(complaint);
                dest.writeValue(preferTimeId);
                dest.writeValue(preferDate);
                dest.writeValue(comments);
                dest.writeValue(status);
                dest.writeValue(lng);
                dest.writeValue(lat);
                dest.writeValue(createdAt);
                dest.writeValue(updatedAt);
                dest.writeValue(brandName);
                dest.writeValue(stateName);
                dest.writeValue(preferTime);
                dest.writeValue(problemType);
            }

            public int describeContents() {
                return 0;
            }

        }



        public static class ServiceComment implements Parcelable
        {

            @SerializedName("comment")
            @Expose
            private String comment;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("user_id")
            @Expose
            private String userId;
            public final static Parcelable.Creator<ServiceComment> CREATOR = new Creator<ServiceComment>() {


                @SuppressWarnings({
                        "unchecked"
                })
                public ServiceComment createFromParcel(Parcel in) {
                    return new ServiceComment(in);
                }

                public ServiceComment[] newArray(int size) {
                    return (new ServiceComment[size]);
                }

            }
                    ;

            protected ServiceComment(Parcel in) {
                this.comment = ((String) in.readValue((String.class.getClassLoader())));
                this.name = ((String) in.readValue((String.class.getClassLoader())));
                this.userId = ((String) in.readValue((String.class.getClassLoader())));
            }

            public ServiceComment() {
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public void writeToParcel(Parcel dest, int flags) {
                dest.writeValue(comment);
                dest.writeValue(name);
                dest.writeValue(userId);
            }

            public int describeContents() {
                return 0;
            }

        }


        public static class ServiceStatus implements Parcelable
        {

            @SerializedName("sstatus")
            @Expose
            private String sstatus;
            @SerializedName("id")
            @Expose
            private Integer id;
            public final static Parcelable.Creator<ServiceStatus> CREATOR = new Creator<ServiceStatus>() {


                @SuppressWarnings({
                        "unchecked"
                })
                public ServiceStatus createFromParcel(Parcel in) {
                    return new ServiceStatus(in);
                }

                public ServiceStatus[] newArray(int size) {
                    return (new ServiceStatus[size]);
                }

            }
                    ;

            protected ServiceStatus(Parcel in) {
                this.sstatus = ((String) in.readValue((String.class.getClassLoader())));
                this.id = ((Integer) in.readValue((Integer.class.getClassLoader())));
            }

            public ServiceStatus() {
            }

            public String getSstatus() {
                return sstatus;
            }

            public void setSstatus(String sstatus) {
                this.sstatus = sstatus;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public void writeToParcel(Parcel dest, int flags) {
                dest.writeValue(sstatus);
                dest.writeValue(id);
            }

            public int describeContents() {
                return 0;
            }

        }


















    }



}