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
package jav.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public abstract class RBTreeUtil {
    private static final String ENTRY_FORMAT = "[%-4s/%-4s/%s]";

    private RBTreeUtil() {
    }
    
    public static int getMaxDepth(Object root, NodeAdapter adapter){
        int maxDepth = 0;
        if(root!=null){
            TestTreeIterator it = new TestTreeIterator(root, adapter);
            while(it.hasNext()){
                it.next();
                if(it.getDepth()>maxDepth){
                    maxDepth = it.getDepth();
                }
            }
        }
        return maxDepth;
    }
    
    
    public static boolean checkIntegrity(Object root, NodeAdapter adapter){
        if(root!=null){
            if(adapter.isRed(root)){ // root node is red
                return false;
            }
            Iterator<Object> it = new TestTreeIterator(root, adapter);
            Object node;
            int totalBN;
            while(it.hasNext()){
                node = it.next();
                if((adapter.isRed(node) && adapter.getParent(node)!=null && adapter.isRed(adapter.getParent(node))) // red node with red parent
                        /*|| !checkBlackNodesInPath(node, adapter)*/){  // number of black nodes doesn't match
                    return false;
                }
                if(checkBlackNodesInPath(node, adapter)<0){
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean checkLevels(Object root, NodeAdapter adapter, TestTreeNode [][]levels){
        if(root==null){
            return levels==null || levels.length==0;
        } else {
            TreeMap<Integer,List<Object>> map = new TreeMap<Integer,List<Object>>();
            TestTreeIterator it = new TestTreeIterator(root, adapter);
            Object treeNode;
            while(it.hasNext()){
                treeNode = it.next();
                List<Object> list = map.get(it.getDepth());
                if(list==null){
                    list = new ArrayList<Object>();
                    map.put(it.getDepth(), list);
                }
                list.add(treeNode);
            }
            boolean found;
            if(levels.length==map.size()){
                List<Object> list;
                for(int i=0; i<levels.length; i++){
                    list = map.get(i+1);
                    if(list!=null && list.size()==levels[i].length){
                        for(Object node : list){
                            found = false;
                            for(TestTreeNode testNode : levels[i]){
                                if(adapter.valueEquals(node, testNode.key) && adapter.isRed(node)==testNode.red && 
                                        ((adapter.getParent(node)==null && testNode.getParentValue()==null) ||
                                         (adapter.getParent(node)!=null && adapter.isRed(node)==testNode.red))){
                                    found = true;
                                    break;
                                }
                            }
                            if(!found){
                                return false;
                            }
                        }
                    } else {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }
    
    public static String toStringByLevels(Object root, NodeAdapter adapter){
        if(root==null){
            return "0 => Empty Map\n";
        } else {
            TreeMap<Integer,List<Object>> map = new TreeMap<Integer,List<Object>>();
            TestTreeIterator it = new TestTreeIterator(root, adapter);
            Object treeNode;
            while(it.hasNext()){
                treeNode = it.next();
                List<Object> list = map.get(it.getDepth());
                if(list==null){
                    list = new ArrayList<Object>();
                    map.put(it.getDepth(), list);
                }
                list.add(treeNode);
            }
            int i = 1;
            StringBuilder sb = new StringBuilder();
            for(List<Object> list : map.values()){
                sb.append(i++).append("=> ");
                for(Object treeEntry : list){
                    sb.append(String.format(ENTRY_FORMAT, adapter.getValue(treeEntry), 
                        ((adapter.getParent(treeEntry)!=null)? adapter.getValue(adapter.getParent(treeEntry)) : ""), 
                        (adapter.isRed(treeEntry)? "R" : "B")));
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }
    
    private static int checkBlackNodesInPath(Object root, NodeAdapter adapter){
        int totalBN = -1;
        int bNCount = adapter.isBlack(root)? 1 : 0;
        Object rootParent = adapter.getParent(root);
        Object node = root;
        //get smaller and count black
        while(adapter.getLeft(node) !=null){
            node = adapter.getLeft(node);
            if(adapter.isBlack(node)){
                bNCount++;
            }
        }
        while(node!=null && node!=rootParent){
            if(adapter.getLeft(node)==null && adapter.getRight(node)==null){
                if(totalBN==-1){
                    totalBN = bNCount;
                } else if(bNCount!=totalBN){
                    return -1;
                }
            }
            if (adapter.getRight(node) != null) {
                node = adapter.getRight(node);
                if(adapter.isBlack(node)){
                    bNCount++;
                }
                while (adapter.getLeft(node) != null) {
                    node = adapter.getLeft(node);
                    if(adapter.isBlack(node)){
                        bNCount++;
                    }
                }
            } else {
                Object ch;
                do {
                    ch = node;
                    node = adapter.getParent(node);
                    if(adapter.isBlack(ch)){
                        bNCount--;
                    }
                } while (node != null && ch == adapter.getRight(node) && node!=rootParent);
            }
        }
        return totalBN;
    }
    
    //
    public static interface NodeAdapter {
        Object getParent(Object node);
        Object getRight(Object node);
        Object getLeft(Object node);
        Object getValue(Object node);
        boolean isRed(Object node);
        boolean isBlack(Object node);
        boolean valueEquals(Object node, Object value);
        int valueCompare(Object node, Object value);
    }
    
    public static class TestTreeNode {
        private Object key = null;
        private Object parentKey = null;
        private boolean red = false;

        public TestTreeNode() {
        }

        public TestTreeNode(Object key, Object parentKey, boolean red) {
            this.key = key;
            this.red = red;
            this.parentKey = parentKey;
        }

        public Object getValue() {
            return key;
        }

        public void setValue(int key) {
            this.key = key;
        }

        public void setParentValue(int parentKey) {
            this.parentKey = parentKey;
        }

        public Object getParentValue() {
            return parentKey;
        }

        public boolean isRed() {
            return red;
        }

        public boolean isBlack() {
            return !red;
        }

        public void setRed(boolean red) {
            this.red = red;
        }

        public void setRed() {
            this.red = true;
        }

        public void setBlack() {
            this.red = false;
        }
        
    }
    
    private static class TestTreeIterator implements Iterator<Object> {

        private Object next;
        private int depth = 1;
        private int prevDepth = 0;
        private NodeAdapter adapter;
        
        TestTreeIterator(Object root, NodeAdapter adapter){
            this.adapter = adapter;
            this.next = getSmallestEntry(root);
        }
        
        @Override
        public boolean hasNext() {
            return next!=null;
        }

        @Override
        public Object next() {
            if(next==null){
                throw new NoSuchElementException();
            }
            Object ret = next;
            prevDepth = depth;
            next = findNext(next);
            return ret;
        }
        
        public int getDepth(){
            return prevDepth;
        }
        
        private Object getSmallestEntry(Object node){
            if(node!=null){
                while(adapter.getLeft(node) !=null){
                    depth++;
                    node = adapter.getLeft(node);
                }
                return node;
            }
            return null;
        }
        
        private Object findNext(Object node){
            if (node == null) {
                return null;
            } else if (adapter.getRight(node) != null) {
                depth++;
                Object n = adapter.getRight(node);
                while (adapter.getLeft(n) != null) {
                    depth++;
                    n = adapter.getLeft(n);
                }
                return n;
            } else {
                depth--;
                Object p = adapter.getParent(node);
                Object ch = node;
                while (p != null && ch == adapter.getRight(p)) {
                    depth--;
                    ch = p;
                    p = adapter.getParent(p);
                }
                return p;
            }
        }
    }
    
}
