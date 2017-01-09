package com.gamedev.dreamteam.scriptureengine;

/**
 *  Класс реализующий манипуляцию состоянием ContextState через ContextScript,
 *  вызывая метод apply(ContextState state), проверяя выполнение условия (predicate)
 *  на текущем сотоянии, при условии выполнения которого выполняется заданное действие (action),
 *  или другой объект типа Script.
 */
public class Script implements Action {

    private final Predicate predicate;
    private final Action[] actions;

    /** Конструктор объекта, назначающий поля predicate и action (является массивом объектов типа Action):
     *   new Script(condition, action1, action2, new Script(condition2, action 3));  (пример 1)
     */
    Script(final Predicate predicate, final Action... actions){
        this.predicate = predicate;
        this.actions = actions;
    }


    /** Запускает выполнение скрипта, проверяя predicate на передоваемом параметре, и запускающий цепочку событий
     *  типа Action, вызывая поочередно у каждого метод apply:
     *  (из примера 1) - проверяется возвращаемое значение функции condition на передоваемом state,
     *                   если оно == true -> последовательно выполняются события action1 и action2,
     *                   далее, проиходит эквиавалентная операция во вложенном объекте Script
     */
    @Override
    public Void apply(ContextState state){
        if (predicate.apply(state))
            for (Action action : this.actions)
                action.apply(state);
        return null;
    }
}
