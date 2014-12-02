package data;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by ben on 11/25/14.
 */
public class UniqueList<T> extends ArrayList<T> {

    @Override
    public boolean add(T object) {
        if (!contains(object))
            return super.add(object);
        return false;
    }

    @Override
    public void add(int index, T object) {
        if (!contains(object))
            super.add(index, object);
    }

    @Override
    public boolean addAll(Collection<? extends T> collection) {
        boolean b = false;
        for (T t : collection)
            b = b || add(t);
        return b;
    }

    protected boolean addBase(T obj) {
        return super.add(obj);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> collection) {
        if (containsAll(collection))
            return super.addAll(index, collection);
        return false;
    }
}
