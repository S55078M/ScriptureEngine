package com.gamedev.dreamteam.scriptureengine;

/**
 * Абстрактный класс для отображения объектов в ContextState, а так же, возможно, и других состояний системы.
 */

// TODO: GameEngine
// TODO: 3 основных метода, после завершения класса GraphicalEngine
abstract public class ContextRenderer implements Renderer {
    protected Context context;                      // Ссылка на обрабатываемый контекст
    private GameEngine gameEngine;                  // Ссылка на действующую Активность
    protected GraphicalEngine graphicalEngine;      // Ссылка на объект, отвечающий за рендер графики
    protected Screen screen;                        // Ссылка на объкт, отвечающий за положение экрана


    /**
     * Конструктор экземпляра класса
     * @param context - Контекст обработчик, от которого берется состояние ContextState
     */
    ContextRenderer(Context context) {
        this.context = context;
        this.gameEngine = context.getCurrentGame();
        this.screen = context.getState().getScreen();
    }


    /**
     *
     * @param gl
     */
    @Override
    public void onDrawFrame(GL10 gl){

    }

    /**
     *
     * @param gl
     * @param width
     * @param heigth
     */
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int heigth){

    }

    /**
     *
     * @param gl
     * @param cfg
     */
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig cfg){

    }
}
