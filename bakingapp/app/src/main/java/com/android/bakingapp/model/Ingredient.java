package com.android.bakingapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Christos on 26/01/2018.
 */

public class Ingredient implements Parcelable {

    private String measure;
    private String ingredient;
    private String quantity;

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return " [measure = " + measure +
                ", ingredient = " + ingredient +
                ", quantity = " + quantity + "]";
    }


    protected Ingredient(Parcel in) {
        measure = in.readString();
        ingredient = in.readString();
        quantity = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(measure);
        dest.writeString(ingredient);
        dest.writeString(quantity);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
}
