package com.gamedev.dreamteam.scriptureengine;

/**
 * Абстрактный класс, осуществляющий взаимодействие с информацией об объектах.
 * (Представляет изменяемое состояние о системе)
 * Для реализации необходимо сконструировать начальное состояние, через метод constructState(ContextState state),
 * принимающий в качестве аргумента объект состояния, от которого строится данное
 */
// TODO: GameEngine
// TODO: Сконструировать поведение класса, в зависимости от реализации объектов (BaseObject) + доделать конструктор
// TODO: Выбрать коллекцию для объектов
// TODO: Сделать в GameEngine настройки первого запуска, к примеру для создания объекта Screen, или через вариант с передаванием первого контекста, как не пустого.
abstract public class ContextState {
    protected GameEngine gameEngine;      // Ссылка на действующую Активность
    protected Screen screen;              // Объект, отвечающий за отображаемую область на экране, при рендере
    protected long idCounter;             // Счетчик программных тактов
    protected String name;                // Название состояния (К примеру "Меню", "Первый уровень")

    protected DataBase dataBase;          // Ссылка на объект из gameEngine, реализующий управление базой данных
    protected Input input;                // Ссылка на объект из gameEngine, обрабатывающий сигналы ввода
    protected SoundSystem soundSystem;    // Ссылка на объект из gameEngine, управляющий воспроизведением звуков

    private Layout[] drawbleObjects;                    // Массив абстракций видимых объектов
    private NonDrawableObjects[] nonDrawbleObjects;     // Массив абстракций-объектов для манипуляций с состоянием


    ContextState(Context context) {
        this.gameEngine = context.getCurrentGame();
        /*this.screen = new Screen(...);*/
        idCounter = 0;
    }

    abstract public void constructState(ContextState state);



    public final Screen getScreen() {
        return screen;
    }

    public final DataBase getDataBase() {
        return dataBase;
    }

    public final Input getInput() {
        return input;
    }

    public final SoundSystem getSoundSystem() {
        return soundSystem;
    }

    public final long getIdCounter() {
        return idCounter;
    }

    public final GameEngine getCurrentGame() {
        return gameEngine;
    }

    public final String getName() {
        return name;
    }
}
