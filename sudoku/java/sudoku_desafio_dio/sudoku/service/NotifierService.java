package sudoku_desafio_dio.sudoku.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sudoku_desafio_dio.sudoku.service.EventEnum.CLEAR_SPACE;

public class NotifierService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<EventEnum, List<EventListener>> listeners = new HashMap<>();

    public NotifierService() {
        listeners.put(CLEAR_SPACE, new ArrayList<>());
    }

    public void subscribe(final EventEnum eventType, EventListener listener){
        var selectedListeners = listeners.get(eventType);
        selectedListeners.add(listener);
    }

    public void notify(final EventEnum eventType){
        listeners.get(eventType).forEach(l -> l.update(eventType));
    }

}
