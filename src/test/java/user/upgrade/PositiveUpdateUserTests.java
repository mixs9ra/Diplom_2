package user.upgrade;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserLogin;
import user.UserRegistration;

public class PositiveUpdateUserTests {
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
    @DisplayName("Позитивный тест: изменение данных пользователя с авторизацией")
    public void updateUserWithAuth() {
        User user = User.createdUser();
        var login = UserLogin.from(User.createdUser());
        User updateUser = User.updatedDada();

        check.userRegistration(user);
        accessToken = check.userAuthorization(login);

        ValidatableResponse updateResponse = check.changeDataUser(updateUser, accessToken);
        check.updateSuccess(updateResponse);

    }
}