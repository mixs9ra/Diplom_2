package user;


public class User {

    private String email;
    private String password;
    private String name;

    public User() {
    }

    // Конструктор с параметрами
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    // Метод для создания пользователя с предопределёнными данными
    public static User createdUser() {
        User user = new User("mix1316@yandex.ru", "kusqiV-5tugju-popqaj", "Денис");
        return user;
    }

    // Метод для создания пользователя без обязательного поля (имя)
    public static User noRequiredField() {
        User withoutName = new User("mix1316@yandex.ru", "kusqiV-5tugju-popqaj", null);
        return withoutName;
    }

    // Метод для создания пользователя с обновлёнными данными
    public static User updatedDada() {
        User updateUser = new User("Mix@yandex.ru", "password123", "Den");
        return updateUser;
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

    // Геттер для name
    public String getName() {
        return name;
    }

    // Сеттер для name
    public void setName(String name) {
        this.name = name;
    }
}