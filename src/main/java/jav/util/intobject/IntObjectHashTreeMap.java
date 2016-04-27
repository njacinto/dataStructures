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
package jav.util.intobject;

import jav.util.int_.IntSet;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

public class IntObjectHashTreeMap<V> implements IntObjectMap<V> {
    public static final int MAX_SIZE = (1<<30);
    public static final float DEFAULT_LOAD_FACTOR = 0.75f;
    //
    protected static final int DEFAULT_CAPACITY = 16;
    protected static final int MOD_COUNT_MASK = ~(-MAX_SIZE);
    //
    private static final int CONCURRENT_MODIFICATION_MASK = 2147483647;
    protected static final Comparator DEFAULT_COMPARATOR = new ComparableComparator();
    //
    protected final Iterable<IntObjectMap.Entry<V>> treeIterable = new TreeIterable();
    //
    @SuppressWarnings("unchecked")
    private Comparator<V> comparator = (Comparator<V>)DEFAULT_COMPARATOR;
    private int size = 0;
    private MapTreeNode root = null;
    private int concurrentModification = 0;
    //
    
//    protected final Iterable<Entry> ENTRY_ITERABLE = new HashIterable();
    //
    private MapTreeNode[] map;
    private int maxAllowed;
    private float loadFactor;
    private int mapIndexMask;

    public IntObjectHashTreeMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }
    
    public IntObjectHashTreeMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }
    
    @SuppressWarnings({"OverridableMethodCallInConstructor", "unchecked"})
    public IntObjectHashTreeMap(int capacity, float loadFactor) {
        map = (MapTreeNode[])Array.newInstance(MapTreeNode.class, mapLength(capacity, DEFAULT_CAPACITY));
        size = 0;
        mapIndexMask = ~(-map.length);
        setLoadFactor(loadFactor);
    }

    public float getLoadFactor() {
        return loadFactor;
    }

    public IntObjectHashTreeMap setLoadFactor(float loadFactor) {
        this.loadFactor = loadFactor<0 ? DEFAULT_LOAD_FACTOR : loadFactor;
        maxAllowed = ((int)(map.length*loadFactor+0.5));
        return this;
    }
    
    public Comparator<V> getComparator() {
        return comparator;
    }

    /**
     * The tree will be rebuilt using the new comparator
     * 
     * @param comparator 
     * @return  
     */
    public IntObjectHashTreeMap setComparator(Comparator<V> comparator) {
        if(comparator!=null && !this.comparator.equals(comparator)){
            this.comparator = comparator;
            MapTreeNode node = root, tmpNode;
            root = null;
            while(node!=null){
                if(node.left!=null){
                    node = node.left;
                } else if(node.right!=null){
                    node = node.right;
                } else {
                    tmpNode = node;
                    node = node.parent;
                    tmpNode.parent = null;
                    if(node!=null){
                        if(node.left==tmpNode){
                            node.left = null;
                        } else {
                            node.right = null;
                        }
                    }
                    //
                    insertNodeInTree(tmpNode);
                }
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
    public V get(int key) {
        MapTreeNode node = getNode(key);
        return node!=null && node.key==key ? node.value : null;
    }

    @Override
    public boolean containsKey(int key) {
        int index = key & mapIndexMask;
        MapTreeNode node = map[index];
        while(node!=null && node.key!=key){
            node = node.next;
        }
        return node!=null && node.key==key;
    }

    @Override
    public boolean containsValue(V value) {
        MapTreeNode node = getNodeOrParent(root, value);
        return node!=null && node.value==value;
    }

    @Override
    public V put(int key, V value) {
        return putNode(key, value);
    }

    @Override
    public V remove(int key) {
        int index = key & mapIndexMask;
        if(map[index]!=null){
            MapTreeNode entry = map[index];
            if(entry.key==key){
                V retVal = entry.value;
                map[index] = entry.next;
                removeNodeFromTree(entry);
                size--;
                incConcurrentModification();
                return retVal;
            } else {
                while(entry.next!=null){
                    if(entry.next.key==key){
                        V retVal = entry.next.value;
                        entry.next = entry.next.next;
                        removeNodeFromTree(entry);
                        size--;
                        incConcurrentModification();
                        return retVal;
                    }
                    entry = entry.next;
                }
            }
        }
        return null;
    }

    @Override
    public void putAll(IntObjectMap<V> c) {
        for(IntObjectMap.Entry<V> entry : c.entrySet()){
            putNode(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends V> c) {
        for(Map.Entry<? extends Integer,? extends V> entry : c.entrySet()){
            putNode(entry.getKey(), entry.getValue());
        }
    }

    @SuppressWarnings("unchecked")
    public V[] valuesToArray() {
        V[] ret = (V[])Array.newInstance(MapTreeNode.class, size);
        int i = 0;
        TreeIterator it = new TreeIterator(this.concurrentModification, getSmallestEntry(root));
        while(it.hasNext()){
            ret[i++] = it.next().value;
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public V[] valuesToArray(int fromIndex, int toIndex) {
        if(fromIndex<toIndex){
            int max = (toIndex<size ? toIndex : size);
            if(fromIndex<max){
                V[] ret = (V[])Array.newInstance(MapTreeNode.class, max-fromIndex);
                int i = 0, index=0;
                TreeIterator it = new TreeIterator(this.concurrentModification, getSmallestEntry(root));
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
    public Set<Entry<V>> entrySet() {
        return new IntObjectMapUtil.EntrySet<V>(treeIterable, this);
    }

    @Override
    public IntSet intKeySet() {
        return new IntObjectMapUtil.KeyIntObjectSet<V>(treeIterable, this);
    }

    @Override
    public Collection<V> values() {
        return new IntObjectMapUtil.ValueIntObjectCollection<>(treeIterable, this);
    }

    public Iterator<V> iteratorValues() {
        return new ValueIterator(new TreeIterator(this.concurrentModification, getSmallestEntry(root)));
    }   

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        TreeIterator it = new TreeIterator(this.concurrentModification, getSmallestEntry(root));
        MapTreeNode node;
        while(it.hasNext()){
            node = it.next();
            sb.append("[").append(node.value).append("]\n");
        }
        return sb.toString();
    }
    
    /**
     * 
     * @return 
     */
    public MapTreeNode cloneTree(){
        if(this.root==null){
            return null;
        } else {
            MapTreeNode rootTmp = this.root.clone();
            MapTreeNode newNavNode = rootTmp;
            MapTreeNode navNode = root;
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
    
    
    // private methods
    private void incConcurrentModification(){
        concurrentModification = (concurrentModification+1)&CONCURRENT_MODIFICATION_MASK;
    }
    
    public MapTreeNode getNode(long key) {
        int index = Long.hashCode(key) & mapIndexMask;
        MapTreeNode node = map[index];
        while(node!=null && node.key!=key){
            node = node.next;
        }
        return node;
    }
    
    /**
     * 
     * @param key
     * @param value
     * @return 
     */
    private V putNode(int key, V value) {
        resizeIfNecessary(size+1);
        int index = key & mapIndexMask;
        if(map[index]==null){
            insertNodeInTree(map[index] = new MapTreeNode(key, value));
            incConcurrentModification();
            size++;
        } else if(map[index].key==key){
            V ret = map[index].value;
            if(value!=map[index].value){ // check if value changed => requires changes on the tree
                incConcurrentModification();
                removeNodeFromTree(map[index]);
                map[index].value = value;
                insertNodeInTree(map[index]);
            }
            return ret;
        } else {
            MapTreeNode entry = map[index];
            while(entry.next!=null){
                if(entry.next.key==key){
                    V ret = entry.next.value;
                    if(value!=entry.next.value){ // check if value changed => requires changes on the tree
                        incConcurrentModification();
                        removeNodeFromTree(entry.next);
                        entry.next.value = value;
                        insertNodeInTree(entry.next);
                    }
                    return ret;
                }
                entry = entry.next;
            }
            insertNodeInTree(entry.next = new MapTreeNode(key, value));
            incConcurrentModification();
            size++;
        }
        return null;
    }
    
    private void insertNodeInTree(MapTreeNode node){
        if(this.root==null){
            root = node.setBlack();
        } else { 
            MapTreeNode parentOrNode = getNodeOrParent(root, node.value);
            if(parentOrNode!=null){
                node.parent = parentOrNode;
                if(comparator.compare(parentOrNode.value, node.value)  > 0){
                    parentOrNode.left = node;
                } else {
                    parentOrNode.right = node;
                }
                afterInsert(node);
            } else {
                throw new RuntimeException("Null parent");
            }
        }
    }
    
    /**
     * Reorganize the tree after insert (colors and rotations)
     * 
     * @param node 
     */
    private void afterInsert(MapTreeNode node){
        while(node.parent!=null && node.parent.parent!=null && node.parent.red){
            if(node.parent==node.parent.parent.left){
                MapTreeNode y = node.parent.parent.right;
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
                MapTreeNode y = node.parent.parent.left;
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
    private MapTreeNode removeNodeFromTree(MapTreeNode node){ 
        MapTreeNode toBeRemoved;
        MapTreeNode sibling;
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
            //remove toBeRemoved from map
            removeFromMap(toBeRemoved);
            node.key = toBeRemoved.key;
            node.value = toBeRemoved.value;
            //add node to map
            addToMap(node);
        }
        if(!toBeRemoved.red && root!=null){
            afterRemove(sibling!=null ? sibling : toBeRemoved);
        }
        toBeRemoved.parent = toBeRemoved.left = toBeRemoved.right = null;
        return node;
    }
    
    private void removeFromMap(MapTreeNode node) {
        int index = Long.hashCode(node.key) & mapIndexMask;
        if(map[index]==node){
            map[index] = node.next;
        } else if(map[index]!=null){
            MapTreeNode entry = map[index];
            while(entry.next!=null){
                if(entry.next==node){
                    entry.next = entry.next.next;
                    break;
                }
                entry = entry.next;
            }
        }
    }
    
    private void addToMap(MapTreeNode node) {
        int index = Long.hashCode(node.key) & mapIndexMask;
        if(map[index]==null){
            map[index] = node;
        } else {
            MapTreeNode entry = map[index];
            while(entry.next!=null){
                entry = entry.next;
            }
            entry.next = node;
        }
    }
    
    /**
     * Reorganize the tree after removing an element (colors and rotations)
     * @param node 
     */
    private void afterRemove(MapTreeNode node){
        while(node!=root && !node.red){
            if(node==node.parent.left || (node.parent.left==null && node!=node.parent.right)){
                MapTreeNode p = node.parent.right;
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
                MapTreeNode p = node.parent.left;
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
    
    private MapTreeNode getSuccessor(MapTreeNode node) {
        if (node == null) {
            return null;
        } else if (node.right != null) {
            return getSmallestEntry(node.right);
        } else {
            MapTreeNode curr = node.parent;
            MapTreeNode prev = node;
            while (curr != null && prev == curr.right) {
                prev = curr;
                curr = curr.parent;
            }
            return curr;
        }
    }
    
    //
    private MapTreeNode getSmallestEntry(MapTreeNode node){
        if(node!=null){
            while(node.left !=null){
                node = node.left;
            }
            return node;
        }
        return null;
    }
    
    private MapTreeNode getNodeOrParent(MapTreeNode root, V value){
        MapTreeNode node = root;
        while(node !=null){
            if(comparator.compare(node.value, value) > 0){
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
    
    private void rotateLeft(MapTreeNode node){
        MapTreeNode p1 = node.right;
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
    
    private void rotateRight(MapTreeNode node){
        MapTreeNode p1 = node.left;
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
    
    // HashMap
    @SuppressWarnings("unchecked")
    private void resizeIfNecessary(int newSize){
        if(newSize>maxAllowed){
            incConcurrentModification();
            MapTreeNode[] currMap = this.map;
            final int currMapLen = currMap.length;
            int mapLength = mapLength(newSize, currMapLen);
            int index;
            int newMapMask = ~(-mapLength);
            MapTreeNode tmpEntry;
            MapTreeNode[] newMap = (MapTreeNode[])Array.newInstance(MapTreeNode.class, mapLength);
            for(MapTreeNode entry : currMap){
                while(entry!=null){
                    index = Long.hashCode(entry.key) & newMapMask;
                    if(newMap[index]==null){
                        newMap[index] = entry;
                        entry = entry.next;
                        newMap[index].next = null;
                    } else {
                        tmpEntry = newMap[index];
                        while(tmpEntry.next!=null){
                            tmpEntry = tmpEntry.next;
                        }
                        tmpEntry.next = entry;
                        entry = entry.next;
                        tmpEntry.next.next = null;
                    }
                }
            }
            this.map = newMap;
            this.mapIndexMask = newMapMask;
            this.maxAllowed = ((int)(mapLength*loadFactor+0.5));
        }
    }
    
    private static int mapLength(int newSize, int currMapLen){
        if(newSize>MAX_SIZE){
            return MAX_SIZE;
        } else if(newSize<DEFAULT_CAPACITY){
            return DEFAULT_CAPACITY;
        } else {
            if(newSize<(currMapLen<<1)){
                return currMapLen<<1;
            } else {
                int mapLength = currMapLen;
                while(mapLength < newSize){
                    mapLength <<= 1;
                }
                return mapLength;
            }
        }
    }
    
    // classes
    
    protected class ValueIterator implements Iterator<V> {

        private TreeIterator it;

        public ValueIterator(TreeIterator it) {
            this.it = it;
        }

        @Override
        public void forEachRemaining(Consumer<? super V> action) {
            while(it.hasNext()){
                action.accept(it.next().value);
            }
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public V next() {
            return it.next().value;
        }

        @Override
        public void remove() {
            it.remove(); 
        }
    }
    
    protected class MapTreeNode implements IntObjectMap.Entry<V> {
        private int key;
        private V value;
        private MapTreeNode left=null, right=null, parent=null, next=null;
        private boolean red = true;

        MapTreeNode() {
        }

        MapTreeNode(int key, V value) {
            this.key = key;
            this.value = value;
        }

        MapTreeNode(int key, V value, boolean isRed) {
            this.key = key;
            this.value = value;
            this.red = isRed;
        }

        MapTreeNode(int key, V value, MapTreeNode parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public int getKey() {
            return key;
        }
        
        @Override
        public V getValue() {
            return value;
        }

        /**
         * This operation is not supported because it would affect the integrity
         * of the tree.
         * To change the value use the IntObjectHashTreeMap.put operation.
         * 
         * @param value
         * @return 
         * @throws UnsupportedOperationException always.
         */
        @Override
        public V setValue(V value) {
            throw new UnsupportedOperationException("Value cannot be set directly.");
//            V tmp = this.value;
//            this.value = value;
//            return tmp;
        }

        public boolean isRed() {
            return red;
        }
        
        public boolean isBlack(){
            return !red;
        }
        
        public MapTreeNode setRed(){
            red = true;
            return this;
        }
        
        public MapTreeNode setBlack(){
            red = false;
            return this;
        }

        public MapTreeNode getNext() {
            return next;
        }

        public MapTreeNode setNext(MapTreeNode next) {
            this.next = next;
            return this;
        }

        public MapTreeNode getLeft() {
            return left;
        }

        public MapTreeNode setLeft(MapTreeNode left) {
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
        public MapTreeNode attachLeft(MapTreeNode left) {
            if(this.left!=null){
                this.left.parent = null;
            }
            this.left = left;
            if(left!=null){
                left.parent = this;
            }
            return this;
        }

        public MapTreeNode getRight() {
            return right;
        }

        public MapTreeNode setRight(MapTreeNode right) {
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
        public MapTreeNode attachRight(MapTreeNode right) {
            if(this.right!=null){
                this.right.parent = null;
            }
            this.right = right;
            if(right!=null){
                right.parent = this;
            }
            return this;
        }

        public MapTreeNode getParent() {
            return parent;
        }

        public MapTreeNode setParent(MapTreeNode parent) {
            this.parent = parent;
            return this;
        }
        
        public int compareTo(V otherValue){
            return IntObjectHashTreeMap.this.comparator.compare(value, otherValue);
        }
        
        @Override
        @SuppressWarnings("CloneDeclaresCloneNotSupported")
        public MapTreeNode clone(){
            return new MapTreeNode(this.key, this.value, this.red);
        }
    }
    
    protected class TreeIterable implements Iterable<IntObjectMap.Entry<V>> {

        @Override
        public Iterator<IntObjectMap.Entry<V>> iterator() {
            return new TreeIterator(IntObjectHashTreeMap.this.concurrentModification, getSmallestEntry(root));
        }
        
    }
    
    protected class TreeIterator implements Iterator<IntObjectMap.Entry<V>> {

        private int concurrentModificationMark;
        private MapTreeNode prev;
        private MapTreeNode next;
        
        TreeIterator(int concurrentModification, MapTreeNode start){
            this.concurrentModificationMark = concurrentModification;
            this.next = start;
        }
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public MapTreeNode next() {
            if(next==null){
                throw new NoSuchElementException();
            }
            if(this.concurrentModificationMark!=IntObjectHashTreeMap.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            prev = next;
            next = findNext(next);
            return prev;
        }

        @Override
        public void remove() {
            if(this.concurrentModificationMark!=IntObjectHashTreeMap.this.concurrentModification){
                throw new ConcurrentModificationException();
            }
            if(prev!=null){
                MapTreeNode node = IntObjectHashTreeMap.this.removeNodeFromTree(prev);
                prev = null;
                this.concurrentModificationMark = IntObjectHashTreeMap.this.concurrentModification;
                if(node!=null && next!=null && node.value==next.value){
                    next = node;
                }
            } else {
                throw new IllegalStateException();
            }
        }
        
        private MapTreeNode findNext(MapTreeNode node){
            if (node == null) {
                return null;
            } else if (node.getRight() != null) {
                MapTreeNode n = node.getRight();
                while (n.getLeft() != null) {
                    n = n.getLeft();
                }
                return n;
            } else {
                MapTreeNode p = node.getParent();
                MapTreeNode ch = node;
                while (p != null && ch == p.getRight()) {
                    ch = p;
                    p = p.getParent();
                }
                return p;
            }
        }
        
    }
    
    public static class ComparableComparator<T extends Comparable> implements Comparator<T> {

        @Override
        @SuppressWarnings("unchecked")
        public int compare(T o1, T o2) {
            return o1==null ? o2==null ? 0 : -1 : o2==null? 1 : o1.compareTo(o2);
        }
        
    }
}
