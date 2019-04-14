package model.lang;

import java.util.List;

/**
 * {@link BinarySearchTree}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class BinarySearchTree<T extends Comparable<T>> {
    private Node<T> root = null;

    public BinarySearchTree() {
    }

    public BinarySearchTree(final T seed) {
        this.root = new Node<>(seed);
    }

    public void traverse() {
        if (this.root != null)
            this.root.visit();
    }

    public Node<T> find(final T val) {
        return this.root != null
                ? this.root.find(val)
                : null;
    }

    public void insert(final T val) {
        final Node<T> node = new Node<>(val);

        if (this.root == null) {
            this.root = node;
        } else {
            this.root.insert(node);
        }
    }

    public void insert(final T[] values) {
        for (T t : values) {
            insert(t);
        }
    }

    public void insert(final List<T> values) {
        for (T t : values) {
            insert(t);
        }
    }
}
