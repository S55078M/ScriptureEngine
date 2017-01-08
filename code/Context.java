package com.gamedev.dreamteam.scriptureengine;

import java.util.Queue;

/**
 * Абстрактный класс, определяющий поведение GameEngine, при использовании.
 * Реализуется перегрузкой метода constructContext(), в котором определяются переменные renderer, state, scripts.
 */

// TODO: GameEngine
// TODO: Подобрать коллекцию объектов ContextScript
// TODO: Вариант, со скриптами, выполняющимися при старте и выходе из контекста
// TODO: Придумать, зачем нужен updateContext()
abstract public class Context {
    protected GameEngine currentGame;         // Ссылка на текущий обрабатываемый объект Activity
    protected ContextRenderer renderer;       // Объект, запускающий поток рендера
    protected ContextState state;             // Объект, представляющий изменяемое состояние всей системы
    protected Queue<ContextScript> scripts;   // Очередь обработчиков скриптов, запускаемых в разных потоках

    /**
     * Конструктор контекста, от объекта Activity (framework)
     * @param game - Activity, отображаемое данный контекст
     */
    Context(GameEngine game){
        this.currentGame = game;
        constructContext();
    }


    /**
     * Построение объекта контекста, заполняя поля renderer, state, scripts
     */
    abstract public void constructContext ();
    /*
        Пример объявления, с использованием требуемых классов ContextRenderer, ContextState, ContextScript:
        this.renderer = ContextRenderer(this);
        this.state = ContextState(this);
        this.scripts = new QueueRealisation<ContextScript>(...)
        this.scripts.add(ContextScript(this));
     */


    /**
     * Запускает выполнение контекста.
     * При чем поток для рендера (отображение контекста) запускается из GameEngine
     * @param old_state - Состояние предыдущего контекста
     */
    public final void startContext(ContextState old_state){
        state.constructState(old_state);

        for (ContextScript behavior : scripts)
            behavior.constructScripts();

        for (ContextScript behavior : scripts)
            behavior.start();
    }

    /**
     * Метод, запускаемый в mainLoop основного потока. Может быть перегружен
     */
    public void  updateContext(){

    }

    /**
     * Завершает выполнение контекста
     */
    public final void closeContext() {
        for (ContextScript behavior : scripts)
            behavior.start();
    }



    /**
     * Возвращает текщий объект Activity, от которого запущен контекст
     * @return  currentGame
     */
    public final GameEngine getCurrentGame() {
        return currentGame;
    }


    /**
     * Возвращает текущее обробатываемое состояние контекста
     * @return  state
     */
    public final ContextState getState() {
        return state;
    }
}
