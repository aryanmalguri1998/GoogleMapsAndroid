package com.example.practicev;

// DataClass is a model class that represents a data item with various attributes.
public class DataClass {

    // Attributes of the data item
    private String dataTitle;
    private String dataDesc;
    private String dataLang;
    private String dataImage;
    private String key;

    // Getter for the key attribute
    public String getKey() {
        return key;
    }

    // Setter for the key attribute
    public void setKey(String key) {
        this.key = key;
    }

    // Getter for the dataTitle attribute
    public String getDataTitle() {
        return dataTitle;
    }

    // Getter for the dataDesc attribute
    public String getDataDesc() {
        return dataDesc;
    }

    // Getter for the dataLang attribute
    public String getDataLang() {
        return dataLang;
    }

    // Getter for the dataImage attribute
    public String getDataImage() {
        return dataImage;
    }

    // Constructor that initializes all attributes
    public DataClass(String dataTitle, String dataDesc, String dataLang, String dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataImage = dataImage;
    }

    // Default constructor
    public DataClass() {

    }
}
