package user.upgrade;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserRegistration;

public class NegativeUpdateUserTests {
    private final UserRegistration check = new UserRegistration();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    @DisplayName("Негативный тест: изменение данных пользователя без авторизации")
    public void updateUserWithoutAuth() {
        User updateUser = User.updatedDada();
        ValidatableResponse errorResponse = check.updateDataWithoutAuth(updateUser);
        check.errorAuthorization(errorResponse);
    }
}