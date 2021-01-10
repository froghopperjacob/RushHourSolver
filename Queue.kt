package main

class Queue<T> {
    val queue: MutableList<T> = mutableListOf()
    val size: Int
        get() {
            return queue.size
        }

    fun isEmpty(): Boolean = queue.isEmpty()

    fun enqueue(element: T) {
        queue.add(element)
    }

    fun dequeue(): T? {
        return if (isEmpty())
            null
        else
            queue.removeAt(0)
    }

    fun peek(): T? {
        return if (isEmpty())
            null
        else
            queue[0]
    }
}