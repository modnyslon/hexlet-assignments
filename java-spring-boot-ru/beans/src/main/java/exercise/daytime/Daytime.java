package exercise.daytime;

import org.springframework.stereotype.Component;

// Интерфейс содержит метод, возвращающий название времени суток
// Реализация методов представлена в классах Morning, Day, Evening, Night,
// которые реализуют этот интерфейс
@Component
public interface Daytime {

    String getName();
}
