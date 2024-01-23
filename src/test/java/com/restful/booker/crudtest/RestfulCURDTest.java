package com.restful.booker.crudtest;

import com.restful.booker.info.RestfulSteps;
import com.restful.booker.model.BookingPojo;
import com.restful.booker.testbase.TestBase;
import com.restful.booker.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.annotations.Title;
import net.serenitybdd.junit.runners.SerenityRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;

import static org.hamcrest.Matchers.hasValue;

@RunWith(SerenityRunner.class)
public class RestfulCURDTest extends TestBase {

    static String token;
    static int id;
    static String firstname = "Mike" + TestUtils.getRandomValue();
    static String lastname = "Davison" + TestUtils.getRandomValue();
    static int totalprice = Integer.parseInt(1 + TestUtils.getRandomValue());
    static boolean depositpaid = true;
    static String additionalneeds = "Hair Dryer";

    @Steps
    RestfulSteps restfulSteps;

    @Title("This method will create a Token")
    @Test
    public void test001() {
        ValidatableResponse response = restfulSteps.getToken().statusCode(200);
        token = response.extract().path("token");

    }

    @Title("This method will create a booking")
    @Test
    public void test002() {
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        ValidatableResponse response = restfulSteps.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds).statusCode(200);
        id = response.extract().path("bookingid");
    }

    @Title("This method will verify new Booking ID creation")
    @Test
    public void test003() {
        ValidatableResponse response = restfulSteps.getSingleBookingIDs(id);
        ArrayList<?> booking = response.extract().path("bookingid");
        Assert.assertTrue(booking.contains(id));

    }

    @Title("This method will verify new Booking ID creation")
    @Test
    public void test004() {
        restfulSteps.getSingleBookingIDs(id).statusCode(200);

    }


    @Title("This method will updated a booking with ID")
    @Test
    public void test005() {
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2024-01-01");
        bookingdates.setCheckout("2024-02-01");
        additionalneeds = "Hair Dryer";
        restfulSteps.updateBookingWithID(id, token, firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        ValidatableResponse response = restfulSteps.getSingleBookingIDs(id);
        HashMap<String, ?> update = response.extract().path("");
        Assert.assertThat(update, hasValue("Hair Dryer"));
    }

    @Title("This method will delete a booking with ID")
    @Test
    public void test006() {
        restfulSteps.deleteABookingID(id, token).statusCode(201);
        restfulSteps.getSingleBookingIDs(id).statusCode(404);
    }


}


