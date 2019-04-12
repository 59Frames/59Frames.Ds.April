package model.lang;

/**
 * {@link Node}
 *
 * @author Daniel Seifert
 * @version 1.0
 * @since 1.0
 */
public class Node<T extends Comparable<T>> {
    private final T data;

    private Node<T> left;
    private Node<T> right;

    public Node(T data) {
        this.data = data;
        this.left = null;
        this.right = null;
    }

    public void visit() {
        if (this.left != null) this.left.visit();
        System.out.println(this.data);
        if (this.right != null) this.right.visit();
    }

    public Node<T> find(final T val) {
        if (this.data == val)
            return this;

        Node<T> n = null;
        if (this.data.compareTo(val) > 0) {
            if (this.left != null)
                n = this.left.find(val);
        } else {
            if (this.right != null)
                n = this.right.find(val);
        }

        return n;
    }

    public void insert(Node<T> node) {
        final int result = node.data.compareTo(this.data);
        if (result < 0) {
            if (this.left == null) this.left = node;
            else this.left.insert(node);
        } else if (result > 0) {
            if (this.right == null) this.right = node;
            else this.right.insert(node);
        }
    }
}
