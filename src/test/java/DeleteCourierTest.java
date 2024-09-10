import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.ScooterServiceClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.Credentials;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class DeleteCourierTest {
  private ScooterServiceClient client = new ScooterServiceClient();
  private Courier courier;
  private String courierId;

  @Before
  public void before() {
    courier = new Courier("courier" + UUID.randomUUID(), "password", "firstName");
    client.createCourier(courier);
    courierId = client.loginAndGetCourierId(Credentials.fromCourier(courier));
  }

  @After
  public void after() {
    if (courierId != null) {
      client.deleteCourier(courierId);
    }
  }

  @Test
  public void shouldDeleteCourier() {
    Response response = client.deleteCourier(courierId);

    response.then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("ok", is(true));

    courierId = null;
  }

  @Test
  public void shouldReturnErrorWhenDeletingNonExistentCourier() {
    client.deleteCourier(courierId);

    Response response = client.deleteCourier(courierId);

    response.then()
        .assertThat()
        .statusCode(404)
        .and()
        .body("message", equalTo("Курьера с таким id нет."));

    courierId = null;
  }
}
