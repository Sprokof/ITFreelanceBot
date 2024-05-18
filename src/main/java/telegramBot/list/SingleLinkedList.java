package telegramBot.list;

public class SingleLinkedList<Element>
{
    private Node<Element> head;
    private Node<Element> tail;
    private int size = 0;
    private static class Node<Element> {
        private final Element element;
        private Node<Element> next;

        Node(Element element, Node<Element> next) {
            this.element = element;
            this.next = next;
        }
    }

    public void add(Element element) {
        Node<Element> newNode = new Node<>(element, null);
        if (head == null) {
            head = newNode;
        } else {
             tail.next = newNode;
        }
        tail = newNode;
        size ++ ;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Element last() {
        return tail.element;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public int size() {
        return size;
    }
}
