package com.gamedev.dreamteam.scriptureengine;

/**
 * Статический класс определяющий основные функции для конструирования скриптов.
 */
public class ContainerScripts {

    // 1. Обобщенные конструкторы функций:

    /**
     * Конструктор функции, определяющейся последовательным применением двух параметрических функций
     * compose(f2, f1).apply(arg) == f2(f1(arg))        (пример 1)
     *
     * @param f1 - Функция, первая модифицирующая аргумент полученной функции на выходе compose
     * @param f2 - функция, которая накладывается на значения во вторую очередь, после функции f1
     * @param <T> - параметр, определяющий возвращающее значение функции f1, и принимающее значениие ф-ии f2
     * @param <M> - параметр, определяющий возвращающее значение всей композиции функций, а так де второй ф-ии (f2)
     * @param <A> - параметр, определяющий принимающее значение всей композиции функций, а так же первой ф-ии (f1)
     * @return  функция, от одного аргумента типа A, возвращающая значение типа M
     */
    public static <T,M,A> Function<A,M> compose(Function<T, M> f2, Function<A, T> f1) {
        return new Function<A,M>() {
            @Override
            public M apply (A arg) {
                return f2.apply(f1.apply(arg));
            }
        };
    }


    /**
     * Конструктор функции, возвращающей всегда одно и тоже значение, указанное при конструировании
     * constant(a).apply(b) == a, при любом b        (пример 2)
     *
     * @param val - значение, которое всегда будет возвращаться, при вызове конструируемой функции
     * @param <A> - тип аргумента конструирующей функции, задающей константное возвращаемое значение
     * @param <T> - тип вариабельного аргумента, который определяет конструируемая функция
     * @return  функция, от значения типа T, и возвращающая всегда значение val
     */
    public static <A,T> Function<T,A> constant(A val) {
        return new Function<T,A>() {
            @Override
            public A apply (T arg) {
                return val;
            }
        };
    }


    /**
     * Конструктор функции оборачивающей порядок ввода аргументов в объект функции, интерфейса Function
     * f.apply(a).apply(b) == flip(f).apply(b).apply(a)        (пример 3)
     *
     * @param f - функция, аргументы которой изменяют порядок ввода
     * @param <A> - тип первого аргумента функции f
     * @param <B> - тип второго аргумента функции f
     * @param <T> - тип возвращаемого значения функции f
     * @return  функция, принимающая значение типа B, далее значение типа A, и возвращающая тип T
     */
    public static <A,B,T> Function<B,Function<A,T>> flip(Function<A,Function<B,T>> f) {
        return new Function<B,Function<A,T>>() {
            @Override
            public Function<A,T> apply (B arg2) {
                return new Function<A,T>() {
                    @Override
                    public T apply (A arg1) {
                        return f.apply(arg1).apply(arg2);
                    }
                };
            }
        };
    }


    /**
     * Функция, конструирующая функциональный объект, реализующий применения функций
     * applicate().apply(f).apply(a) == f.apply(a)        (пример 4)
     *
     * @param <A> - параметр аргумента функции аргумента
     * @param <B> - возвращаемый параметр функции аргумента
     * @return  функция, принимающая функциональный объект в качестве аргумента, и возвращающая его же
     */
    public static <A, B> Function<Function<A, B>, Function<A, B>> applicate() {
        return new Function<Function<A, B>, Function<A, B>>() {
            @Override
            public Function<A, B> apply(Function<A, B> f) {
                return new Function<A, B>() {
                    @Override
                    public B apply(A arg) {
                        return f.apply(arg);
                    }
                };
            }
        };
    }


    /**
     * Функция, конструирующая функциональный объект, через наложение функции (operator) на послеодвательность ф-ий
     * (Композиция функциональной свертки)
     * sum.apply(sum.apply(a.apply(arg)).apply(b.apply(arg))).apply(c.apply(arg)) == on(sum, a, b, c).apply(arg)
     * sum.apply(val1).apply(val2) == on(sum, constant(val1), constant(val2)).apply(null)
     *                                                                                              (пример 5)
     *
     * @param operator - бинарная функция, типа Function<B, Function<B, B>>, сворачивающая последовательность ф-ий
     * @param fs - последовательность функций, от одного аргумента
     * @param <A> - тип аргумента всех функций последовательности fs
     * @param <B> - тип возвращающегося значения функций fs, operator, и принимаемого для operator
     * @return  свертка функцией operator последовательности функций fs, с ленивым подставлением аргумента
     */
    @SafeVarargs
    public static <A, B> Function<A, B> on(Function<B, Function<B, B>> operator, Function<A, B>... fs){
        return new Function<A, B>() {
            @Override
            public B apply(A arg) {
                B accum = operator.apply(fs[0].apply(arg)).apply(fs[1].apply(arg));
                for (int i = 2; i < fs.length; ++i){
                    accum = operator.apply(fs[i].apply(arg)).apply(accum);
                }
                return accum;
            }
        };
    }







    // 2. Конструкторы предикатов:

    /**
     * Конструктор предиката, возвращающий отрицание предиката, переданного в качестве аргумента
     * not(always) == never        (пример 6)
     *
     * @param predicate - функция (предикат), от которой строится отрицание (Predicate)
     * @return  Предикат  (Predicate)
     */
    public static Predicate not(Predicate predicate){
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                return !predicate.apply(state);
            }
        };
    }


    /**
     * Конструктор предиката, строящий его сочитая два предиката через оператор `and` (&&)
     * new Script(p1, new Script(p2, action)) == new Script(and(p1, p2), action)        (пример 7)
     *
     * @param p1 - первый предикат, выступающий в качестве операнда (Predicate)
     * @param p2 - второй предикат, выступающий в качестве операнда (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          p1.apply(state) && p2.apply(state)
     */
    public static Predicate and(Predicate p1, Predicate p2){
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                return p1.apply(state) && p2.apply(state);
            }
        };
    }

    /**
     * Конструктор предиката, строящий его сочитая несколько предикатов через оператор `and` (&&)
     * (Аналог and для двух аргументов, разширяющий на списко некольктх аргументов)
     * and(p1,p2,p3).apply(state) == p1.apply(state) && p2.apply(state) && p3.apply(state)       (пример 8)
     *
     * @param predicates - последовательность предикатов (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          predicates[0].apply(state) && predicates[1].apply(state) && predicates[2].apply(state) ...
     */
    public static Predicate and(Predicate... predicates){
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                for (Predicate p_current : predicates)
                    if (!p_current.apply(state))
                        return false;
                return true;
            }
        };
    }


    /**
     * Конструктор предиката, строящий его сочитая два предиката через оператор `or` (||)
     * new Script(p1, action); new Script(p2, action) ==> new Script(or(p1, p2), action)        (пример 9)
     *
     * @param p1 - первый предикат, выступающий в качестве операнда (Predicate)
     * @param p2 - второй предикат, выступающий в качестве операнда (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          p1.apply(state) || p2.apply(state)
     */
    public static Predicate or(Predicate p1, Predicate p2){
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                return p1.apply(state) || p2.apply(state);
            }
        };
    }

    /**
     * Конструктор предиката, строящий его сочитая несколько предикатов через оператор `or` (||)
     * (Аналог or для двух аргументов, разширяющий на списко некольктх аргументов)
     * or(p1,p2,p3).apply(state) == p1.apply(state) || p2.apply(state) || p3.apply(state)       (пример 10)
     *
     * @param predicates - последовательность предикатов (Predicate)
     * @return  функция (предикат), прнимающая значение state типа ContextState, и возвращающая
     *          predicates[0].apply(state) || predicates[1].apply(state) || predicates[2].apply(state) ...
     */
    public static Predicate or(Predicate... predicates){
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                for (Predicate p_current : predicates)
                    if (p_current.apply(state))
                        return true;
                return false;
            }
        };
    }


    /**
     * Конструктор предиката, из данного, накладывающий условие, что он выполнится один раз тогда и только тогда,
     * когда значение предиката, передаваемого в качестве аргумента изменится с false на true !
     * (В начале испольнения считается что значение предиката до момента начала было равно false)
     * once(always) == onStart       (пример 11)
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
     * foreverCond(onStart) == always       (пример 12)
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
     * compare(val1, val2, less())       (пример 13)
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
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                return f.apply(arg1).apply(arg2);
            }
        };
    }

    /**
     * Конструктор предиката, сравнивающий значение и значение, возвращаемое переданной ф-ей, через бинарный предикат
     * compare(val1, getX(id), equal())       (пример 14)
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
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                return f.apply(arg1).apply(f_arg2.apply(state));
            }
        };
    }

    /**
     * Конструктор предиката, сравнивающий значения, возвращаемые переданными ф-ями, через бинарный предикат
     * compare(getX(id1), getX(id2), greater())       (пример 15)
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
        return new Predicate() {
            @Override
            public Boolean apply(ContextState state) {
                return f.apply(f_arg1.apply(state)).apply(f_arg2.apply(state));
            }
        };
    }







    // 3. Конструкторы бинарных предикатов (для сравнения):

    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на меньше
     * (t1 < t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 < val2)       (пример 16)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> less() {
        return new Function<T, Function<T, Boolean>>() {
            @Override
            public Function<T, Boolean> apply(T t1) {
                return new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t2) {
                        return t1.compareTo(t2) < 0;
                    }
                };
            }
        };
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на меньше или равно
     * (t1 <= t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 <= val2)       (пример 17)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> less_or_equal() {
        return new Function<T, Function<T, Boolean>>() {
            @Override
            public Function<T, Boolean> apply(T t1) {
                return new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t2) {
                        return t1.compareTo(t2) <= 0;
                    }
                };
            }
        };
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на равенство
     * (t1 == t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 == val2)       (пример 18)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> equal() {
        return new Function<T, Function<T, Boolean>>() {
            @Override
            public Function<T, Boolean> apply(T t1) {
                return new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t2) {
                        return t1.compareTo(t2) == 0;
                    }
                };
            }
        };
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на неравенство
     * (t1 != t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 != val2)       (пример 19)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> not_equal() {
        return new Function<T, Function<T, Boolean>>() {
            @Override
            public Function<T, Boolean> apply(T t1) {
                return new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t2) {
                        return t1.compareTo(t2) != 0;
                    }
                };
            }
        };
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на больше
     * (t1 > t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 > val2)       (пример 20)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> greater() {
        return new Function<T, Function<T, Boolean>>() {
            @Override
            public Function<T, Boolean> apply(T t1) {
                return new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t2) {
                        return t1.compareTo(t2) > 0;
                    }
                };
            }
        };
    }


    /**
     * Возвращает функциональный объект - бинарный предикат для сравнения на больше или равно
     * (t1 >= t2, где t1, t2 - значения вводимые последовательно в полученную функцию через apply())
     * less().apply(val1).apply(val2) == (val1 >= val2)       (пример 21)
     *
     * @param <T> - параметр, определенный интерфейсом Comparable<T>; тип вводимых переменных
     * @return  бинарный предикат, от параметра типа T
     */
    public static <T extends Comparable<T>> Function<T, Function<T, Boolean>> greater_or_equal() {
        return new Function<T, Function<T, Boolean>>() {
            @Override
            public Function<T, Boolean> apply(T t1) {
                return new Function<T, Boolean>() {
                    @Override
                    public Boolean apply(T t2) {
                        return t1.compareTo(t2) >= 0;
                    }
                };
            }
        };
    }







    // 4. Конструкторы функций, для возвращаемых значений:


    // 5. Основные функции, для получения значений/объектов:


    // 6. Конструкторы скриптов:


    // 7. Модификаторы событий (контрукторы событий):


    // 8. Основные предикаты:


    // 9. Основные события:

}
