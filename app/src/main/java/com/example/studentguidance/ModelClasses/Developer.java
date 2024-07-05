package com.example.studentguidance.ModelClasses;

public class Developer {
    String name,role,details,url="null",docId="null";

    public Developer(){}
    public Developer(String devname, String role, String details, String url,String docId) {
        this.name = devname;
        this.role = role;
        this.details = details;
        this.url = url;
        this.docId=docId;
    }

    public String getName() {
        return name;
    }

    public void setName(String devname) {
        this.name = devname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}
