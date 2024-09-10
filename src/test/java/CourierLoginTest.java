import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.client.ScooterServiceClient;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.model.Credentials;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;

public class CourierLoginTest {

  private ScooterServiceClient client = new ScooterServiceClient();
  private Courier courier;
  private String courierId;

  @Before
  public void before() {
    courier = new Courier("courier" + UUID.randomUUID(), "password", "firstName");
    client.createCourier(courier);
  }

  @After
  public void after() {
    if (courierId != null) {
      client.deleteCourier(courierId);
    }
  }

  @Test
  public void shouldLoginCourier() {
    Response response = client.loginCourier(Credentials.fromCourier(courier));

    courierId = response.then().extract().jsonPath().getString("id");

    response.then()
        .assertThat()
        .statusCode(200)
        .and()
        .body("id", equalTo(Integer.parseInt(courierId)));
  }

  @Test
  public void shouldNotLoginCourierIfNoLogin() {
    Response response = client.loginCourier(new Credentials(null, courier.getPassword()));

    response.then()
        .assertThat()
        .statusCode(400)
        .and()
        .body("message", equalTo("Недостаточно данных для входа"));
  }

  @Test
  public void shouldNotLoginCourierIfNoPassword() {
    Response response = client.loginCourier(new Credentials(courier.getLogin(), ""));

    response.then()
        .assertThat()
        .statusCode(400)
        .and()
        .body("message", equalTo("Недостаточно данных для входа"));
  }

  @Test
  public void shouldNotLoginCourierWithIncorrectLogin() {
    Response response = client.loginCourier(new Credentials("wrongLogin", courier.getPassword()));

    response.then()
        .assertThat()
        .statusCode(404)
        .and()
        .body("message", equalTo("Учетная запись не найдена"));
  }

  @Test
  public void shouldNotLoginCourierWithIncorrectPassword() {
    Response response = client.loginCourier(new Credentials(courier.getLogin(), "wrongPassword"));

    response.then()
        .assertThat()
        .statusCode(404)
        .and()
        .body("message", equalTo("Учетная запись не найдена"));
  }

  @Test
  public void shouldNotLoginNonExistentCourier() {
    Courier nonExistentCourier = new Courier("nonExistentLogin", "password", "firstName");
    Response response = client.loginCourier(Credentials.fromCourier(nonExistentCourier));

    response.then()
        .assertThat()
        .statusCode(404)
        .and()
        .body("message", equalTo("Учетная запись не найдена"));
  }
}
