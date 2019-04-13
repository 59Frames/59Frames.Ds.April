package model.lang;

import java.util.Comparator;

/**
 * {@link Node}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Node<T extends Comparable<T>> {

    private static final int LEFT = -0x1;
    private static final int RIGHT = 0x1;

    private final T value;

    private Node<T> left;
    private Node<T> right;

    public Node(T value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public void visit() {
        if (this.left != null) this.left.visit();
        System.out.println(this.value);
        if (this.right != null) this.right.visit();
    }

    public Node<T> find(final T val) {
        if (this.value.equals(val))
            return this;

        Node<T> n = null;
        if (this.value.compareTo(val) > 0) {
            if (this.left != null)
                n = this.left.find(val);
        } else {
            if (this.right != null)
                n = this.right.find(val);
        }

        return n;
    }

    public T getValue() {
        return this.value;
    }

    public void insert(Node<T> node) {
        final int result = node.value.compareTo(this.value);

        if (result == 0) return;

        checkAndOrInsert(result < 0 ? LEFT : RIGHT, node);
    }

    private void checkAndOrInsert(int pos, Node<T> node) {
        switch (pos) {
            case LEFT: {
                if (this.left == null) this.left = node;
                else this.left.insert(node);
                break;
            }
            case RIGHT: {
                if (this.right == null) this.right = node;
                else this.right.insert(node);
                break;
            }
        }
    }
}
