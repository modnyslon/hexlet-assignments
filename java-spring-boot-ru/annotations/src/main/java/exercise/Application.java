package exercise;

import exercise.model.Address;
import exercise.annotation.Inspect;
import java.lang.reflect.Method;

public class Application {
    public static void main(String[] args) {
        var address = new Address("London", 12345678);
        // Получаем все методы класса Address
        Method[] methods = Address.class.getDeclaredMethods();

        // BEGIN
        // Перебираем методы и проверяем наличие аннотации Inspect
        for (Method method : methods) {
            if (method.isAnnotationPresent(Inspect.class)) {
                // Получаем тип возвращаемого значения
                String returnType = method.getReturnType().getSimpleName();
                // Выводим информацию о методе
                System.out.println("Method " + method.getName() +
                        " returns a value of type " + returnType);
            }
        }


            // END

    }
}
