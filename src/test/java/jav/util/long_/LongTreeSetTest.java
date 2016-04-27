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

import jav.util.RBTreeTestCases;
import org.junit.Test;
import jav.util.RBTreeUtil.TestTreeNode;
import java.util.PrimitiveIterator;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LongTreeSetTest extends LongSetTest {
    
    
    @Override
    protected LongSet collection(){
        return new LongTreeSet();
    }
    
    @Override
    protected LongSet collection(long ... elements){
        return new LongTreeSet(elements);
    }
    
    public LongTreeSetTest() {
    }
    
    @Test
    public void testDepth(){
        LongTreeSetTestAux instance = new LongTreeSetTestAux();
        //
        //System.out.println(instance.toStringByLevels());
        assertEquals(0, instance.getMaxDepth());
        //
        instance.add(1);
        //System.out.println(instance.toStringByLevels());
        assertEquals(1, instance.getMaxDepth());
        //
        for(int i=2; i<=3; i++){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(2, instance.getMaxDepth());
        //
        for(int i=4; i<=5; i++){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(3, instance.getMaxDepth());
        //
        for(int i=10; i>5; i--){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(5, instance.getMaxDepth());
        for(int i=11; i<=50; i++){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(9, instance.getMaxDepth());
    }
    
    @Test
    public void testIntegraty(){
        LongTreeSetTestAux instance = new LongTreeSetTestAux();
        //
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        instance.add(1);
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(int i=2; i<=3; i++){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(int i=4; i<=5; i++){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(int i=10; i>5; i--){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
    }
    
    @Test
    public void testDelete_Root(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_RootWithChildren(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT_WITH_CHILDREN;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_RootLeftChildren(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT_LEFT_CHILDREN;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_RootRightChildren(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT_RIGHT_CHILDREN;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
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
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_leafRight(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_LEAF_RIGHT;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_leafRightRed(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_LEAF_RIGHT_RED;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeBlack(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_BLACK;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeRed(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_RED;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeBlackWithRedChild(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_BLACK_WITH_RED_CHILD;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_nodeBlackWithRedChilds(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_NODE_BLACK_WITH_RED_CHILDS;
        testDeleteNode(new LongTreeSetTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(),  
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_Random(){
        LongTreeSetTestAux instance = new LongTreeSetTestAux();
        for(long i=1; i<=100; i++){
            instance.add(i);
        }
        //System.out.println(instance.toStringByLevels());
        PrimitiveIterator.OfLong it = instance.iterator();
        for(long i=1; i<=100 && it.hasNext(); i++){
            assertEquals(i, it.nextLong());
        }
        instance.checkIntegrity();
        int addRefNum = 101;
        for(long j=2; j<100; j+=3){
            instance.remove(j);
            //System.out.println(instance.toStringByLevels());
            instance.checkIntegrity();
            for(int i=1; i<=3; i++){
                instance.add(addRefNum);
                addRefNum++;
            }
            instance.checkIntegrity();
        }
        long deleted = 2;
        for(long i=1; i<addRefNum; i++){
            if(i==deleted && i<100){
                deleted+=3;
            } else {
                assertTrue(instance.contains(i));
            }
        }
    }
    
    private static void testDeleteNode(LongTreeSetTestAux instance, long removeKey, 
            long startValue, long endValue, 
            TestTreeNode[][] expectedAfterBuild, TestTreeNode[][] expectedAfterDelete, 
            boolean splitInsert, boolean printOut){
        if(splitInsert){
            long middle = (long)(((endValue-startValue)/2.0)+0.5);
            for(long i=startValue; i<=middle; i++){
                instance.add(i);
            }
            for(long i=endValue; i>middle; i--){
                instance.add(i);
            }
        } else {
            for(long i=startValue; i<=endValue; i++){
                instance.add(i);
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
                assertTrue(instance.contains(i));
            }
        }
        assertTrue(instance.checkIntegrity());
    }
}
