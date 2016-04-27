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

import jav.util.RBTreeTestCases;
import jav.util.RBTreeUtil.TestTreeNode;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LongObjectTreeMapTest extends LongObjectMapTest{

    @Override
    protected LongObjectMap<Long> map() {
        return new LongObjectTreeMap<Long>();
    }
    
    public LongObjectTreeMapTest() {
    }
    
    @Test
    public void testDepth(){
        LongObjectTreeMapTestAux instance = new LongObjectTreeMapTestAux();
        //
        //System.out.println(instance.toStringByLevels());
        assertEquals(0, instance.getMaxDepth());
        //
        instance.put(1l, 1l);
        //System.out.println(instance.toStringByLevels());
        assertEquals(1, instance.getMaxDepth());
        //
        for(long i=2; i<=3; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(2, instance.getMaxDepth());
        //
        for(long i=4; i<=5; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(3, instance.getMaxDepth());
        //
        for(long i=10; i>5; i--){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(5, instance.getMaxDepth());
        for(long i=11; i<=50; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(9, instance.getMaxDepth());
    }
    
    @Test
    public void testIntegraty(){
        LongObjectTreeMapTestAux instance = new LongObjectTreeMapTestAux();
        //
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        instance.put(1l, 1l);
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(long i=2; i<=3; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(long i=4; i<=5; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(long i=10; i>5; i--){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
    }
    
    @Test
    public void testDelete_Root(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_RootWithChildren(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT_WITH_CHILDREN;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_RootLeftChildren(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT_LEFT_CHILDREN;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_RootRightChildren(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT_RIGHT_CHILDREN;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    /**
     * Delete left leaf 
     */
    @Test
    public void testDelete_leafLeft(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_LEAF_LEFT;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_leafRight(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_LEAF_RIGHT;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_leafRightRed(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_LEAF_RIGHT_RED;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeBlack(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_BLACK;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeRed(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_RED;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeBlackWithRedChild(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_BLACK_WITH_RED_CHILD;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeBlackWithRedChilds(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_BLACK_WITH_RED_CHILDS;
        testDeleteNode(new LongObjectTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_Random(){
        LongObjectTreeMapTestAux instance = new LongObjectTreeMapTestAux();
        for(long i=1; i<=100; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        for(long i=1; i<=100; i++){
            assertEquals(i, instance.get(i).longValue());
        }
        instance.checkIntegrity();
        long addRefNum = 101;
        for(int j=2; j<100; j+=3){
            instance.remove(j);
            //System.out.println(instance.toStringByLevels());
            instance.checkIntegrity();
            for(int i=1; i<=3; i++){
                instance.put(addRefNum, addRefNum);
                addRefNum++;
            }
            instance.checkIntegrity();
        }
        int deleted = 2;
        for(long i=1; i<addRefNum; i++){
            if(i==deleted && i<100){
                deleted+=3;
            } else {
                assertEquals(i, instance.get(i).longValue());
            }
        }
    }
    
    private static void testDeleteNode(LongObjectTreeMapTestAux instance, long removeKey, 
            long startValue, long endValue, int valueFactor,
            TestTreeNode[][] expectedAfterBuild, TestTreeNode[][] expectedAfterDelete, 
            boolean splitInsert, boolean printOut){
        if(splitInsert){
            long middle = (long)(((endValue-startValue)/2.0)+0.5);
            for(long i=startValue; i<=middle; i++){
                instance.put(i, i*valueFactor);
            }
            for(long i=endValue; i>middle; i--){
                instance.put(i, i*valueFactor);
            }
        } else {
            for(long i=startValue; i<=endValue; i++){
                instance.put(i, i*valueFactor);
            }
        }
        if(printOut){
            System.out.println(instance.toStringByLevels());
        }
        assertTrue(instance.checkLevels(expectedAfterBuild));
        //
        instance.remove(removeKey);
        if(printOut){
            System.out.println(instance.toStringByLevels());
        }
        assertTrue(instance.checkLevels(expectedAfterDelete));
        for(long i=startValue; i<=endValue; i++){
            if(i!=removeKey){
                assertEquals(i*valueFactor, instance.get(i).longValue());
            }
        }
        assertTrue(instance.checkIntegrity());
    }
}
