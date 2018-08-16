package in.co.iodev.keralarescue.Models;

public class DataModel {
    String number_of_people,lattitude,longitude,Locality,District,Battery_percentage,TimeIndex;

    public String getTimeIndex() {
        return TimeIndex;
    }

    public String getBattery_percentage() {
        return Battery_percentage;
    }

    public String getLattitude() {
        return lattitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLocality (){
        return Locality;
    }

    public String getDistrict() {
        return District;
    }

    public String getNumber_of_people() {
        return number_of_people;
    }

    public void setBattery_percentage(String battery_percentage) {
        Battery_percentage = battery_percentage;
    }

    public void setLattitude(String lattitude) {
        this.lattitude = lattitude;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public void setLocality(String locality) {
        Locality = locality;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setNumber_of_people(String number_of_people) {
        this.number_of_people = number_of_people;
    }

    public void setTimeIndex(String timeIndex) {
        TimeIndex = timeIndex;
    }
}
