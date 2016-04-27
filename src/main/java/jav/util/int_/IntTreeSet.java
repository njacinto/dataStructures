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
package jav.util.int_;

import jav.util.int_.comparator.IntComparator;
import jav.util.int_.comparator.IntComparatorAsc;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.IntConsumer;

public class IntTreeSet implements IntSet {
    //
    private static final int CONCURRENT_MODIFICATION_MASK = 2147483647;
    protected static final IntComparator DEFAULT_COMPARATOR = IntComparatorAsc.INSTANCE;
    protected static final int DEFAULT_NULL_VALUE = 0;
    //
    protected final Iterable<Entry> treeIterable = new TreeIterable();
    //
    private IntComparator comparator = DEFAULT_COMPARATOR;
    private int size = 0;
    private Entry root = null;
    private int concurrentModification = 0;
    private int nullValue = DEFAULT_NULL_VALUE;

    public IntTreeSet() {
    }

    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public IntTreeSet(int ... c) {
        addAll(c);
    }

    
    public int getNullValue() {
        return nullValue;
    }
    
    public void setNullValue(int nullValue) {
        this.nullValue = nullValue;
    }
    
    public IntComparator getComparator() {
        return comparator;
    }

    /**
     * The tree will be rebuilt using the new comparator
     * 
     * @param comparator 
     * @return  
     */
    public IntTreeSet setComparator(IntComparator comparator) {
        if(comparator!=null && !this.comparator.equals(comparator)){
            this.comparator = comparator;
            TreeIterator it = new TreeIterator();
            clear();
            Entry node;
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
    public boolean contains(int value) {
        Entry node = getNodeOrParent(root, value);
        return node!=null && node.value==value;
    }

    @Override
    public boolean add(int value) {
        return addNode(value);
    }

    @Override
    public boolean remove(int value) {
        Entry node = getNodeOrParent(root, value);
        if(node!=null && node.value==value){ 
            removeNode(node);
            return true;
        }
        return false;
    }

    @Override
    public boolean addAll(int ... c) {
        for(int value : c){
            addNode(value);
        }
        return true;
    }

    @Override
    public boolean addAll(IntCollection c) {
        PrimitiveIterator.OfInt it = c.iterator();
        while(it.hasNext()){
            addNode(it.nextInt());
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Integer> c) {
        for(Integer value : c){
            addNode(value);
        }
        return true;
    }

    @Override
    public boolean containsAll(int... c) {
        for(int l : c){
            if(!contains(l)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(IntCollection c) {
        PrimitiveIterator.OfInt it = c.iterator();
        while(it.hasNext()){
            if(!contains(it.nextInt())){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<? extends Integer> c) {
        for(Integer l : c){
            if(l==null || !contains(l)){
                return false;
            }
        }
        return true;
    }


    @Override
    public boolean removeAll(int... elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean removeAll(IntCollection elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean removeAll(Collection<? extends Integer> elements) {
        Objects.requireNonNull(elements);
        return batchRemove(false, elements);
    }

    @Override
    public boolean retainAll(int... elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public boolean retainAll(IntCollection elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public boolean retainAll(Collection<? extends Integer> elements) {
        Objects.requireNonNull(elements);
        return batchRemove(true, elements);
    }

    @Override
    public int[] toArray() {
        int[] ret = new int[size];
        int i = 0;
        TreeIterator it = new TreeIterator();
        while(it.hasNext()){
            ret[i++] = it.next().value;
        }
        return ret;
    }

    @Override
    public int[] toArray(int fromIndex, int toIndex) {
        if(fromIndex<toIndex){
            int max = (toIndex<size ? toIndex : size);
            if(fromIndex<max){
                int[] ret = new int[max-fromIndex];
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
    public PrimitiveIterator.OfInt iterator() {
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
        Entry node;
        while(it.hasNext()){
            node = it.next();
            sb.append("[").append(node.value).append("]\n");
        }
        return sb.toString();
    }

    @Override
    public Spliterator.OfInt spliterator() {
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
    protected boolean batchRemove(boolean complement, int[] elRemove) {
        final TreeIterator it = new TreeIterator();
        boolean found;
        Entry node;
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
    protected boolean batchRemove(boolean complement, IntCollection elRemove) {
        final TreeIterator it = new TreeIterator();
        Entry node;
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
    protected boolean batchRemove(boolean complement, Collection<? extends Integer> elRemove) {
        final TreeIterator it = new TreeIterator();
        Entry node;
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
    private boolean addNode(int value){
        boolean ret = true;
        if(this.root==null){
            root = new Entry(value).setBlack();
            this.size++;
        } else { 
            Entry parentOrNode = getNodeOrParent(root, value);
            if(parentOrNode!=null){
                if(parentOrNode.value == value){
                    ret = false;
                } else {
                    Entry newNode = new Entry(value, parentOrNode);
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
    private void afterInsert(Entry node){
        while(node.parent!=null && node.parent.parent!=null && node.parent.red){
            if(node.parent==node.parent.parent.left){
                Entry y = node.parent.parent.right;
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
                Entry y = node.parent.parent.left;
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
    private Entry removeNode(Entry node){ 
        Entry toBeRemoved;
        Entry sibling;
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
    private void afterRemove(Entry node){
        while(node!=root && !node.red){
            if(node==node.parent.left || (node.parent.left==null && node!=node.parent.right)){
                Entry p = node.parent.right;
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
                Entry p = node.parent.left;
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
    
    private static Entry getSuccessor(Entry node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            return getSmallestEntry(node.right);
        } else {
            Entry curr = node.parent;
            Entry prev = node;
            while (curr != null && prev == curr.right) {
                prev = curr;
                curr = curr.parent;
            }
            return curr;
        }
    }
    
    //
    private static Entry getSmallestEntry(Entry node){
        if(node!=null){
            while(node.left !=null){
                node = node.left;
            }
            return node;
        }
        return null;
    }
    
    private Entry getNodeOrParent(Entry root, int value){
        Entry node = root;
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
    
    private void rotateLeft(Entry node){
        Entry p1 = node.right;
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
    
    private void rotateRight(Entry node){
        Entry p1 = node.left;
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
    protected Entry cloneTree(){
        if(this.root==null){
            return null;
        } else {
            Entry rootTmp = this.root.clone();
            Entry newNavNode = rootTmp;
            Entry navNode = root;
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
    
    protected static class ValueIterator implements PrimitiveIterator.OfInt {

        private TreeIterator it;

        public ValueIterator(TreeIterator it) {
            this.it = it;
        }
        
        @Override
        public int nextInt() {
            return it.next().value;
        }

        @Override
        public void forEachRemaining(IntConsumer action) {
            while(it.hasNext()){
                action.accept(it.next().value);
            }
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public Integer next() {
            return it.next().value;
        }

        @Override
        public void remove() {
            it.remove(); 
        }
    }
    
    protected class Entry {
        private int value;
        private Entry left, right, parent;
        private boolean red = true;

        Entry() {
        }

        Entry(int value) {
            this.value = value;
        }

        Entry(int value, boolean isRed) {
            this.value = value;
            this.red = isRed;
        }

        Entry(int value, Entry parent) {
            this.value = value;
            this.parent = parent;
        }

        public int getValue() {
            return value;
        }

        public int setValue(int value) {
            int tmp = this.value;
            this.value = value;
            return tmp;
        }

        public boolean isRed() {
            return red;
        }
        
        public boolean isBlack(){
            return !red;
        }
        
        public Entry setRed(){
            red = true;
            return this;
        }
        
        public Entry setBlack(){
            red = false;
            return this;
        }

        public Entry getLeft() {
            return left;
        }

        public Entry setLeft(Entry left) {
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
        public Entry attachLeft(Entry left) {
            if(this.left!=null){
                this.left.parent = null;
            }
            this.left = left;
            if(left!=null){
                left.parent = this;
            }
            return this;
        }

        public Entry getRight() {
            return right;
        }

        public Entry setRight(Entry right) {
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
        public Entry attachRight(Entry right) {
            if(this.right!=null){
                this.right.parent = null;
            }
            this.right = right;
            if(right!=null){
                right.parent = this;
            }
            return this;
        }

        public Entry getParent() {
            return parent;
        }

        public Entry setParent(Entry parent) {
            this.parent = parent;
            return this;
        }
        
        public int compareTo(int otherValue){
            return IntTreeSet.this.comparator.compare(value, otherValue);
        }
        
        @Override
        public Entry clone(){
            return new Entry(this.value, this.red);
        }
    }
    
    protected class TreeIterable implements Iterable<Entry> {

        @Override
        public Iterator<Entry> iterator() {
            return new TreeIterator();
        }
        
    }
    
    protected class TreeIterator implements Iterator<Entry> {

        private int concurrentModificationMark;
        private Entry prev;
        private Entry next;
        
        TreeIterator(){
            this.concurrentModificationMark = IntTreeSet.this.concurrentModification;
            this.next = IntTreeSet.getSmallestEntry(root);
        }
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public Entry next() {
            if(next==null){
                throw new NoSuchElementException();
            }
            if(this.concurrentModificationMark!=IntTreeSet.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            prev = next;
            next = findNext(next);
            return prev;
        }

        @Override
        public void remove() {
            if(this.concurrentModificationMark!=IntTreeSet.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            if(prev!=null){
                Entry node = IntTreeSet.this.removeNode(prev);
                prev = null;
                this.concurrentModificationMark = IntTreeSet.this.concurrentModification;
                if(node!=null && next!=null && node.value==next.value){
                    next = node;
                }
            } else {
                throw new IllegalStateException();
            }
        }
        
        private Entry findNext(Entry node){
            if (node == null) {
                return null;
            } else if (node.getRight() != null) {
                Entry n = node.getRight();
                while (n.getLeft() != null) {
                    n = n.getLeft();
                }
                return n;
            } else {
                Entry p = node.getParent();
                Entry ch = node;
                while (p != null && ch == p.getRight()) {
                    ch = p;
                    p = p.getParent();
                }
                return p;
            }
        }
        
    }
    
}
