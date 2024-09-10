import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.ScooterServiceClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.Credentials;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public class GetOrdersTest {

  private final ScooterServiceClient client = new ScooterServiceClient();

  private Courier courier;
  private String courierId;

  @Test
  public void shouldReturnListOfOrders() {
    Response response = client.getOrders();

    response.then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("orders", notNullValue())
        .and()
        .body("orders", hasSize(greaterThan(0)))
        .and()
        .body("pageInfo", notNullValue())
        .and()
        .body("availableStations", notNullValue());
  }

  @Test
  public void shouldReturnErrorForNonExistentCourierId() {
    courier = new Courier("courier" + UUID.randomUUID(), "password123", "testName");
    client.createCourier(courier);
    courierId = client.loginAndGetCourierId(Credentials.fromCourier(courier));
    client.deleteCourier(courierId);

    Response response = client.getOrdersByCourierId(courierId);

    response.then()
        .assertThat()
        .statusCode(404)
        .and()
        .body("message", equalTo("Курьер с идентификатором " + courierId + " не найден"));
  }
}
