package com.gamedev.dreamteam.scriptureengine;

/**
 * Интерфейс для функции-предиката, прининимающей текущее состояние, и возвращающей Boolean
 */
public interface Predicate extends Function<ContextState, Boolean> {
    @Override
    Boolean apply(ContextState state);
}
