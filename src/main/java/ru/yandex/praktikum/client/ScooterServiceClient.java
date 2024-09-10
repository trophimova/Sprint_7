package ru.yandex.praktikum.client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.Credentials;
import ru.yandex.praktikum.model.Order;

import static io.restassured.RestAssured.given;

public class ScooterServiceClient {

  private static final String BASE_URL = "https://qa-scooter.praktikum-services.ru/";

  @Step("Создание курьера с логином {courier.login}")
  public Response createCourier(Courier courier) {
    return given().baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .body(courier)
        .post("/api/v1/courier");
  }

  @Step("Авторизация курьера с логином {credentials.login}")
  public Response loginCourier(Credentials credentials) {
    return given().baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .body(credentials)
        .post("/api/v1/courier/login");
  }

  @Step("Авторизация курьера с логином {credentials.login} и получение ID курьера")
  public String loginAndGetCourierId(Credentials credentials) {
    Response loginResponse = given()
        .baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .body(credentials)
        .post("/api/v1/courier/login");

    return loginResponse.then().extract().jsonPath().getString("id");
  }

  @Step("Удаление курьера с ID {courierId}")
  public Response deleteCourier(String courierId) {
    int id = Integer.parseInt(courierId);
    return given().baseUri(BASE_URL).header("Content-Type", "application/json").delete("/api/v1/courier/" + id);
  }

  @Step("Создание заказа")
  public Response createOrder(Order order) {
    return given().baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .body(order)
        .post("/api/v1/orders");
  }

  @Step("Получение списка всех заказов")
  public Response getOrders() {
    return given()
        .baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .get("/api/v1/orders");
  }

  @Step("Получение списка заказов по courierId {courierId}")
  public Response getOrdersByCourierId(String courierId) {
    return given()
        .baseUri(BASE_URL)
        .header("Content-Type", "application/json")
        .queryParam("courierId", courierId)
        .get("/api/v1/orders");
  }
}
