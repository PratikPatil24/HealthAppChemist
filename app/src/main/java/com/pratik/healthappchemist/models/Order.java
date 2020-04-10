package com.pratik.healthappchemist.models;

import java.io.Serializable;

public class Order implements Serializable {

    String id, chemistID, patientID, doctorName, patientName, doctorID, doctorSpeciality, doctorDegree, date, medicines, orderType, type;
    int status;

    public Order() {
    }

    public Order(String id, String chemistID, String patientID, String doctorName, String patientName, String doctorID, String doctorSpeciality, String doctorDegree, String date, String medicines, String orderType, String type, int status) {
        this.id = id;
        this.chemistID = chemistID;
        this.patientID = patientID;
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorSpeciality = doctorSpeciality;
        this.doctorDegree = doctorDegree;
        this.date = date;
        this.medicines = medicines;
        this.orderType = orderType;
        this.type = type;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChemistID() {
        return chemistID;
    }

    public void setChemistID(String chemistID) {
        this.chemistID = chemistID;
    }

    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getDoctorSpeciality() {
        return doctorSpeciality;
    }

    public void setDoctorSpeciality(String doctorSpeciality) {
        this.doctorSpeciality = doctorSpeciality;
    }

    public String getDoctorDegree() {
        return doctorDegree;
    }

    public void setDoctorDegree(String doctorDegree) {
        this.doctorDegree = doctorDegree;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedicines() {
        return medicines;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
