package com.community.jboss.visitingcard.VisitingCard;

import com.google.gson.annotations.SerializedName;

public class VisitingCard {

    @SerializedName("name")
    public String name;
    @SerializedName("email")
    public String email;
    @SerializedName("phone")
    public String phone;
    @SerializedName("picture")
    public String picture;

    public VisitingCard() {

    }


}
