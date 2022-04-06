
package com.example.ezmeal.JsonRecipes;

//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("jsonschema2pojo")
public class FsaLightsPer100g {

    @SerializedName("fat")
    @Expose
    private String fat;
    @SerializedName("salt")
    @Expose
    private String salt;
    @SerializedName("saturates")
    @Expose
    private String saturates;
    @SerializedName("sugars")
    @Expose
    private String sugars;

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getSaturates() {
        return saturates;
    }

    public void setSaturates(String saturates) {
        this.saturates = saturates;
    }

    public String getSugars() {
        return sugars;
    }

    public void setSugars(String sugars) {
        this.sugars = sugars;
    }

}
