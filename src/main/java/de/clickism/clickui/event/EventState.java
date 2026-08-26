package de.clickism.clickui.event;

/**
 * Represents the state of an event, allowing it to be marked as consumed to prevent further propagation.
 */
public class EventState {
    private boolean consumed = false;

    /**
     * Marks the event as consumed, preventing further propagation to other listeners.
     */
    public void consume() {
        this.consumed = true;
    }

    /**
     * Checks if the event has been consumed.
     *
     * @return true if the event has been consumed, false otherwise.
     */
    public boolean consumed() {
        return consumed;
    }
}
