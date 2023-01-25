package host.observer;

public interface ISubscriber {
    void Update(String updateMessage);

    void write(String message);
}
