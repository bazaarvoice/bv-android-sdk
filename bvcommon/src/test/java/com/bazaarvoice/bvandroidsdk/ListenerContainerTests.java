package com.bazaarvoice.bvandroidsdk;

import com.bazaarvoice.bvandroidsdk.internal.ListenerContainer;

import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ListenerContainerTests {

    @Test
    public void shouldAddListener() {
        ListenerContainer<Object> listenerContainer = new ListenerContainer<>();
        Object listener = new Object();
        listenerContainer.add(listener);
        assertTrue(listenerContainer.contains(listener));
        assertEquals(1, listenerContainer.getListeners().size());
    }

    @Test
    public void shouldRemoveListener() {
        ListenerContainer<Object> listenerContainer = new ListenerContainer<>();
        Object listener = new Object();
        listenerContainer.add(listener);
        assertTrue(listenerContainer.contains(listener));
        listenerContainer.remove(listener);
        assertFalse(listenerContainer.contains(listener));
    }

    @Test
    public void shouldRemoveAllListeners() {
        ListenerContainer<Object> listenerContainer = new ListenerContainer<>();
        Object listener = new Object();
        listenerContainer.add(listener);
        assertTrue(listenerContainer.contains(listener));
        listenerContainer.removeAll();
        assertFalse(listenerContainer.contains(listener));
    }
}
