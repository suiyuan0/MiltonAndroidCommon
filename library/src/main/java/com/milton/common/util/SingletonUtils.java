package com.milton.common.util;

/**
 * Singleton helper class for lazily initialization.
 *
 * @param <T> 泛型
 * @author <a href="http://www.trinea.cn/" target="_blank">Trinea</a>
 */
public abstract class SingletonUtils<T> {

    private T instance;

    protected abstract T newInstance();

    public final T getInstance() {
        if (instance == null) {
            synchronized (SingletonUtils.class) {
                if (instance == null) {
                    instance = newInstance();
                }
            }
        }
        return instance;
    }
}
