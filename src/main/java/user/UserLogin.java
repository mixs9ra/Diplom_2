package user;

public class UserLogin {
    private String email;
    private String password;

    public UserLogin() {
    }

    // Конструктор с параметрами
    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Метод для создания UserLogin из объекта User
    public static UserLogin from(User user) {
        return new UserLogin(user.getEmail(), user.getPassword());
    }

    // Метод для создания объекта UserLogin с некорректными данными
    public static UserLogin incorrectData() {
        UserLogin invalidUser = new UserLogin("mix1316@yandex.ru", "kusqiV-5tugju-popqaj");
        return invalidUser;
    }

    // Геттер для email
    public String getEmail() {
        return email;
    }

    // Сеттер для email
    public void setEmail(String email) {
        this.email = email;
    }

    // Геттер для password
    public String getPassword() {
        return password;
    }

    // Сеттер для password
    public void setPassword(String password) {
        this.password = password;
    }
}