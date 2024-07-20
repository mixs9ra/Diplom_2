package order.receive;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.OrderProcess;
import org.junit.Before;
import org.junit.Test;
public class NegativeReceiveOrderTests {
    private final OrderProcess bill = new OrderProcess();
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @Test
    @DisplayName("Негативный тест: получение заказа неавторизованного пользователя")
    public void testGetUserOrdersWithoutAuth() {
        ValidatableResponse listResponse = bill.getListOrderUnauthorized();
        bill.errorGetListOrder(listResponse);
    }
}