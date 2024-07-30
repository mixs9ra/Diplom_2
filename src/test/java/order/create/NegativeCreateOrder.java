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
public class NegativeCreateOrder {
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
    @DisplayName("Негативный тест: создание заказа без авторизации")
    public void testCreateOrderWithoutAuth() {
        var ingredientsResponse = bill.getIngredientsResponse(); // Получем список ингридиентов

        List<String> ingredientIds = bill.getIngredientIds(ingredientsResponse);  // Лист с ID ингредиентов

        List<String> selectedIngredientIds = bill.getFirstThreeIngredients(ingredientIds); // Получаем первые три ингредиента

        ValidatableResponse orderResponse = bill.createOrderWithoutAuth(selectedIngredientIds);  //Создаем заказ
        bill.createSuccessful(orderResponse); // Проверяем успешное создание заказа
    }
    @Test
    @DisplayName("Негативный тест: создание заказа без ингредиентов")
    public void testWithoutIngredients() {
        User user = User.createdUser();
        var login = UserLogin.from(User.createdUser());

        check.userRegistration(user);
        accessToken = check.userAuthorization(login);

        ValidatableResponse noIngredientsResponse = bill.createOrderWithoutIngredients(accessToken); // Создание заказа без ингредиентов
        bill.errorCreateOrder(noIngredientsResponse);
    }
    @Test
    @DisplayName("Негативный тест: создание заказа с неверным хешем ингредиентов")
    public void testWithInvalidIngredient() {
        User user = User.createdUser();
        var login = UserLogin.from(User.createdUser());

        check.userRegistration(user);
        accessToken = check.userAuthorization(login);

        ValidatableResponse invalidResponse = bill.passingInvalidHash(accessToken); // Создание заказа с не валидным хэш
        bill.errorCreate(invalidResponse);
    }
}