package user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;

import java.net.HttpURLConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertTrue;

public class UserRegistration { // Класс для регистрации и управления пользователями

    // Константы для путей к API
    public static final String USER_PATH = "/api/auth/register";
    public static final String LOGIN_PATH = "/api/auth/login";
    public static final String UPDATE_PATH = "/api/auth/user";
    public static final String DELETE_PATH = "/api/auth/user";

    // Метод для регистрации пользователя
    @Step("Регистрация пользователя")
    public ValidatableResponse userRegistration(User user) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .post(USER_PATH)
                .then().log().all();
    }

    // Метод для проверки успешной регистрации пользователя
    @Step("Проверка успешной регистрации пользователя")
    public Response successfulCreated(ValidatableResponse createResponse) {
        var response = createResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK) // Ожидается статус 200, хотя обычно 201 для создания ресурса
                .extract()
                .response();

        boolean created = response.path("success");
        assertTrue(created); // Проверка, что регистрация успешна
        return response;
    }

    // Метод для извлечения accessToken из ответа
    @Step("Извлечение accessToken")
    public String extractToken(Response response) {
        return response.path("accessToken");
    }

    // Метод для проверки ошибки при попытке регистрации уже существующего пользователя
    @Step("Проверка ответа системы на дублирование пользователя")
    public void creationError(ValidatableResponse duplicateResponse) {
        duplicateResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("message", equalTo("User already exists")); // Сообщение о том, что пользователь уже существует
    }

    // Метод для проверки ошибки при отсутствии обязательных полей в запросе
    @Step("Проверка ответа системы, если одного из обязательных полей нет")
    public ValidatableResponse withoutName(ValidatableResponse errorResponse) {
        return errorResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields")); // Сообщение о необходимости всех обязательных полей
    }

    // Метод для входа пользователя в систему
    @Step("Вход пользователя в систему")
    public ValidatableResponse userLogin(UserLogin login) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .body(login)
                .when()
                .post(LOGIN_PATH)
                .then().log().all();
    }

    // Метод для проверки успешной авторизации пользователя
    @Step("Проверка успешной авторизации")
    public Response authorization(ValidatableResponse loginResponse) {
        var response = loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .response();
        return response;
    }

    // Метод для проверки ошибки при вводе невалидных данных для авторизации
    @Step("Проверка ответа системы на ввод невалидных данных")
    public ValidatableResponse invalidCredentials(ValidatableResponse loginResponse) {
        return loginResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect")); // Сообщение об ошибке ввода данных
    }

    // Метод для авторизации пользователя и получения accessToken
    @Step("Авторизация пользователя")
    public String userAuthorization(UserLogin login) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(login)
                .when()
                .post(LOGIN_PATH)
                .then().log().all()
                .extract()
                .path("accessToken"); // Возвращаем accessToken из ответа
    }

    // Метод для изменения данных пользователя
    @Step("Изменение данных пользователя")
    public ValidatableResponse changeDataUser(User updateUser, String accessToken) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .and()
                .header("Authorization", accessToken)
                .and()
                .body(updateUser)
                .when()
                .patch(UPDATE_PATH)
                .then().log().all();
    }

    // Метод для проверки успешного изменения данных пользователя
    @Step("Проверка на успешное изменение данных")
    public ValidatableResponse updateSuccess(ValidatableResponse updateResponse) {
        return updateResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_OK)
                .body("success", equalTo(true)); // Проверяем успешность изменения данных
    }

    // Метод для попытки изменения данных без авторизации
    @Step("Изменение данных не авторизованного пользователя")
    public ValidatableResponse updateDataWithoutAuth(User updateUser) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(updateUser)
                .when()
                .patch(UPDATE_PATH)
                .then().log().all();
    }

    // Метод для проверки ошибки при попытке изменения данных без авторизации
    @Step("Проверка ответа системы на попытку изменить данные без авторизации")
    public ValidatableResponse errorAuthorization(ValidatableResponse errorResponse) {
        return errorResponse
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised")); // Сообщение о необходимости авторизации
    }

    // Метод для удаления пользователя
    @Step("Удаление пользователя")
    public ValidatableResponse userDelete(String accessToken) {
        return given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", accessToken)
                .when()
                .delete(DELETE_PATH)
                .then().log().all();
    }

    // Метод для проверки успешного удаления пользователя
    @Step("Успешное удаление пользователя")
    public void deleteSuccessfully(ValidatableResponse response) {
        response
                .assertThat()
                .statusCode(HttpURLConnection.HTTP_ACCEPTED); // Проверяем успешное удаление пользователя
    }
}