
package com.example.ezmeal.JsonRecipes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

//@Generated("jsonschema2pojo")
public class Example {

    /*
    @SerializedName("fsa_lights_per100g")
    @Expose
    private FsaLightsPer100g fsaLightsPer100g;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("instructions")
    @Expose
    private List<Instruction> instructions = null;
    @SerializedName("nutr_per_ingredient")
    @Expose
    private List<NutrPerIngredient> nutrPerIngredient = null;
    @SerializedName("nutr_values_per100g")
    @Expose
    private NutrValuesPer100g nutrValuesPer100g;
    @SerializedName("partition")
    @Expose
    private String partition;
    @SerializedName("quantity")
    @Expose
    private List<Quantity> quantity = null;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("unit")
    @Expose
    private List<Unit> unit = null;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("weight_per_ingr")
    @Expose
    private List<Double> weightPerIngr = null;

    public FsaLightsPer100g getFsaLightsPer100g() {
        return fsaLightsPer100g;
    }

    public void setFsaLightsPer100g(FsaLightsPer100g fsaLightsPer100g) {
        this.fsaLightsPer100g = fsaLightsPer100g;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Instruction> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<Instruction> instructions) {
        this.instructions = instructions;
    }

    public List<NutrPerIngredient> getNutrPerIngredient() {
        return nutrPerIngredient;
    }

    public void setNutrPerIngredient(List<NutrPerIngredient> nutrPerIngredient) {
        this.nutrPerIngredient = nutrPerIngredient;
    }

    public NutrValuesPer100g getNutrValuesPer100g() {
        return nutrValuesPer100g;
    }

    public void setNutrValuesPer100g(NutrValuesPer100g nutrValuesPer100g) {
        this.nutrValuesPer100g = nutrValuesPer100g;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public List<Quantity> getQuantity() {
        return quantity;
    }

    public void setQuantity(List<Quantity> quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Unit> getUnit() {
        return unit;
    }

    public void setUnit(List<Unit> unit) {
        this.unit = unit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<Double> getWeightPerIngr() {
        return weightPerIngr;
    }

    public void setWeightPerIngr(List<Double> weightPerIngr) {
        this.weightPerIngr = weightPerIngr;
    }
    */
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("ingredients")
    @Expose
    private List<Ingredient> ingredients = null;
    @SerializedName("instructions")
    @Expose
    private List<Instruction> instructions = null;
    @SerializedName("nutr_values_per100g")
    @Expose
    private NutrValuesPer100g nutrValuesPer100g;

    public String getId()
    {
        return id;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Instruction> getInstructions()
    {
        return instructions;
    }

    public NutrValuesPer100g getNutrValuesPer100g() {
        return nutrValuesPer100g;
    }
}
