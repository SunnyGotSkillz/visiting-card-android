package com.community.jboss.visitingcard.VisitingCard;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

interface APIInterface {
    @GET("/api/visitingcard")
    Call<VisitingCard> doGetVisitingCard();

    @POST("/api/visitingcard")
    Call<VisitingCard> createVisitingCard(@Body VisitingCard card);
}
