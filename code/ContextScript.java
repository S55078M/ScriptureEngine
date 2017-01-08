package com.gamedev.dreamteam.scriptureengine;

/**
 * Абстрактный класс для оброботки и изменения состояния ContextState, через объекты типа Scripts.
 * Для реализации необходимо переопределить при наследовании метод constructScripts(), назначающий
 * поведение класса наследника.
 */
// TODO: GameEngine
// TODO: Подобрать коллекцию для Script, в соответствии с чем сделать addScript
// TODO: Вариант с заданием поведения через ContextScript у объектов
abstract public class ContextScript implements Runnable {
    private GameEngine gameEngine;  // Ссылка на действующую Активность
    private Context context;        // Ссылка на контекст, задаваемый в конструкторе
    private Script[] scripts;       // Массив скриптов, исполняющихся при работе context
    protected String threadName;    // Имя потока
    private Thread thread;          // Объект потока
    protected int priority;         // Приоритет исполняемого потока
    private boolean run;            // Переменная, отвечающая за текущую обработку потока


    /**
     * Конструктор экземпляра класса
     * @param context - Контекст обработчик, от которого берется состояние ContextState
     */
    ContextScript(Context context) {
        gameEngine = context.getCurrentGame();
        this.context = context;
        run = false;
    }


    /**
     * Данный метод производит конструирование скриптов, через метод addScript. Так же в данном методе задаются поля
     * threadName и priority.
     * Необходим для реализации поведения в процессе выполнения
     */
    abstract public void constructScripts();
    /*
        Пример определения:
        priority = 1;
        threadName = "Thread";
        addScript(new Script(predicate, action));
     */


    /**
     * Запускает поток. Метод должен вызываться после того, как данный объект уже был сконструирован
     * (constructScripts())
     */
    public final void start(){
        thread = new Thread(this, threadName);
        run = true;
        thread.setPriority(priority);
        thread.start();
    }

    /**
     * Останавливает выполнение потока
     * @return  значение типа boolean, показывающая, что поток завершился стабильно
     */
    public final boolean stop(){
        run = false;
        try {
            thread.join();
        }
        catch(InterruptedException e){
            return false;
        }
        return true;
    }

    /**
     * Позволяет заново запустить поток, после выхода из него
     */
    public final void resume(){
        run = true;
        thread.setPriority(priority);
        thread.start();
    }


    /**
     * Метод, накладываемый интерфейсом Runnable, и опредедляющий поведение во время исполнения потока
     */
    @Override
    public final void run() {
        while (run) {
            for (Script script : scripts){
                script.apply(context.getState());
            }
        }
    }


    /**
     * Добовляет объект типа Script в коллекцию scripts.
     * Данный метод используется в классах наследниках, для конструирования поведения ContextScript,
     * через метод constructScripts()
     * @param script
     */
    protected final void addScript(Script script){

    }


    /**
     * Определяет состояние потока
     * @return  false, если поток не запущен, и true, если запущен
     */
    public final boolean isRun(){
        return run;
    }
}
