/**
 * A basic queue.
 *
 * @author Jacob Selbo
 * @version 1.0
 * @since 1/12/21
 */
class Queue<T> {
    private val queue: MutableList<T> = mutableListOf()
    val size: Int
        get() {
            return queue.size
        }

    /**
     * Checks if the queue is empty
     *
     * @return A boolean
     */
    fun isEmpty(): Boolean = queue.isEmpty()

    /**
     * Adds a element to the bottom of the queue
     *
     * @param element An element of the given type
     */
    fun enqueue(element: T) {
        queue.add(element)
    }

    /**
     * Removes and returns the first element in the queue
     *
     * @return The first element in the queue
     */
    fun dequeue(): T? {
        return if (isEmpty())
            null
        else
            queue.removeAt(0)
    }

    /**
     * Returns the first element in the queue
     *
     * @return The first element in the queue
     */
    fun peek(): T? {
        return if (isEmpty())
            null
        else
            queue[0]
    }
}