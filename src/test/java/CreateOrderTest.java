import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.praktikum.client.ScooterServiceClient;
import ru.yandex.praktikum.model.Order;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.greaterThan;

@RunWith(Parameterized.class)
public class CreateOrderTest {

  private final ScooterServiceClient client = new ScooterServiceClient();
  private final String firstName;
  private final String lastName;
  private final String address;
  private final String metroStation;
  private final String phone;
  private final int rentTime;
  private final String deliveryDate;
  private final String comment;
  private final String[] colors;

  public CreateOrderTest(String firstName, String lastName, String address, String metroStation, String phone,
                         int rentTime, String deliveryDate, String comment, String[] colors) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.address = address;
    this.metroStation = metroStation;
    this.phone = phone;
    this.rentTime = rentTime;
    this.deliveryDate = deliveryDate;
    this.comment = comment;
    this.colors = colors;
  }

  @Parameterized.Parameters
  public static Collection<Object[]> orderData() {
    return Arrays.asList(new Object[][]{
        {"Naruto", "Uchiha", "Konoha, 142 apt.", "4", "+7 800 355 35 35", 5, "2020-06-06", "Saske, come back to Konoha", new String[]{"BLACK"}},
        {"Sakura", "Haruno", "Konoha, 150 apt.", "12", "+7 900 123 45 67", 3, "2020-07-07", "I'll wait for you, Saske", new String[]{"GREY"}},
        {"Kakashi", "Hatake", "Konoha, 100 apt.", "5", "+7 700 123 00 00", 7, "2020-08-08", "Read my book", new String[]{"BLACK", "GREY"}},
        {"Itachi", "Uchiha", "Konoha, 180 apt.", "10", "+7 800 555 55 55", 10, "2020-09-09", "Forgive me, Saske", new String[]{}}
    });
  }

  @Test
  public void shouldCreateOrder() {
    Order order = new Order(
        firstName,
        lastName,
        address,
        metroStation,
        phone,
        rentTime,
        deliveryDate,
        comment,
        colors
    );

    Response response = client.createOrder(order);

    response.then()
        .assertThat()
        .statusCode(201)
        .and()
        .body("track", notNullValue())
        .and()
        .body("track", greaterThan(0));
  }
}
