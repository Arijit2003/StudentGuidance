package com.example.studentguidance.ModelClasses;

import org.parceler.Parcel;

@Parcel
public class Faculty {
    String documentid,name,designation,department,cabinnumber,email,imagelink;
    public Faculty(){}

    public Faculty(String documentid,String name, String designation, String department, String cabinnumber, String email, String imagelink) {
        this.documentid=documentid;
        this.name = name;
        this.designation = designation;
        this.department = department;
        this.cabinnumber = cabinnumber;
        this.email = email;
        this.imagelink = imagelink;
    }

    public String getDocumentid() {
        return documentid;
    }

    public void setDocumentid(String documentid) {
        this.documentid = documentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCabinnumber() {
        return cabinnumber;
    }

    public void setCabinnumber(String cabinnumber) {
        this.cabinnumber = cabinnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }
}
