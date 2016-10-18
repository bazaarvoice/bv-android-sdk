/**
 * Copyright 2016 Bazaarvoice Inc. All rights reserved.
 */
package com.bazaarvoice.bvandroidsdk.internal;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * Container for a group of listeners that should be added and removed, but
 * will also proactively guard against users failing to call remove.
 *
 * @param <ListenerType> The listener class to contain WeakReferences to.
 */
public final class ListenerContainer<ListenerType> {
    Map<ListenerType, WeakReference<ListenerType>> listenerMap;

    public ListenerContainer() {
        listenerMap = new WeakHashMap<>();
    }

    public void add(ListenerType listener) {
        listenerMap.put(listener, new WeakReference<ListenerType>(listener));
    }

    public void remove(ListenerType listener) {
        if (contains(listener)) {
            listenerMap.remove(listener);
        }
    }

    public boolean contains(ListenerType listener) {
        return listenerMap.containsKey(listener);
    }

    public Set<ListenerType> getListeners() {
        return listenerMap.keySet();
    }
}
