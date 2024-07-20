package user.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserLogin;
import user.UserRegistration;
public class PositiveLoginTests {
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
    @DisplayName("Позитивный тест: логин под существующим пользователем")
    public void testLoginUser() {
        User user = User.createdUser();
        var login = UserLogin.from(User.createdUser());

        check.userRegistration(user);

        ValidatableResponse loginResponse = check.userLogin(login);
        var response = check.authorization(loginResponse);

        accessToken = check.extractToken(response);

    }
}