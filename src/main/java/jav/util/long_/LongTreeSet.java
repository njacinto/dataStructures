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
package jav.util.long_;

import jav.util.long_.comparator.LongComparator;
import jav.util.long_.comparator.LongComparatorAsc;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.LongConsumer;

public class LongTreeSet implements LongSet {
    //
    private static final int CONCURRENT_MODIFICATION_MASK = 2147483647;
    protected static final LongComparator DEFAULT_COMPARATOR = LongComparatorAsc.INSTANCE;
    protected static final long DEFAULT_NULL_VALUE = 0;
    //
    protected final Iterable<TreeSetNode> treeIterable = new TreeIterable();
    //
    private LongComparator comparator = DEFAULT_COMPARATOR;
    private int size = 0;
    private TreeSetNode root = null;
    private int concurrentModification = 0;
    private long nullValue = DEFAULT_NULL_VALUE;

    public LongTreeSet() {
    }

    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public LongTreeSet(long ... c) {
        addAll(c);
    }

    
    public long getNullValue() {
        return nullValue;
    }
    
    public void setNullValue(long nullValue) {
        this.nullValue = nullValue;
    }
    
    public LongComparator getComparator() {
        return comparator;
    }

    /**
     * The tree will be rebuilt using the new comparator
     * 
     * @param comparator 
     * @return  
     */
    public LongTreeSet setComparator(LongComparator comparator) {
        if(comparator!=null && !this.comparator.equals(comparator)){
            this.comparator = comparator;
            TreeIterator it = new TreeIterator();
            clear();
            TreeSetNode node;
            while(it.hasNext()){
                node = it.next();
                addNode(node.value);
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
    public boolean contains(long value) {
        TreeSetNode node = getNodeOrParent(root, value);
        return node!=null && node.value==value;
    }

    @Override
    public boolean add(long value) {
        return addNode(value);
    }

    @Override
    public boolean remove(long value) {
        TreeSetNode node = getNodeOrParent(root, value);
        if(node!=null && node.value==value){ 
            removeNode(node);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(long ... c) {
        for(long value : c){
            addNode(value);
        }
        return true;
    }

    @Override
    public boolean addAll(LongCollection c) {
        PrimitiveIterator.OfLong it = c.iterator();
        while(it.hasNext()){
            addNode(it.nextLong());
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Long> c) {
        for(Long value : c){
            addNode(value);
        }
        return true;
    }

    @Override
    public boolean containsAll(long... c) {
        for(long l : c){
            if(!contains(l)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(LongCollection c) {
        PrimitiveIterator.OfLong it = c.iterator();
        while(it.hasNext()){
            if(!contains(it.nextLong())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<? extends Long> c) {
        for(Long l : c){
            if(l==null || !contains(l)){
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean removeAll(long... elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean removeAll(LongCollection elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean removeAll(Collection<? extends Long> elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean retainAll(long... elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public boolean retainAll(LongCollection elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public boolean retainAll(Collection<? extends Long> elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public long[] toArray() {
        long[] ret = new long[size];
        int i = 0;
        TreeIterator it = new TreeIterator();
        while(it.hasNext()){
            ret[i++] = it.next().value;
        }
        return ret;
    }

    @Override
    public long[] toArray(int fromIndex, int toIndex) {
        if(fromIndex<toIndex){
            int max = (toIndex<size ? toIndex : size);
            if(fromIndex<max){
                long[] ret = new long[max-fromIndex];
                int i = 0, index=0;
                TreeIterator it = new TreeIterator();
                while(it.hasNext() && i<max){
                    if(i>=fromIndex){
                        ret[index++] = it.next().value;
                    } else {
                        it.next();
                    }
                    i++;
                }
                return ret;
            } else {
                throw new IndexOutOfBoundsException("From index bigger than number of elements on the set");
            }
        } else {
            throw new IllegalArgumentException("From index bigger than number of elements to retrieve");
        }
    }

    @Override
    public PrimitiveIterator.OfLong iterator() {
        return new ValueIterator(new TreeIterator());
    }   

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TreeIterator it = new TreeIterator();
        TreeSetNode node;
        while(it.hasNext()){
            node = it.next();
            sb.append("[").append(node.value).append("]\n");
        }
        return sb.toString();
    }

    @Override
    public Spliterator.OfLong spliterator() {
        return Spliterators.spliterator(iterator(), 0, 0);
    }
    
    // private methods
    private void incConcurrentModification(){
        concurrentModification = (concurrentModification+1)&CONCURRENT_MODIFICATION_MASK;
    }
    
    
    /**
     * 
     * @param complement
     * @param elRemove
     * @return true if the set was modified, false otherwise
     */
    protected boolean batchRemove(boolean complement, long[] elRemove) {
        final TreeIterator it = new TreeIterator();
        boolean found;
        TreeSetNode node;
        final int concModif = this.concurrentModification;
        while (it.hasNext()) {
            node = it.next();
            found = false;
            for (int i = 0; i < elRemove.length; i++){
                if(elRemove[i] == node.value){
                    found=true;
                    break;
                }
            }
            if(found!=complement){
                it.remove();
            }
        }
        return (concModif != this.concurrentModification);
    }
    
    /**
     * 
     * @param complement
     * @param elRemove
     * @return 
     */
    protected boolean batchRemove(boolean complement, LongCollection elRemove) {
        final TreeIterator it = new TreeIterator();
        TreeSetNode node;
        final int concModif = this.concurrentModification;
        while (it.hasNext()) {
            node = it.next();
            if(elRemove.contains(node.value)!=complement){
                it.remove();
            }
        }
        return (concModif != this.concurrentModification);
    }
    
    /**
     * 
     * @param complement
     * @param elRemove
     * @return 
     */
    protected boolean batchRemove(boolean complement, Collection<? extends Long> elRemove) {
        final TreeIterator it = new TreeIterator();
        TreeSetNode node;
        final int concModif = this.concurrentModification;
        while (it.hasNext()) {
            node = it.next();
            if(elRemove.contains(node.value)!=complement){
                it.remove();
            }
        }
        return (concModif != this.concurrentModification);
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    private boolean addNode(long value){
        boolean ret = true;
        if(this.root==null){
            root = new TreeSetNode(value).setBlack();
            this.size++;
        } else { 
            TreeSetNode parentOrNode = getNodeOrParent(root, value);
            if(parentOrNode!=null){
                if(parentOrNode.value == value){
                    ret = false;
                } else {
                    TreeSetNode newNode = new TreeSetNode(value, parentOrNode);
                    if(comparator.compare(parentOrNode.value, value)  > 0){
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
    private void afterInsert(TreeSetNode node){
        while(node.parent!=null && node.parent.parent!=null && node.parent.red){
            if(node.parent==node.parent.parent.left){
                TreeSetNode y = node.parent.parent.right;
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
                TreeSetNode y = node.parent.parent.left;
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
    private TreeSetNode removeNode(TreeSetNode node){ 
        TreeSetNode toBeRemoved, sibling;
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
            node.value = toBeRemoved.value;
        }
        if(!toBeRemoved.red && root!=null){
            afterRemove(sibling!=null ? sibling : toBeRemoved);
        }
        toBeRemoved.parent = toBeRemoved.left = toBeRemoved.right = null;
        size--;
        incConcurrentModification();
        return node;
    }
    
    /**
     * Reorganize the tree after removing an element (colors and rotations)
     * @param node 
     */
    private void afterRemove(TreeSetNode node){
        while(node!=root && !node.red){
            if(node==node.parent.left || (node.parent.left==null && node!=node.parent.right)){
                TreeSetNode p = node.parent.right;
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
                TreeSetNode p = node.parent.left;
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
    
    private static TreeSetNode getSuccessor(TreeSetNode node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            return getSmallestEntry(node.right);
        } else {
            TreeSetNode curr = node.parent;
            TreeSetNode prev = node;
            while (curr != null && prev == curr.right) {
                prev = curr;
                curr = curr.parent;
            }
            return curr;
        }
    }
    
    //
    private static TreeSetNode getSmallestEntry(TreeSetNode node){
        if(node!=null){
            while(node.left !=null){
                node = node.left;
            }
            return node;
        }
        return null;
    }
    
    private TreeSetNode getNodeOrParent(TreeSetNode root, long value){
        TreeSetNode node = root;
        while(node !=null){
            if(node.value == value){
                return node;
            } else if(comparator.compare(node.value, value) > 0){
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
    
    private void rotateLeft(TreeSetNode node){
        TreeSetNode p1 = node.right;
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
    
    private void rotateRight(TreeSetNode node){
        TreeSetNode p1 = node.left;
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
    protected TreeSetNode cloneTree(){
        if(this.root==null){
            return null;
        } else {
            TreeSetNode rootTmp = this.root.clone();
            TreeSetNode newNavNode = rootTmp;
            TreeSetNode navNode = root;
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
    
    protected static class ValueIterator implements PrimitiveIterator.OfLong {

        private TreeIterator it;

        public ValueIterator(TreeIterator it) {
            this.it = it;
        }
        
        @Override
        public long nextLong() {
            return it.next().value;
        }

        @Override
        public void forEachRemaining(LongConsumer action) {
            while(it.hasNext()){
                action.accept(it.next().value);
            }
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Long next() {
            return it.next().value;
        }

        @Override
        public void remove() {
            it.remove(); 
        }
    }
    
    protected class TreeSetNode {
        private long value;
        private TreeSetNode left, right, parent;
        private boolean red = true;

        TreeSetNode() {
        }

        TreeSetNode(long value) {
            this.value = value;
        }

        TreeSetNode(long value, boolean isRed) {
            this.value = value;
            this.red = isRed;
        }

        TreeSetNode(long value, TreeSetNode parent) {
            this.value = value;
            this.parent = parent;
        }

        public long getValue() {
            return value;
        }

        public long setValue(long value) {
            long tmp = this.value;
            this.value = value;
            return tmp;
        }

        public boolean isRed() {
            return red;
        }
        
        public boolean isBlack(){
            return !red;
        }
        
        public TreeSetNode setRed(){
            red = true;
            return this;
        }
        
        public TreeSetNode setBlack(){
            red = false;
            return this;
        }

        public TreeSetNode getLeft() {
            return left;
        }

        public TreeSetNode setLeft(TreeSetNode left) {
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
        public TreeSetNode attachLeft(TreeSetNode left) {
            if(this.left!=null){
                this.left.parent = null;
            }
            this.left = left;
            if(left!=null){
                left.parent = this;
            }
            return this;
        }

        public TreeSetNode getRight() {
            return right;
        }

        public TreeSetNode setRight(TreeSetNode right) {
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
        public TreeSetNode attachRight(TreeSetNode right) {
            if(this.right!=null){
                this.right.parent = null;
            }
            this.right = right;
            if(right!=null){
                right.parent = this;
            }
            return this;
        }

        public TreeSetNode getParent() {
            return parent;
        }

        public TreeSetNode setParent(TreeSetNode parent) {
            this.parent = parent;
            return this;
        }
        
        public int compareTo(long otherValue){
            return LongTreeSet.this.comparator.compare(value, otherValue);
        }
        
        @Override
        public TreeSetNode clone(){
            return new TreeSetNode(this.value, this.red);
        }
    }
    
    protected class TreeIterable implements Iterable<TreeSetNode> {

        @Override
        public Iterator<TreeSetNode> iterator() {
            return new TreeIterator();
        }
        
    }
    
    protected class TreeIterator implements Iterator<TreeSetNode> {

        private int concurrentModificationMark;
        private TreeSetNode prev;
        private TreeSetNode next;
        
        TreeIterator(){
            this.concurrentModificationMark = LongTreeSet.this.concurrentModification;
            this.next = LongTreeSet.getSmallestEntry(root);
        }
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public TreeSetNode next() {
            if(next==null){
                throw new NoSuchElementException();
            }
            if(this.concurrentModificationMark!=LongTreeSet.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            prev = next;
            next = findNext(next);
            return prev;
        }

        @Override
        public void remove() {
            if(this.concurrentModificationMark!=LongTreeSet.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            if(prev!=null){
                TreeSetNode node = LongTreeSet.this.removeNode(prev);
                prev = null;
                this.concurrentModificationMark = LongTreeSet.this.concurrentModification;
                if(node!=null && next!=null && node.value==next.value){
                    next = node;
                }
            } else {
                throw new IllegalStateException();
            }
        }
        
        private TreeSetNode findNext(TreeSetNode node){
            if (node == null) {
                return null;
            } else if (node.getRight() != null) {
                TreeSetNode n = node.getRight();
                while (n.getLeft() != null) {
                    n = n.getLeft();
                }
                return n;
            } else {
                TreeSetNode p = node.getParent();
                TreeSetNode ch = node;
                while (p != null && ch == p.getRight()) {
                    ch = p;
                    p = p.getParent();
                }
                return p;
            }
        }
        
    }
    
}
