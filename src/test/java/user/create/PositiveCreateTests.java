package user.create;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserRegistration;
public class PositiveCreateTests {
    private final UserRegistration check = new UserRegistration();
    private String accessToken;
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse response = check.userDelete(accessToken);
            check.deleteSuccessfully(response);
        }
    }
    @Test
    @DisplayName("Позитивный тест: создание уникального пользователя")
    public void createUser() {
        User user = User.createdUser();
        ValidatableResponse createResponse = check.userRegistration(user);
        var response = check.successfulCreated(createResponse);
        accessToken = check.extractToken(response);

    }
}