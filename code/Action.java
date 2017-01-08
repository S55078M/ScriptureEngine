package com.gamedev.dreamteam.scriptureengine;

/**
 * Интерфейс для функции-предиката, прининимающей текущее состояние, и возвращающей Boolean
 */
public interface Action extends Function<ContextState, Void>{
    @Override
    Void apply(ContextState state);
}