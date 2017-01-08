package com.gamedev.dreamteam.scriptureengine;

/**
 * Интерфейс для реализации поведения функций как объектов от одного параметра
 */
public interface Function<A, T> {
    T apply(A arg); // Метод apply вызывает функцию от параметра
}
