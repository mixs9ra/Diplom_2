package order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderProcess { // Объявление класса

    // Константы для путей к API
    public static final String ORDER_PATH = "/api/orders";
    public static final String INGREDIENTS_PATH = "/api/ingredients";

    // Метод для получения списка ингредиентов
    @Step("Получения списка ингридиентов")
    public Response getIngredientsResponse() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(INGREDIENTS_PATH)
                .then().log().all()
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .extract()
                .response();
    }

    // Метод для получения ID ингредиентов из ответа
    @Step("Получение ID ингредиентов из ответа и формирование списка")
    public List<String> getIngredientIds(Response ingredientsResponse) {
        return ingredientsResponse.jsonPath().getList("data._id");
    }

    // Метод для получения первых трёх ингредиентов
    @Step("Получаем первые три ингредиента для дальнейшего формирования заказа")
    public List<String> getFirstThreeIngredients(List<String> ingredientIds) {
        return ingredientIds.subList(0, 3);
    }

    // Метод для авторизации и создания заказа
    @Step("Авторизовываемся и создаем заказ")
    public ValidatableResponse createOrder(List<String> ingredientIds, String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(new Ingredient(ingredientIds))
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    // Метод для создания заказа без авторизации
    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(List<String> ingredientIds) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(new Ingredient(ingredientIds))
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    // Метод для проверки успешного создания заказа
    @Step("Проверка создания заказа")
    public void createSuccessful(ValidatableResponse orderResponse) {
        orderResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true));
    }

    // Метод для создания заказа без ингредиентов
    @Step("Создание заказа без ингридиентов")
    public ValidatableResponse createOrderWithoutIngredients(String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body("{ \"ingredients\": [] }")
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    // Метод для проверки ошибки при создании заказа без ID ингредиентов
    @Step("Проверка что заказ не создается без id ингредиентов")
    public void errorCreateOrder(ValidatableResponse noIngredientsResponse) {
        noIngredientsResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    // Метод для создания заказа с неверным хешем ингредиентов
    @Step("Создание заказа с неверным хешем ингридиентов")
    public ValidatableResponse passingInvalidHash(String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body("{ \"ingredients\": [\"61c0c5a71d1f82001bda\"] }")
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    // Метод для проверки ошибки при создании заказа с неверным хешем ингредиентов
    @Step("Проверка возврата ошибки системы при создании заказа с неверным хешем ингредиентов")
    public void errorCreate(ValidatableResponse invalidResponse) {
        invalidResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    // Метод для получения списка заказов
    @Step("Get запрос на получение списка заказа")
    public ValidatableResponse getListOrder(String accessToken) {
        return given().log().all()
                .header("Authorization", accessToken)
                .header("Content-type", "application/json")
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    // Метод для проверки успешного получения списка заказов
    @Step("Проверка успешного получения списка заказов")
    public void getListSuccessful(ValidatableResponse listResponse) {
        listResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("orders.size()", equalTo(1))
                .body("total", not(isEmptyOrNullString()))
                .body("totalToday", not(isEmptyOrNullString()));
    }

    // Метод для получения списка заказов без авторизации
    @Step("Получение списка заказов без авторизации")
    public ValidatableResponse getListOrderUnauthorized() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }

    // Метод для проверки ошибки при получении списка заказов без авторизации
    @Step("Проверка ответа системы на запрос списка без авторизации")
    public void errorGetListOrder(ValidatableResponse listResponse) {
        listResponse.assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}