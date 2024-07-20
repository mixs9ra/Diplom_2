package order.receive;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.OrderProcess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserLogin;
import user.UserRegistration;
import java.util.List;

public class PositiveReceiveOrderTests {
    private final UserRegistration check = new UserRegistration();
    private final OrderProcess bill = new OrderProcess();
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            ValidatableResponse response = check.userDelete(accessToken);
            check.deleteSuccessfully(response); // Правильное имя метода
        }
    }

    @Test
    @DisplayName("Позитивный тест: получение списка заказа авторизованного пользователя")
    public void testGetOrdersWithAuth() {
        User user = User.createdUser();
        var login = UserLogin.from(User.createdUser());

        check.userRegistration(user);
        accessToken = check.userAuthorization(login);

        var ingredientsResponse = bill.getIngredientsResponse();
        List<String> ingredientIds = ingredientsResponse.jsonPath().getList("data._id");
        List<String> selectedIngredientIds = bill.getFirstThreeIngredients(ingredientIds);

        ValidatableResponse orderResponse = bill.createOrder(selectedIngredientIds, accessToken); // Создаем заказ

        bill.createSuccessful(orderResponse); // Проверяем успешное создание заказа

        ValidatableResponse listResponse = bill.getListOrder(accessToken); // Получаем список заказа
        bill.getListSuccessful(listResponse);
    }
}