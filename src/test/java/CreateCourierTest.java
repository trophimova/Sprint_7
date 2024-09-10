import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.praktikum.model.Courier;
import ru.yandex.praktikum.client.ScooterServiceClient;
import ru.yandex.praktikum.model.Credentials;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

public class CreateCourierTest {

  private ScooterServiceClient client = new ScooterServiceClient();
  private Courier courier;
  private String courierId;

  @Before
  public void before() {
    courier = new Courier("courier" + UUID.randomUUID(), "password123", "testName");
  }

  @After
  public void after() {
    if (courierId != null) {
      client.deleteCourier(courierId);
    }
  }

  @Test
  public void shouldCreateCourier() {
    Response response = client.createCourier(courier);

    response.then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("ok", is(true));

    courierId = client.loginAndGetCourierId(Credentials.fromCourier(courier));
  }

  @Test
  public void shouldCreateCourierIfNoFirstName() {
    Courier noFirstNameCourier = new Courier(courier.getLogin(), courier.getPassword(), null);
    Response response = client.createCourier(noFirstNameCourier);

    response.then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("ok", is(true));

    courierId = client.loginAndGetCourierId(Credentials.fromCourier(noFirstNameCourier));
  }

  @Test
  public void shouldNotCreateDuplicateCourier() {
    Response responseFirstCourier = client.createCourier(courier);

    responseFirstCourier.then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("ok", is(true));

    courierId = client.loginAndGetCourierId(Credentials.fromCourier(courier));

    Response responseDuplicateCourier = client.createCourier(courier);

    responseDuplicateCourier.then()
        .assertThat()
        .statusCode(409)
        .and()
        .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
  }

  @Test
  public void shouldNotCreateCourierIfDuplicateLogin() {
    Response responseFirstCourier = client.createCourier(courier);

    responseFirstCourier.then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("ok", is(true));

    courierId = client.loginAndGetCourierId(Credentials.fromCourier(courier));

    Response responseSecondCourier = client.createCourier(new Courier(courier.getLogin(), "newPassword", "newFirstName"));

    responseSecondCourier.then()
        .assertThat()
        .statusCode(409)
        .and()
        .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
  }

  @Test
  public void shouldNotCreateCourierIfNoLogin() {
    Courier noLoginCourier = new Courier(null, courier.getPassword(), courier.getFirstName());
    Response response = client.createCourier(noLoginCourier);

    response.then()
        .assertThat()
        .statusCode(400)
        .and()
        .body("message", equalTo("Недостаточно данных для создания учетной записи"));
  }

  @Test
  public void shouldNotCreateCourierIfNoPassword() {
    Courier noPasswordCourier = new Courier(courier.getLogin(), null, courier.getFirstName());
    Response response = client.createCourier(noPasswordCourier);

    response.then()
        .assertThat()
        .statusCode(400)
        .and()
        .body("message", equalTo("Недостаточно данных для создания учетной записи"));
  }
}
