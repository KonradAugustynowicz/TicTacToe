package memento;

import java.util.ArrayList;
import java.util.List;

public class History {

    private final List<Memento> mementoList = new ArrayList<>();

    public void addMemento(Memento memento) {
        mementoList.add(memento);
    }

    public Memento getMemento(int index) {
        return mementoList.get(index);
    }

    public int getListSize() {
        return mementoList.size();
    }
}
