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

import jav.util.RBTreeUtil;

public class LongObjectTreeMapTestAux extends LongObjectTreeMap<Long> {
    private static final TreeNodeAdapter NODE_ADAPTER = new TreeNodeAdapter();
    
    public int getMaxDepth(){
        return RBTreeUtil.getMaxDepth(super.cloneTree(), NODE_ADAPTER);
    }
    
    public boolean checkIntegrity(){
        return RBTreeUtil.checkIntegrity(super.cloneTree(), NODE_ADAPTER);
    }
    
    public boolean checkLevels(RBTreeUtil.TestTreeNode [][]levels){
        return RBTreeUtil.checkLevels(super.cloneTree(), NODE_ADAPTER, levels);
    }
    
    public String toStringByLevels(){
        return RBTreeUtil.toStringByLevels(super.cloneTree(), NODE_ADAPTER);
    }
    
    
    private static class TreeNodeAdapter implements RBTreeUtil.NodeAdapter {

        @Override
        public Object getParent(Object node) {
            return ((TreeNode)node).getParent();
        }

        @Override
        public Object getRight(Object node) {
            return ((TreeNode)node).getRight();
        }

        @Override
        public Object getLeft(Object node) {
            return ((TreeNode)node).getLeft();
        }

        @Override
        public Object getValue(Object node) {
            return ((TreeNode)node).getKey();
        }

        @Override
        public boolean isRed(Object node) {
            return ((TreeNode)node).isRed();
        }

        @Override
        public boolean isBlack(Object node) {
            return ((TreeNode)node).isBlack();
        }
        
        @Override
        public boolean valueEquals(Object node, Object value){
            return (((TreeNode)node)).getKey()==((Number)value).longValue();
        }
        
        @Override
        public int valueCompare(Object node, Object value){
            return (((TreeNode)node)).getKey()<((Number)value).longValue()? -1 : 
                    (((TreeNode)node)).getKey()>((Number)value).longValue()? 1 : 0;
        }
    }
}
