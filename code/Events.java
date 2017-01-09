package com.gamedev.dreamteam.scriptureengine;

import java.util.Collection;

/**
 * Статический класс определяющий основные функции для конструирования скриптов.
 */
public final class Events {

    // 1. Обобщенные конструкторы функций:

    /**
     * Конструктор функции, определяющейся последовательным применением двух параметрических функций
     * compose(f2, f1).apply(arg) == f2(f1(arg))
     *
     * @param f1 - Функция, первая модифицирующая аргумент полученной функции на выходе compose
     * @param f2 - функция, которая накладывается на значения во вторую очередь, после функции f1
     * @param <T> - параметр, определяющий возвращающее значение функции f1, и принимающее значениие ф-ии f2
     * @param <M> - параметр, определяющий возвращающее значение всей композиции функций, а так де второй ф-ии (f2)
     * @param <A> - параметр, определяющий принимающее значение всей композиции функций, а так же первой ф-ии (f1)
     * @return  функция, от одного аргумента типа A, возвращающая значение типа M
     */
    public static <T,M,A> Function<A,M> compose(Function<T, M> f2, Function<A, T> f1) {
        return arg -> f2.apply(f1.apply(arg));
    }


    /**
     * Конструктор функции, возвращающей всегда одно и тоже значение, указанное при конструировании
     * constant(a).apply(b) == a, при любом b
     *
     * @param val - значение, которое всегда будет возвращаться, при вызове конструируемой функции
     * @param <A> - тип аргумента конструирующей функции, задающей константное возвращаемое значение
     * @return  функция, от значения типа T, и возвращающая всегда значение val
     */
    public static <A> Function<Object, A> constant(A val) {
        return arg -> val;
    }


    /**
     * Конструктор функции оборачивающей порядок ввода аргументов в объект функции, интерфейса Function
     * f.apply(a).apply(b) == flip(f).apply(b).apply(a)
     *
     * @param f - функция, аргументы которой изменяют порядок ввода
     * @param <A> - тип первого аргумента функции f
     * @param <B> - тип второго аргумента функции f
     * @param <T> - тип возвращаемого значения функции f
     * @return  функция, принимающая значение типа B, далее значение типа A, и возвращающая тип T
     */
    public static <A,B,T> Function<B,Function<A,T>> flip(Function<A,Function<B,T>> f) {
        return arg2 -> (Function<A, T>) arg1 -> f.apply(arg1).apply(arg2);
    }


    /**
     * Функция, конструирующая функциональный объект, через наложение функции (operator) на послеодвательность ф-ий
     * (Композиция функциональной свертки)
     * sum.apply(sum.apply(a.apply(arg)).apply(b.apply(arg))).apply(c.apply(arg)) == on(sum, a, b, c).apply(arg)
     * sum.apply(val1).apply(val2) == on(sum, constant(val1), constant(val2)).apply(null)
     *
     * @param operator - бинарная функция, типа Function<B, Function<B, B>>, сворачивающая последовательность ф-ий
     * @param fs - последовательность функций, от одного аргумента
     * @param <A> - тип аргумента всех функций последовательности fs
     * @param <B> - тип возвращающегося значения функций fs, operator, и принимаемого для operator
     * @return  свертка функцией operator последовательности функций fs, с ленивым подставлением аргумента
     */
    @SafeVarargs
    public static <A, B> Function<A, B> on(Function<B, Function<B, B>> operator, Function<A, B>... fs){
        return arg -> {
            B accum = operator.apply(fs[0].apply(arg)).apply(fs[1].apply(arg));
            for (int i = 2; i < fs.length; ++i){
                accum = operator.apply(fs[i].apply(arg)).apply(accum);
            }
            return accum;
        };
    }






    // 2. Конструкторы предикатов:

    /**
     * Конструктор предиката, возвращающий отрицание предиката, переданного в качестве аргумента
     * not(always) == never
     *
     * @param predicate - функция (предикат), от которой строится отрицание (Predicate)
     * @return  Предикат  (Predicate)
     */
    public static Predicate not(Predicate predicate){
        return state -> !predicate.apply(state);
    }


    /**
     * Конструктор предиката, строящий его сочитая два предиката через оператор `and` (&&)
     * new Script(p1, new Script(p2, action)) == new Script(and(p1, p2), action)
     *
     * @param p1 - первый предикат, выступающий в качестве операнда (Predicate)
     * @param p2 - второй предикат, выступающий в качестве операнда (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          p1.apply(state) && p2.apply(state)
     */
    public static Predicate and(Predicate p1, Predicate p2){
        return state -> p1.apply(state) && p2.apply(state);
    }

    /**
     * Конструктор предиката, строящий его сочитая несколько предикатов через оператор `and` (&&)
     * (Аналог and для двух аргументов, разширяющий на списко некольктх аргументов)
     * and(p1,p2,p3).apply(state) == p1.apply(state) && p2.apply(state) && p3.apply(state)
     *
     * @param predicates - последовательность предикатов (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          predicates[0].apply(state) && predicates[1].apply(state) && predicates[2].apply(state) ...
     */
    public static Predicate and(Predicate... predicates){
        return state -> {
            for (Predicate p_current : predicates)
                if (!p_current.apply(state))
                    return false;
            return true;
        };
    }


    /**
     * Конструктор предиката, строящий его сочитая два предиката через оператор `or` (||)
     * new Script(p1, action); new Script(p2, action) ==> new Script(or(p1, p2), action)
     *
     * @param p1 - первый предикат, выступающий в качестве операнда (Predicate)
     * @param p2 - второй предикат, выступающий в качестве операнда (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          p1.apply(state) || p2.apply(state)
     */
    public static Predicate or(Predicate p1, Predicate p2){
        return state -> p1.apply(state) || p2.apply(state);
    }

    /**
     * Конструктор предиката, строящий его сочитая несколько предикатов через оператор `or` (||)
     * (Аналог or для двух аргументов, разширяющий на списко некольктх аргументов)
     * or(p1,p2,p3).apply(state) == p1.apply(state) || p2.apply(state) || p3.apply(state)
     *
     * @param predicates - последовательность предикатов (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          predicates[0].apply(state) || predicates[1].apply(state) || predicates[2].apply(state) ...
     */
    public static Predicate or(Predicate... predicates){
        return state -> {
            for (Predicate p_current : predicates)
                if (p_current.apply(state))
                    return true;
            return false;
        };
    }


    /**
     * Конструктор предиката, из данного, накладывающий условие, что он выполнится один раз тогда и только тогда,
     * когда значение предиката, передаваемого в качестве аргумента изменится с false на true !
     * (В начале испольнения считается что значение предиката до момента начала было равно false)
     * once(always) == onStart
     *
     * @param predicate - предикат (Predicate)
     * @return  предикат (Predicate)
     */
    public static Predicate once(Predicate predicate){
        return new Predicate() {

            boolean predicate_is_true = false;

            @Override
            public Boolean apply(ContextState state) {
                if (!predicate_is_true && predicate.apply(state)){
                    predicate_is_true = true;
                    return true;
                }
                predicate_is_true = false;
                return false;
            }
        };
    }

    /**
     * Конструктор предиката, выозвращающего всегда true, если предикат передоваемый в качестве аргумента
     * вернул хотя бы один раз true
     * foreverCond(onStart) == always
     *
     * @param predicate - предикат (Predicate)
     * @return  предикат (Predicate)
     */
    public static Predicate foreverCond(Predicate predicate){
        return new Predicate() {

            boolean predicate_is_true = false;

            @Override
            public Boolean apply(ContextState state) {
                if (predicate_is_true || predicate.apply(state)){
                    predicate_is_true = true;
                    return true;
                }
                return false;
            }
        };
    }


    /**
     * Конструктор предиката, сравнивающий два значения через бинарный предикат
     * compare(val1, val2, less())
     *
     * @param arg1  - первое сравниваемое значение типа T
     * @param arg2  - второе сравниваемое значение типа T
     * @param f - бинарный предикат, через который производится сравнивание
     * @param <T> - тип значений для сравнивания
     * @return  предикат (Predicate)
     */
    public static <T extends Comparable<T>> Predicate compare(T arg1,
                                                              T arg2,
                                                              Function<T, Function<T, Boolean>> f){
        return state -> f.apply(arg1).apply(arg2);
    }

    /**
     * Конструктор предиката, сравнивающий значение и значение, возвращаемое переданной ф-ей, через бинарный предикат
     * compare(val1, getX(id), equal())
     *
     * @param arg1    - первое сравниваемое значение типа T
     * @param f_arg2  - функция, возвращающая сравниваемое значение
     * @param f     - бинарный предикат, через который производится сравнивание
     * @param <T> - тип значений для сравнивания
     * @return  предикат (Predicate)
     */
    public static <T extends Comparable<T>> Predicate compare(T arg1,
                                                              Function<ContextState,T> f_arg2,
                                                              Function<T,Function<T, Boolean>> f){
        return state -> f.apply(arg1).apply(f_arg2.apply(state));
    }

    /**
     * Конструктор предиката, сравнивающий значения, возвращаемые переданными ф-ями, через бинарный предикат
     * compare(getX(id1), getX(id2), greater())
     *
     * @param f_arg1  - первая функция, возвращающая сравниваемое значение
     * @param f_arg2  - вторая функция, возвращающая сравниваемое значение
     * @param f     - бинарный предикат, через который производится сравнивание
     * @param <T> - тип значений, возвращаемый функциями, для сравнивания
     * @return  предикат (Predicate)
     */
    public static <T extends Comparable<T>> Predicate compare(Function<ContextState,T> f_arg1,
                                                              Function<ContextState,T> f_arg2,
                                                              Function<T,Function<T, Boolean>> f){
        return state -> f.apply(f_arg1.apply(state)).apply(f_arg2.apply(state));
    }






    // 3. Конструкторы бинарных предикатов (для сравнения):

    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на меньше
     * (t1 < t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 < val2)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> less() {
        return t1 -> (Function<T, Boolean>) t2 -> t1.compareTo(t2) < 0;
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на меньше или равно
     * (t1 <= t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 <= val2)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> less_or_equal() {
        return t1 -> (Function<T, Boolean>) t2 -> t1.compareTo(t2) <= 0;
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на равенство
     * (t1 == t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 == val2)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> equal() {
        return t1 -> (Function<T, Boolean>) t2 -> t1.compareTo(t2) == 0;
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на неравенство
     * (t1 != t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 != val2)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> not_equal() {
        return t1 -> (Function<T, Boolean>) t2 -> t1.compareTo(t2) != 0;
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на больше
     * (t1 > t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 > val2)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> greater() {
        return t1 -> (Function<T, Boolean>) t2 -> t1.compareTo(t2) > 0;
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на больше или равно
     * (t1 >= t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 >= val2)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> greater_or_equal() {
        return t1 -> (Function<T, Boolean>) t2 -> t1.compareTo(t2) >= 0;
    }




    // 4. Конструкторы функций, для возвращаемых значений:


    // 5. Основные функции, для получения значений/объектов:


    // 6. Конструкторы скриптов:

    /**
     * Возвращает скрипт, при выполнении которого проверяется возвращаемое значение предикатом,
     * и если оно true - выполняется действие s1, иначе, при false - s2
     * elseScript(p, a1, a2); ==> new Script(p, a1); new Script(not(p), a2);
     *
     * @param p - Предикат, выполняющий улсовие
     * @param s1 - выполняемое событие, если предикат p вернул true
     * @param s2 - выполняемое событие, если предикат p вернул false
     * @return  Объект Script
     */
    public static Script elseScript(Predicate p, Action s1, Action s2){
        return new Script(null, null){

            @Override
            public Void apply(ContextState state){
                if (p.apply(state)){
                    return s1.apply(state);
                }
                return s2.apply(state);
            }
        };
    }
    /*public static Script elseScript(Predicate p, Action s1, Action s2){
        return new Script(always, (Action) state -> {
            if (p.apply(state)){
                return s1.apply(state);
            }
            return s2.apply(state);
        });
    }*/


    /**
     * Выполняет действие s до тех пор, пока возвращаемое значение предиката не вернет false.
     *
     * @param p - предикат
     * @param s - действие
     * @return  Объект Script
     */
    public static Script whileScript(Predicate p, Action s){
        return new Script(null, null){

            @Override
            public Void apply(ContextState state){
                while(p.apply(state)){
                    s.apply(state);
                }
                return null;
            }
        };
    }


    /**
     * Конструирует скрипт, из функции f, принимающей целое число и возвращающей объект Action, таким образом, что
     * данная функция f, "проходит" по всем значениям от 0 до range, и исполняет все сконструированные действия
     * forScript(10, flip(constant).apply(a)) - повторит действие a 10 раз
     *
     * @param range - максимальное значение до которого будет генерироваться область для функци f
     * @param f - функция, "проходящая" по всем значениям из области данных чисел, и возвращающая объекты типа Action
     * @return  Объект Script
     */
    public static Script forScript(Integer range, Function<Integer, Action> f){
        return new Script(null, null){

            @Override
            public Void apply(ContextState state){
                for (int i = 0; i < range; ++i){
                    f.apply(i).apply(state);
                }
                return null;
            }
        };
    }

    /**
     * Конструирует скрипт, из функции f, принимающей объект типа T и возвращающей объект Action, таким образом, что
     * данная функция f, "проходит" по всем значениям в заданной коллекции и исполняет все сконструированные действия
     *
     * @param arr - коллекция, определяющая область применения функции f
     * @param f - функция, "проходящая" по всем значениям из коллекции, и возвращающая объекты типа Action
     * @param <T> - тип, определяющий тип коллекции
     * @return  Объект Script
     */
    public static <T> Script forScript(Collection<T> arr, Function<T, Action> f){
        return new Script(null, null){

            @Override
            public Void apply(ContextState state){
                for (T var : arr){
                    f.apply(var).apply(state);
                }
                return null;
            }
        };
    }


    /**
     * Запускает исполнение скрипта в отдельном потоке
     *
     * @param threadName - имя созданного потока
     * @param priority - приоритет созданного потока
     * @param script - объект типа Script, исполняемый в потоке один раз, после чего поток перестает выполняться
     * @return  Объект типа Script
     */
    public static Script threadScript(String threadName, int priority, Script script){
        return new Script(null, null){

            private Thread thread;

            @Override
            public Void apply(ContextState state){
                thread = new Thread(threadName){

                    @Override
                    public void run() {
                        script.apply(state);
                    }
                };

                thread.setPriority(priority);
                thread.start();
                return null;
            }
        };
    }




    // 7. Модификаторы событий (конструкторы событий):


    // 8. Основные предикаты:

    /**
     * Предикат, возвращающий всегда true
     */
    public static Predicate always = state -> true;




    // 9. Основные события:

    /**
     * Событие, не производящее никаких действий
     */
    public static Action nothing = state -> null;

}
