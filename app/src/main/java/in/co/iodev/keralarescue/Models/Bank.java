package in.co.iodev.keralarescue.Models;

import java.io.Serializable;

public class Bank implements Serializable {
    private String bankName;
    private int thumbnail;
    private int qrcode;
    private String branch;
    private String acNumber;
    private String acName;
    private String ifsc;
    private String upi;

    public Bank(String bankName , int thumbnail , int qrcode , String branch , String acName , String acNumber , String ifsc , String upi) {
        this.bankName = bankName;
        this.thumbnail = thumbnail;
        this.qrcode = qrcode;
        this.branch = branch;
        this.acName = acName;
        this.acNumber = acNumber;
        this.ifsc = ifsc;
        this.upi = upi;
    }

    public Bank(String bankName , int thumbnail) {
        this.bankName = bankName;
        this.thumbnail = thumbnail;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getBankName() {
        return bankName;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public int getQrcode() {
        return qrcode;
    }

    public void setQrcode(int qrcode) {
        this.qrcode = qrcode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAcNumber() {
        return acNumber;
    }

    public void setAcNumber(String acNumber) {
        this.acNumber = acNumber;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getIfsc() {
        return ifsc;
    }

    public void setIfsc(String ifsc) {
        this.ifsc = ifsc;
    }

    public String getUpi() {
        return upi;
    }

    public void setUpi(String upi) {
        this.upi = upi;
    }
}
