package com.rto_driving_test_rajasthan.Models;

public class AppointmentModel implements java.io.Serializable {
    private static final long serialVersionUID = 2112707044850739693L;
    private String So_Wo_Do;
    private String TEST_TYPE;
    private int DRIVER_NUMBER;
    private String Applicant_Last_Name;
    private String Reference_Number;
    private String Date_Of_Birth;
    private String Appointment_Date;
    private int Number;
    private String Photo_Status;
    private Object APPLICANT_PIC;
    private String Applicant_Name;
    private String Licence_Number;
    private String Receipt_Number;
    private String RTO_CODE;

    public String getSo_Wo_Do() {
        return this.So_Wo_Do;
    }

    public void setSo_Wo_Do(String So_Wo_Do) {
        this.So_Wo_Do = So_Wo_Do;
    }

    public String getTEST_TYPE() {
        return this.TEST_TYPE;
    }

    public void setTEST_TYPE(String TEST_TYPE) {
        this.TEST_TYPE = TEST_TYPE;
    }

    public int getDRIVER_NUMBER() {
        return this.DRIVER_NUMBER;
    }

    public void setDRIVER_NUMBER(int DRIVER_NUMBER) {
        this.DRIVER_NUMBER = DRIVER_NUMBER;
    }

    public String getApplicant_Last_Name() {
        return this.Applicant_Last_Name;
    }

    public void setApplicant_Last_Name(String Applicant_Last_Name) {
        this.Applicant_Last_Name = Applicant_Last_Name;
    }

    public String getReference_Number() {
        return this.Reference_Number;
    }

    public void setReference_Number(String Reference_Number) {
        this.Reference_Number = Reference_Number;
    }

    public String getDate_Of_Birth() {
        return this.Date_Of_Birth;
    }

    public void setDate_Of_Birth(String Date_Of_Birth) {
        this.Date_Of_Birth = Date_Of_Birth;
    }

    public String getAppointment_Date() {
        return this.Appointment_Date;
    }

    public void setAppointment_Date(String Appointment_Date) {
        this.Appointment_Date = Appointment_Date;
    }

    public int getNumber() {
        return this.Number;
    }

    public String getRTO_CODE() {
        return this.RTO_CODE;
    }


    public void setNumber(int Number) {
        this.Number = Number;
    }

    public String getPhoto_Status() {
        return this.Photo_Status;
    }

    public void setPhoto_Status(String Photo_Status) {
        this.Photo_Status = Photo_Status;
    }

    public Object getAPPLICANT_PIC() {
        return this.APPLICANT_PIC;
    }

    public void setAPPLICANT_PIC(Object APPLICANT_PIC) {
        this.APPLICANT_PIC = APPLICANT_PIC;
    }

    public String getApplicant_Name() {
        return this.Applicant_Name;
    }

    public void setApplicant_Name(String Applicant_Name) {
        this.Applicant_Name = Applicant_Name;
    }

    public String getLicence_Number() {
        return this.Licence_Number;
    }

    public void setLicence_Number(String Licence_Number) {
        this.Licence_Number = Licence_Number;
    }

    public String getReceipt_Number() {
        return this.Receipt_Number;
    }

    public void setReceipt_Number(String Receipt_Number) {
        this.Receipt_Number = Receipt_Number;
    }

    public void setRTO_CODE(String RTO_CODE) {
        this.RTO_CODE= RTO_CODE;
    }
}
