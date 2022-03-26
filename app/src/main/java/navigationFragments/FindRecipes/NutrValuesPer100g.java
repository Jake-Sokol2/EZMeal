
package navigationFragments.FindRecipes;

//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("jsonschema2pojo")
public class NutrValuesPer100g {

    @SerializedName("energy")
    @Expose
    private Double energy;
    @SerializedName("fat")
    @Expose
    private Double fat;
    @SerializedName("protein")
    @Expose
    private Double protein;
    @SerializedName("salt")
    @Expose
    private Double salt;
    @SerializedName("saturates")
    @Expose
    private Double saturates;
    @SerializedName("sugars")
    @Expose
    private Double sugars;

    public Double getEnergy() {
        return energy;
    }

    public void setEnergy(Double energy) {
        this.energy = energy;
    }

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getProtein() {
        return protein;
    }

    public void setProtein(Double protein) {
        this.protein = protein;
    }

    public Double getSalt() {
        return salt;
    }

    public void setSalt(Double salt) {
        this.salt = salt;
    }

    public Double getSaturates() {
        return saturates;
    }

    public void setSaturates(Double saturates) {
        this.saturates = saturates;
    }

    public Double getSugars() {
        return sugars;
    }

    public void setSugars(Double sugars) {
        this.sugars = sugars;
    }

}
