package order.create;

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
public class PositiveCreateOrder {
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
            check.deleteSuccessfully(response);
        }
    }
    @Test
    @DisplayName("Позитивный тест: создание заказа с ингредиентами авторизованного пользователя")
    public void testCreateOrder() {
        User user = User.createdUser();
        var login = UserLogin.from(User.createdUser());

        check.userRegistration(user);   // Регистрация

        accessToken = check.userAuthorization(login); // Авторизация

        var ingredientsResponse = bill.getIngredientsResponse();    // Получение ингредиентов

        List<String> ingredientIds = bill.getIngredientIds(ingredientsResponse);  // Лист с ID ингредиентов

        List<String> selectedIngredientIds = bill.getFirstThreeIngredients(ingredientIds); // Получаем первые три ингредиента

        ValidatableResponse orderResponse = bill.createOrder(selectedIngredientIds, accessToken); // Создаем заказ

        bill.createSuccessful(orderResponse); // Проверяем успешное создание заказа
    }
}