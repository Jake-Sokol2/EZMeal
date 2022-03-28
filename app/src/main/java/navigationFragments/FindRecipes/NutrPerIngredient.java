
package navigationFragments.FindRecipes;

//import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//@Generated("jsonschema2pojo")
public class NutrPerIngredient {

    @SerializedName("fat")
    @Expose
    private Double fat;
    @SerializedName("nrg")
    @Expose
    private Double nrg;
    @SerializedName("pro")
    @Expose
    private Double pro;
    @SerializedName("sat")
    @Expose
    private Double sat;
    @SerializedName("sod")
    @Expose
    private Double sod;
    @SerializedName("sug")
    @Expose
    private Double sug;

    public Double getFat() {
        return fat;
    }

    public void setFat(Double fat) {
        this.fat = fat;
    }

    public Double getNrg() {
        return nrg;
    }

    public void setNrg(Double nrg) {
        this.nrg = nrg;
    }

    public Double getPro() {
        return pro;
    }

    public void setPro(Double pro) {
        this.pro = pro;
    }

    public Double getSat() {
        return sat;
    }

    public void setSat(Double sat) {
        this.sat = sat;
    }

    public Double getSod() {
        return sod;
    }

    public void setSod(Double sod) {
        this.sod = sod;
    }

    public Double getSug() {
        return sug;
    }

    public void setSug(Double sug) {
        this.sug = sug;
    }

}
