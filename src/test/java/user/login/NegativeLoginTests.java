package user.login;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.UserLogin;
import user.UserRegistration;
public class NegativeLoginTests {
    private final UserRegistration check = new UserRegistration();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    @DisplayName("Негативный тест: логин с неверным логином и паролем.")
    public void testInvalidData() {
        UserLogin invalidUser = UserLogin.incorrectData();

        ValidatableResponse loginResponse = check.userLogin(invalidUser);
        check.invalidCredentials(loginResponse);
    }
}