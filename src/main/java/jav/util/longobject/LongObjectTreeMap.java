/*
 * Copyright (C) 2015.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package jav.util.longobject;

import jav.util.long_.LongSet;
import jav.util.long_.comparator.LongComparator;
import jav.util.long_.comparator.LongComparatorAsc;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class LongObjectTreeMap<T> implements LongObjectMap<T> {
    //
    private static final int CONCURRENT_MODIFICATION_MASK = 2147483647;
    protected static final LongComparator DEFAULT_COMPARATOR = LongComparatorAsc.INSTANCE;
    //
    protected final Iterable<LongObjectMap.Entry<T>> treeIterable = new TreeIterable();
    //
    private LongComparator comparator = DEFAULT_COMPARATOR;
    private int size = 0;
    private TreeNode<T> root = null;
    private int concurrentModification = 0;

    public LongComparator getComparator() {
        return comparator;
    }

    /**
     * The tree will be rebuilt using the new comparator
     * 
     * @param comparator 
     * @return  
     */
    public LongObjectTreeMap setComparator(LongComparator comparator) {
        if(comparator!=null && !this.comparator.equals(comparator)){
            this.comparator = comparator;
            TreeIterator it = new TreeIterator();
            clear();
            TreeNode<T> node;
            while(it.hasNext()){
                node = it.next();
                addNode(node.key, node.value);
            }
        }
        return this;
    }
    
    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean containsKey(long key) {
        TreeNode<T> node = getNodeOrParent(root, key);
        return node!=null && node.key==key;
    }

    @Override
    public boolean containsValue(T value) {
        TreeIterator it = new TreeIterator();
        TreeNode<T> node;
        while(it.hasNext()){
            node = it.next();
            if(node.value==value){
                return true;
            }
        }
        return false;
        
    }

    @Override
    public T get(long key) {
        TreeNode<T> node = getNodeOrParent(root, key);
        return node==null || node.key!=key ? null : node.value;
    }

    @Override
    public T put(long key, T value) {
        return addNode(key, value);
    }

    @Override
    public T remove(long key) {
        TreeNode<T> node = getNodeOrParent(root, key);
        return node==null || node.key!=key ? null : removeNode(node);
        
    }

    @Override
    public void putAll(LongObjectMap<T> m) {
        for(LongObjectMap.Entry<T> entry : m.entrySet()){
            addNode(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void putAll(Map<? extends Long, ? extends T> m) {
        for(Map.Entry<? extends Long, ? extends T> entry : m.entrySet()){
            addNode(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public LongSet longKeySet() {
        return new LongObjectMapUtil.KeyLongObjectSet<T>(treeIterable, this);
        
    }

    @Override
    public Collection<T> values() {
        return new LongObjectMapUtil.ValueLongObjectCollection<T>(treeIterable, this);
    }

    @Override
    public Set<Entry<T>> entrySet() {
        return new LongObjectMapUtil.EntrySet<T>(treeIterable, this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TreeIterator it = new TreeIterator();
        TreeNode node;
        while(it.hasNext()){
            node = it.next();
            sb.append("[").append(node.key).append(" - ").append(node.value).append("]\n");
        }
        return sb.toString();
    }
    
    // private methods
    private void incConcurrentModification(){
        concurrentModification = (concurrentModification+1)&CONCURRENT_MODIFICATION_MASK;
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    private T addNode(long key, T value){
        T ret = null;
        if(this.root==null){
            root = new TreeNode<T>(key, value).setBlack();
            this.size++;
        } else { 
            TreeNode<T> parentOrNode = getNodeOrParent(root, key);
            if(parentOrNode!=null){
                if(parentOrNode.key == key){
                    ret = parentOrNode.getValue();
                    parentOrNode.setValue(value);
                } else {
                    TreeNode<T> newNode = new TreeNode<T>(key, value, parentOrNode);
                    if(comparator.compare(parentOrNode.key, key)  > 0){
                        parentOrNode.left = newNode;
                    } else {
                        parentOrNode.right = newNode;
                    }
                    this.size++;
                    afterInsert(newNode);
                }
            } else {
                throw new RuntimeException("Null parent");
            }
        }
        incConcurrentModification();
        return ret;
    }
    
    /**
     * Reorganize the tree after insert (colors and rotations)
     * 
     * @param node 
     */
    private void afterInsert(TreeNode<T> node){
        while(node.parent!=null && node.parent.parent!=null && node.parent.red){
            if(node.parent==node.parent.parent.left){
                TreeNode<T> y = node.parent.parent.right;
                if(y!=null && y.red){
                    node.parent.setBlack();
                    y.setBlack();
                    node.parent.parent.setRed();
                    node = node.parent.parent;
                } else {
                    if(node==node.parent.right){
                        node = node.parent;
                        rotateLeft(node);
                    }
                    node.parent.setBlack();
                    node.parent.parent.setRed();
                    rotateRight(node.parent.parent);
                }
            } else {
                TreeNode<T> y = node.parent.parent.left;
                if(y!=null && y.red){
                    node.parent.setBlack();
                    y.setBlack();
                    node.parent.parent.setRed();
                    node = node.parent.parent;
                } else {
                    if(node==node.parent.left){
                        node = node.parent;
                        rotateRight(node);
                    }
                    node.parent.setBlack();
                    node.parent.parent.setRed();
                    rotateLeft(node.parent.parent);
                }
            }
        }
        root.setBlack();
    }
    
    /**
     * 
     * @param node
     * @return 
     */
    private T removeNode(TreeNode<T> node){ // TODO: Change
        T ret = node.value;
        TreeNode<T> toBeRemoved, sibling;
        toBeRemoved = (node.left==null || node.right==null) ? node : getSuccessor(node);
        sibling = (toBeRemoved.left!=null)? toBeRemoved.left : toBeRemoved.right;
        if(sibling!=null){
            sibling.parent = toBeRemoved.parent;
        }
        if(toBeRemoved.parent==null){
            root = sibling;
        } else {
            if(toBeRemoved==toBeRemoved.parent.left){
                toBeRemoved.parent.left = sibling;
            } else {
                toBeRemoved.parent.right = sibling;
            }
        }
        if(toBeRemoved!=node){
            node.key = toBeRemoved.key;
            node.value = toBeRemoved.value;
        }
        if(!toBeRemoved.red && root!=null){
            afterRemove(sibling!=null ? sibling : toBeRemoved);
        }
        toBeRemoved.parent = toBeRemoved.left = toBeRemoved.right = null;
        size--;
        incConcurrentModification();
        return ret;
    }
    
    /**
     * Reorganize the tree after removing an element (colors and rotations)
     * @param node 
     */
    private void afterRemove(TreeNode<T> node){
        while(node!=root && !node.red){
            if(node==node.parent.left || (node.parent.left==null && node!=node.parent.right)){
                TreeNode<T> p = node.parent.right;
                if(p.red){
                    p.setBlack();
                    node.parent.setRed();
                    rotateLeft(node.parent);
                    p = node.parent.right;
                }
                if((p.left==null || !p.left.red) && (p.right==null || !p.right.red)){
                    p.setRed();
                    node = node.parent;
                } else {
                    if(p.right==null || !p.right.red){
                        p.left.setBlack();
                        p.setRed();
                        rotateRight(p);
                        p = node.parent.right;
                    }
                    p.red = node.parent.red;
                    node.parent.setBlack();
                    p.right.setBlack();
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                TreeNode<T> p = node.parent.left;
                if(p.red){
                    p.setBlack();
                    node.parent.setRed();
                    rotateRight(node.parent);
                    p = node.parent.left;
                }
                if((p.left==null || !p.left.red) && (p.right==null || !p.right.red)){
                    p.setRed();
                    node = node.parent;
                } else {
                    if(p.left==null || !p.left.red){
                        p.right.setBlack();
                        p.setRed();
                        rotateLeft(p);
                        p = node.parent.left;
                    }
                    p.red = node.parent.red;
                    node.parent.setBlack();
                    p.left.setBlack();
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        node.setBlack();
    }
    
    private TreeNode<T> getSuccessor(TreeNode<T> node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            return getSmallestEntry(node.right);
        } else {
            TreeNode<T> curr = node.parent;
            TreeNode<T> prev = node;
            while (curr != null && prev == curr.right) {
                prev = curr;
                curr = curr.parent;
            }
            return curr;
        }
    }
    
    //
    private TreeNode<T> getSmallestEntry(TreeNode<T> node){
        if(node!=null){
            while(node.left !=null){
                node = node.left;
            }
            return node;
        }
        return null;
    }
    
    private TreeNode<T> getNodeOrParent(TreeNode<T> root, long key){
        TreeNode<T> node = root;
        while(node !=null){
            if(node.key == key){
                return node;
            } else if(comparator.compare(node.key, key) > 0){
                if(node.left==null){
                    return node;
                }
                node = node.left;
            } else {
                if(node.right==null){
                    return node;
                }
                node = node.right;
            }
        }
        return node;
    }
    
    private void rotateLeft(TreeNode<T> node){
        TreeNode<T> p1 = node.right;
        node.right = p1.left;
        if(p1.left!=null){
            p1.left.parent = node;
        }
        p1.parent = node.parent;
        if(node.parent==null){
            root = p1;
        } else {
            if(node == node.parent.left){
                node.parent.left = p1;
            } else {
                node.parent.right = p1;
            }
        }
        p1.left = node;
        node.parent = p1;
    }
    
    private void rotateRight(TreeNode<T> node){
        TreeNode<T> p1 = node.left;
        node.left = p1.right;
        if(p1.right!=null){
            p1.right.parent = node;
        }
        p1.parent = node.parent;
        if(node.parent==null){
            root = p1;
        } else {
            if(node == node.parent.right){
                node.parent.right = p1;
            } else {
                node.parent.left = p1;
            }
        }
        p1.right = node;
        node.parent = p1;
    }
    
    /**
     * For testing
     * 
     * @return 
     */
    protected TreeNode<T> cloneTree(){
        if(this.root==null){
            return null;
        } else {
            TreeNode<T> rootTmp = this.root.clone();
            TreeNode<T> newNavNode=rootTmp, navNode=root;
            while(navNode != null){
                if(navNode.getLeft()!=null && newNavNode.getLeft()==null){ //
                    navNode = navNode.getLeft();
                    newNavNode.attachLeft(navNode.clone());
                    newNavNode = newNavNode.getLeft();
                } else if(navNode.getRight()!=null && newNavNode.getRight()==null){
                    navNode = navNode.getRight();
                    newNavNode.attachRight(navNode.clone());
                    newNavNode = newNavNode.getRight();
                } else { 
                    navNode = navNode.getParent();
                    newNavNode = newNavNode.getParent();
                }
            }
            return rootTmp;
        }
    }
    
    //
    protected class TreeNode<T> implements LongObjectMap.Entry<T> {
        private long key;
        private T value;
        private TreeNode<T> left, right, parent;
        private boolean red = true;

        TreeNode() {
        }

        TreeNode(long key, T value) {
            this.key = key;
            this.value = value;
        }

        TreeNode(long key, T value, boolean isRed) {
            this(key, value);
            this.red = isRed;
        }

        TreeNode(long key, T value, TreeNode<T> parent) {
            this(key, value);
            this.parent = parent;
        }
        
        @Override
        public long getKey() {
            return key;
        }

        @Override
        public T getValue() {
            return value;
        }

        @Override
        public T setValue(T value) {
            T tmp = this.value;
            this.value = value;
            return tmp;
        }

        public boolean isRed() {
            return red;
        }
        
        public boolean isBlack(){
            return !red;
        }
        
        public TreeNode<T> setRed(){
            red = true;
            return this;
        }
        
        public TreeNode<T> setBlack(){
            red = false;
            return this;
        }

        public TreeNode<T> getLeft() {
            return left;
        }

        public TreeNode<T> setLeft(TreeNode<T> left) {
            this.left = left;
            return this;
        }

        /**
         * Sets the left child to the new node provided, setting also the parent
         * of the new node to this node.
         * If a left node already exists on this node, it's parent will be set 
         * to null.
         * @param left
         * @return 
         */
        public TreeNode attachLeft(TreeNode<T> left) {
            if(this.left!=null){
                this.left.parent = null;
            }
            this.left = left;
            if(left!=null){
                left.parent = this;
            }
            return this;
        }

        public TreeNode<T> getRight() {
            return right;
        }

        public TreeNode<T> setRight(TreeNode<T> right) {
            this.right = right;
            return this;
        }   

        /**
         * Sets the right child to the new node provided, setting also the parent
         * of the new node to this node.
         * If a right node already exists on this node, it's parent will be set 
         * to null.
         * @param right
         * @return 
         */
        public TreeNode<T> attachRight(TreeNode<T> right) {
            if(this.right!=null){
                this.right.parent = null;
            }
            this.right = right;
            if(right!=null){
                right.parent = this;
            }
            return this;
        }

        public TreeNode<T> getParent() {
            return parent;
        }

        public TreeNode<T> setParent(TreeNode<T> parent) {
            this.parent = parent;
            return this;
        }
        
        public int compareTo(long otherKey){
            return LongObjectTreeMap.this.comparator.compare(key, otherKey);
        }
        
        @Override
        public TreeNode<T> clone(){
            return new TreeNode<T>(this.key, this.value, this.red);
        }
    }
    
    private class TreeIterable implements Iterable<LongObjectMap.Entry<T>> {

        @Override
        public Iterator<Entry<T>> iterator() {
            return new TreeIterator();
        }
        
    }
    
    private class TreeIterator implements Iterator<LongObjectMap.Entry<T>> {

        private int concurrentModificationMark;
        private TreeNode<T> prev;
        private TreeNode<T> next;
        
        TreeIterator(){
            this.concurrentModificationMark = LongObjectTreeMap.this.concurrentModification;
            this.next = LongObjectTreeMap.this.getSmallestEntry(root);
        }
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public TreeNode<T> next() {
            if(next==null){
                throw new NoSuchElementException();
            }
            if(this.concurrentModificationMark!=LongObjectTreeMap.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            prev = next;
            next = findNext(next);
            return prev;
        }

        @Override
        public void remove() {
            if(this.concurrentModificationMark!=LongObjectTreeMap.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            if(prev!=null){
                LongObjectTreeMap.this.remove(prev.key);
                prev = null;
                this.concurrentModificationMark = LongObjectTreeMap.this.concurrentModification;
            } else {
                throw new IllegalStateException();
            }
        }
        
        private TreeNode<T> findNext(TreeNode<T> node){
            if (node == null) {
                return null;
            } else if (node.getRight() != null) {
                TreeNode<T> n = node.getRight();
                while (n.getLeft() != null) {
                    n = n.getLeft();
                }
                return n;
            } else {
                TreeNode<T> p = node.getParent();
                TreeNode<T> ch = node;
                while (p != null && ch == p.getRight()) {
                    ch = p;
                    p = p.getParent();
                }
                return p;
            }
        }
        
    }
    
}
