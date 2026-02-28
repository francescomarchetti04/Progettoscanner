package YGOscanner.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EventBus {

    public interface EventListener {
        void onEvent(Object event);
    }

    private static EventBus instance;
    private final Map<Class<?>, Set<EventListener>> listeners = new HashMap<>();

    private EventBus() {}

    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    public synchronized void subscribe(Class<?> eventType, EventListener listener) {
        listeners.computeIfAbsent(eventType, k -> new HashSet<>()).add(listener);
    }

    public synchronized void unsubscribe(Class<?> eventType, EventListener listener) {
        Set<EventListener> set = listeners.get(eventType);
        if (set != null) set.remove(listener);
    }

    public synchronized void post(Object event) {
        Set<EventListener> set = listeners.get(event.getClass());
        if (set != null) {
            for (EventListener listener : set) {
                listener.onEvent(event);
            }
        }
    }
}