import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Carson Duerr
 * @userid cduerr3
 * @GTID 903186923
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it appears in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        for (T newData: data) {
            if (newData == null) {
                throw new IllegalArgumentException("Data cannot be null");
            }
            add(newData);
        }
    }

    /**
     * Adds the data to the AVL. Start by adding it as a leaf like in a regular
     * BST and then rotate the tree as needed.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to recalculate heights and balance factors going up the tree,
     * rebalancing if necessary.
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
     * Recursive helper method for the add method
     *
     * @param data the data to be added
     * @param node current node being looked at
     * @return current AVL node
     */
    private AVLNode<T> rAdd(T data, AVLNode<T> node) {
        if (node == null) {
            ++size;
            return new AVLNode<T>(data);
        } else if (node.getData().compareTo(data) == 0) {
            return node;
        } else {
            if (node.getData().compareTo(data) > 0) {
                node.setLeft(rAdd(data, node.getLeft()));
            } else {
                node.setRight(rAdd(data, node.getRight()));
            }
            updateHeightAndBF(node);
            return rotate(node);
        }
    }

    /**
     * Updates height and balance factor of current node
     *
     * @param  node node to be updated
     */
    private void updateHeightAndBF(AVLNode<T> node) {
        int leftHeight = (node.getLeft() == null) ? -1 : node.getLeft()
                .getHeight();
        int rightHeight = (node.getRight() == null) ? -1 : node.getRight()
                .getHeight();
        node.setHeight(Math.max(leftHeight, rightHeight) + 1);
        node.setBalanceFactor(leftHeight - rightHeight);
    }

    /**
     * Rotates current node and subtree to maintain balance
     *
     * Chooses correct type of rotation to perform
     *
     * @param  node root of subtree being looked at
     * @return new root of subtree
     */
    private AVLNode<T> rotate(AVLNode<T> node) {
        int bf = node.getBalanceFactor();
        if (bf > 1) {
            int bfLeft = node.getLeft().getBalanceFactor();
            return (bfLeft < 0) ? leftRightRotation(node)
                    : rightRotation(node);
        } else if (bf < -1) {
            int bfRight = node.getRight().getBalanceFactor();
            return (bfRight > 0) ? rightLeftRotation(node)
                    : leftRotation(node);
        } else {
            return node;
        }
    }

    /**
     * left-right rotation to balance the subtree
     *
     * @param  node root of subtree to rotate
     * @return new root of subtree
     */
    private AVLNode<T> leftRightRotation(AVLNode<T> node) {
        node.setLeft(leftRotation(node.getLeft()));
        return rightRotation(node);
    }

    /**
     * right rotation to balance the subtree.
     *
     * @param  node root of subtree to rotate
     * @return new root of subtree
     */
    private AVLNode<T> rightRotation(AVLNode<T> node) {
        AVLNode<T> leftChild = node.getLeft();
        node.setLeft(leftChild.getRight());
        leftChild.setRight(node);
        updateHeightAndBF(node);
        updateHeightAndBF(leftChild);
        return leftChild;
    }

    /**
     * right-left rotation to balance the subtree
     *
     * @param  node root of subtree to rotate
     * @return new root of subtree
     */
    private AVLNode<T> rightLeftRotation(AVLNode<T> node) {
        node.setRight(rightRotation(node.getRight()));
        return leftRotation(node);
    }

    /**
     * left rotation to balance the subtree
     *
     * @param  node root of subtree to rotate
     * @return new root of subtree
     */
    private AVLNode<T> leftRotation(AVLNode<T> node) {
        AVLNode<T> rightChild = node.getRight();
        node.setRight(rightChild.getLeft());
        rightChild.setLeft(node);
        updateHeightAndBF(node);
        updateHeightAndBF(rightChild);
        return rightChild;
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the successor to replace the data,
     * not the predecessor. As a reminder, rotations can occur after removing
     * the successor node.
     *
     * Remember to recalculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        AVLNode<T> tempNode = new AVLNode<T>(null);
        root = rRemove(data, root, tempNode);
        --size;
        return tempNode.getData();
    }

    /**
     * Recursive helper method for the remove method
     *
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data being searched for
     * @param node current node
     * @param storage stores the removed data
     * @return current node
     */
    private AVLNode<T> rRemove(T data, AVLNode<T> node, AVLNode<T> storage) {
        if (node == null) {
            throw new java.util.NoSuchElementException("Data is not in"
                    + " the AVL tree");
        }
        if (node.getData().compareTo(data) == 0) {
            storage.setData(node.getData());
            if (node.getLeft() == null && node.getRight() == null) {
                return null;
            } else if (node.getLeft() != null && node.getRight() == null) {
                return node.getLeft();
            } else if (node.getLeft() == null && node.getRight() != null) {
                return node.getRight();
            } else {
                AVLNode<T> tempNode = new AVLNode<T>(null);
                node.setLeft(findPredecessor(node.getLeft(), tempNode));
                node.setData(tempNode.getData());
            }
        } else {
            if (node.getData().compareTo(data) > 0) {
                node.setLeft(rRemove(data, node.getLeft(), storage));
            } else {
                node.setRight(rRemove(data, node.getRight(), storage));
            }
        }
        updateHeightAndBF(node);
        return rotate(node);
    }

    /**
     * helper method that finds the direct predecessor of a node.
     *
     * @param node node to find the predecessor of
     * @param tempNode node storing predecessor node data
     * @return removed data from the tree
     */
    private AVLNode<T> findPredecessor(AVLNode<T> node, AVLNode<T> tempNode) {
        if (node.getRight() == null) {
            tempNode.setData(node.getData());
            return node.getLeft();
        } else {
            node.setRight(findPredecessor(node.getRight(), tempNode));
        }
        return node;
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
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
            throw new java.util.NoSuchElementException("Data was not found");
        }
        return rGet(data, root);
    }

    /**
     * Recursive helper method for get
     *
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data being searched for
     * @param node current node being looked at
     * @return the data that was searched for
     */
    private T rGet(T data, AVLNode<T> node) {
        if (node == null) {
            throw new java.util.NoSuchElementException("Data was not found");
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
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }
        return rContains(data, root);
    }

    /**
     * Recursively goes through data structure each node at a time to find
     * inputted data.
     *
     * @param data the data being searched for
     * @param node node spot being looked at to check for data
     * @return whether or not data is contained in current node
     */
    private boolean rContains(T data, AVLNode<T> node) {
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
     * Returns the data on branches of the tree with the maximum depth. If you
     * encounter multiple branches of maximum depth while traversing, then you
     * should list the remaining data from the left branch first, then the
     * remaining data in the right branch. This is essentially a preorder
     * traversal of the tree, but only of the branches of maximum depth.
     *
     * Your list should not duplicate data, and the data of a branch should be
     * listed in order going from the root to the leaf of that branch.
     *
     * Should run in worst case O(n), but you should not explore branches that
     * do not have maximum depth. You should also not need to traverse branches
     * more than once.
     *
     * Hint: How can you take advantage of the balancing information stored in
     * AVL nodes to discern deep branches?
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * Returns: [10, 5, 2, 1, 0, 7, 8, 9, 15, 20, 25, 30]
     *
     * @return the list of data in branches of maximum depth in preorder
     * traversal order
     */
    public List<T> deepestBranches() {
        List<T> deep = new ArrayList<>();
        rDeepestBranches(root, deep);
        return deep;
    }

    /**
     * recursive helper for deepestBranches method
     *
     * @param node current node location
     * @param deep list of the data on the deepest branches
     */
    private void rDeepestBranches(AVLNode<T> node, List<T> deep) {
        if (node == null) {
            return;
        } else {
            deep.add(node.getData());
            if (node.getLeft() != null && node.getLeft().getHeight() == node.getHeight() - 1) {
                rDeepestBranches(node.getLeft(), deep);
            }
            if (node.getRight() != null && (node.getRight().getHeight() == node.getHeight() - 1)) {
                rDeepestBranches(node.getRight(), deep);
            }
        }
    }

    /**
     * Returns a sorted list of data that are within the threshold bounds of
     * data1 and data2. That is, the data should be > data1 and < data2.
     *
     * Should run in worst case O(n), but this is heavily dependent on the
     * threshold data. You should not explore branches of the tree that do not
     * satisfy the threshold.
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * sortedInBetween(7, 14) returns [8, 9, 10, 13]
     * sortedInBetween(3, 8) returns [4, 5, 6, 7]
     * sortedInBetween(8, 8) returns []
     *
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold
     * @throws IllegalArgumentException if data1 or data2 are null
     * or if data1 > data2
     * @return a sorted list of data that is > data1 and < data2
     */
    public List<T> sortedInBetween(T data1, T data2) {
        if (data1 == null) {
            throw new IllegalArgumentException("Data1 cannot be null");
        }
        if (data2 == null) {
            throw new IllegalArgumentException("Data2 cannot be null");
        }
        if (data1.compareTo(data2) > 0) {
            throw new IllegalArgumentException("Data1 cannot be greater than data2");
        }
        List<T> sorted = new ArrayList<>();
        rSortedInBetween(data1, data2, root, sorted);
        return sorted;
    }

    /**
     * recursive helper for the sortedInBetween method
     * @param data1 smaller bound
     * @param data2 larger bound
     * @param node current node
     * @param sorted list of the sorted data between data1 and data2
     */
    private void rSortedInBetween(T data1, T data2, AVLNode<T> node, List<T> sorted) {
        if (node == null) {
            return;
        }
        if (node.getData().compareTo(data1) > 0 && node.getData().compareTo(data2) < 0 && node.getLeft() == null) {
            sorted.add(node.getData());
        }
        if (node.getData().compareTo(data1) > 0 || node.getData().compareTo(data2) == 0) {
            rSortedInBetween(data1, data2, node.getLeft(), sorted);
        }
        if (sorted.size() != 0 && node.getData().compareTo(data1) > 0 && node.getData().compareTo(data2) < 0 && node.getData().compareTo(sorted.get(sorted.size() - 1)) > 0) {
            sorted.add(node.getData());
        }
        if (node.getData().compareTo(data2) <= 0 || node.getData().compareTo(data1) == 0) {
            rSortedInBetween(data1, data2, node.getRight(), sorted);
        }
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Since this is an AVL, this method does not need to traverse the tree
     * and should be O(1)
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        if (root == null) {
            return -1;
        }
        return root.getHeight();
    }

    /**
     * Returns the size of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the AVL tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }

    /**
     * Returns the root of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the AVL tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}