import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;

/**
 * Your implementation of a binary search tree.
 *
 * @author Carson Duerr
 * @userid cduerr3
 * @GTID 903186923
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the BST with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        for (T curr: data) {
            if (curr == null) {
                throw new IllegalArgumentException("Data cannot be null");
            }
            add(curr);
        }
    }

    /**
     * Add the data as a leaf in the BST. Should traverse the tree to find the
     * appropriate location. If the data is already in the tree, then nothing
     * should be done (the duplicate shouldn't get added, and size should not be
     * incremented).
     * 
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        root = rAdd(data, root);
    }

    /**
     * Recursive helper method for add
     *
     * @param data the data to be added
     * @param node the node location being checked
     * @return current BST node
     */
    private BSTNode<T> rAdd(T data, BSTNode<T> node) {
        if (node == null) {
            ++size;
            return new BSTNode<T>(data);
        } else if (node.getData().compareTo(data) == 0) {
            return node;
        } else {
            if (node.getData().compareTo(data) > 0) {
                node.setLeft(rAdd(data, node.getLeft()));
                return node;
            } else {
                node.setRight(rAdd(data, node.getRight()));
                return node;
            }
        }
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf (no children). In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the predecessor to replace the data.
     * You MUST use recursion to find and remove the predecessor (you will
     * likely need an additional helper method to handle this case efficiently).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in. Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        BSTNode<T> n = new BSTNode<T>(null);
        root = rRemove(data, root, n);
        --size;
        return n.getData();
    }

    /**
     * Recursive helper method for the remove method
     *
     * @throws java.util.NoSuchElementException if the data is not in the tree
     * @param data the data that is to be removed
     * @param node current node being looked at
     * @param x node used to store removed data
     * @return current node being looked at
     */
    private BSTNode<T> rRemove(T data, BSTNode<T> node, BSTNode<T> x) {
        if (node == null) {
            throw new java.util.NoSuchElementException("This data is not in"
                    + " the structure");
        }
        if (node.getData().compareTo(data) == 0) {
            x.setData(node.getData());
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            } else if (node.getLeft() != null && node.getRight() == null) {
                return node.getLeft();
            } else if (node.getLeft() == null && node.getRight() != null) {
                return node.getRight();
            } else {
                BSTNode<T> temp = new BSTNode<T>(null);
                node.setRight(findSuccessor(node.getRight(), temp));
                node.setData(temp.getData());
            }
        } else {
            if (node.getData().compareTo(data) > 0) {
                node.setLeft(rRemove(data, node.getLeft(), x));
            } else {
                node.setRight(rRemove(data, node.getRight(), x));
            }
        }
        return node;
    }

    /**
     * Recursive helper that finds the successor of a given node
     *
     * @param node current node whose successor is being looked for
     * @param temp node used to store successor data
     * @return node to be removed
     */
    private BSTNode<T> findSuccessor(BSTNode<T> node, BSTNode<T> temp) {
        if (node.getLeft() == null) {
            temp.setData(node.getData());
            return node.getRight();
        } else {
            node.setLeft(findSuccessor(node.getLeft(), temp));
        }
        return node;
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        if (size == 0) {
            throw new NoSuchElementException("The data structure is empty");
        }
        return rGet(data, root);
    }

    /**
     * Recursive helper method for the get method
     *
     * @param data the data being searched for
     * @param node current node being looked at
     * @return the data that was searched for
     */
    private T rGet(T data, BSTNode<T> node) {
        if (node == null) {
            throw new NoSuchElementException("Data was not found");
        } else if (node.getData().compareTo(data) == 0) {
            return node.getData();
        } else {
            if (node.getData().compareTo(data) > 0) {
                return rGet(data, node.getLeft());
            } else {
                return rGet(data, node.getRight());
            }
        }
    }

    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Cannot remove null data");
        }
        return rContains(data, root);
    }

    /**
     * recursive helper method for the contains method
     *
     * @param data the data be searched for
     * @param node current node being looked at
     * @return true if data is found, false if not
     */
    private boolean rContains(T data, BSTNode<T> node) {
        if (node == null) {
            return false;
        } else if (node.getData().compareTo(data) == 0) {
            return true;
        } else {
            if (node.getData().compareTo(data) > 0) {
                return rContains(data, node.getLeft());
            } else {
                return rContains(data, node.getRight());
            }
        }
    }

    /**
     * Should run in O(n).
     *
     * @return a preorder traversal of the tree
     */
    public List<T> preorder() {
        ArrayList<T> preList = new ArrayList<T>();
        preList = rPreOrder(root, preList);
        return preList;
    }

    /**
     * recursive helper for preorder method
     *
     * @param root starting node of the recursion
     * @param list stores the data in preorder
     * @return list of data in preorder
     */
    private ArrayList<T> rPreOrder(BSTNode<T> root, ArrayList<T> list) {
        if (root != null) {
            list.add(root.getData());
            rPreOrder(root.getLeft(), list);
            rPreOrder(root.getRight(), list);
        }
        return list;
    }

    /**
     * Should run in O(n).
     *
     * @return an inorder traversal of the tree
     */
    public List<T> inorder() {
        ArrayList<T> inList = new ArrayList<T>();
        inList = rInOrder(root, inList);
        return inList;
    }

    /**
     * recursive helper for the in order traversal method
     *
     * @param root starting node of the recursion
     * @param list stores the data in order
     * @return list of data in order
     */
    private ArrayList<T> rInOrder(BSTNode<T> root, ArrayList<T> list) {
        if (root != null) {
            rInOrder(root.getLeft(), list);
            list.add(root.getData());
            rInOrder(root.getRight(), list);
        }
        return list;
    }

    /**
     * Should run in O(n).
     *
     * @return a postorder traversal of the tree
     */
    public List<T> postorder() {
        ArrayList<T> postList = new ArrayList<T>();
        postList = rPostOrder(root, postList);
        return postList;
    }

    /**
     * helper method for post order traversal
     *
     * @param root starting node of the recursion
     * @param list stores the data in post order
     * @return list of data in post order
     */
    private ArrayList<T> rPostOrder(BSTNode<T> root, ArrayList<T> list) {
        if (root != null) {
            rPostOrder(root.getLeft(), list);
            rPostOrder(root.getRight(), list);
            list.add(root.getData());
        }
        return list;
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * To do this, add the root node to a queue. Then, while the queue isn't
     * empty, remove one node, add its data to the list being returned, and add
     * its left and right child nodes to the queue. If what you just removed is
     * {@code null}, ignore it and continue with the rest of the nodes.
     *
     * Should run in O(n). This does not need to be done recursively.
     *
     * @return a level order traversal of the tree
     */
    public List<T> levelorder() {
        ArrayList<T> list = new ArrayList<T>();
        Queue<BSTNode<T>> queue = new ArrayDeque<BSTNode<T>>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<T> n = queue.poll();
            if (n != null) {
                list.add(n.getData());
            }
            if (n.getLeft() != null) {
                queue.add(n.getLeft());
            }
            if (n.getRight() != null) {
                queue.add(n.getRight());
            }
        }
        return list;
    }

    /**
     * This method checks whether a binary tree meets the criteria for being
     * a binary search tree.
     *
     * This method is a static method that takes in a BSTNode called
     * {@code treeRoot}, which is the root of the tree that you should check.
     *
     * You may assume that the tree passed in is a proper binary tree; that is,
     * there are no loops in the tree, the parent-child relationship is
     * correct, that there are no duplicates, and that every parent has at
     * most 2 children. So, what you will have to check is that the order
     * property of a BST is still satisfied.
     *
     * Should run in O(n). However, you should stop the check as soon as you
     * find evidence that the tree is not a BST rather than checking the rest
     * of the tree.
     *
     * @param <T> the generic typing
     * @param treeRoot the root of the binary tree to check
     * @return true if the binary tree is a BST, false otherwise
     */
    public static <T extends Comparable<? super T>> boolean isBST(
            BSTNode<T> treeRoot) {
        return rIsBST(treeRoot, null, null);
    }

    /**
     * recursive helper for isBST method
     * @param curr current node being looked at
     * @param min smaller value
     * @param max larger value
     * @param <T> data type
     * @return true if tree is a BST, false if not
     */
    private static <T extends Comparable<? super T>> boolean rIsBST(
            BSTNode<T> curr, T min, T max)  {
        if (curr == null) {
            return true;
        }
        if (min != null && curr.getData().compareTo(min) < 0) {
            return false;
        } else if (max != null && curr.getData().compareTo(max) > 0) {
            return false;
        }
        return (rIsBST(curr.getLeft(), min, curr.getData()))
                && rIsBST(curr.getRight(), curr.getData(), max);
    }


    /**
     * Clears the tree.
     *
     * Should run in O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Calculate and return the height of the root of the tree. A node's
     * height is defined as {@code max(left.height, right.height) + 1}. A leaf
     * node has a height of 0 and a null child should be -1.
     *
     * Should be calculated in O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (size == 0) {
            return -1;
        }
        return rHeight(root);
    }

    /**
     * recursive helper for the height method
     *
     * @param node current node to calculate the height of
     * @return height of the node
     */
    private int rHeight(BSTNode<T> node) {
        if (node == null) {
            return -1;
        } else {
            int leftHeight = rHeight(node.getLeft());
            int rightHeight = rHeight(node.getRight());
            if (leftHeight > rightHeight) {
                return leftHeight + 1;
            } else {
                return rightHeight + 1;
            }
        }
    }

    /**
     * Returns the size of the BST.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the number of elements in the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Returns the root of the BST.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}