package client.memento;

import java.util.ArrayList;
import java.util.List;

public class History {

    private final List<Memento> mementoList = new ArrayList<>();

    public void addMemento(Memento memento) {
        System.out.println(memento.getState());
        mementoList.add(memento);
    }

    public Memento getMemento(int index) {
        return mementoList.get(index);
    }

    public int getListSize() {
        return mementoList.size();
    }
}
