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
import jav.util.RBTreeUtil.TestTreeNode;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LongTreeMapTest extends LongMapTest{

    @Override
    protected LongMap map() {
        return new LongTreeMap();
    }
    
    public LongTreeMapTest() {
    }
    
    @Test
    public void testDepth(){
        LongTreeMapTestAux instance = new LongTreeMapTestAux();
        //
        //System.out.println(instance.toStringByLevels());
        assertEquals(0, instance.getMaxDepth());
        //
        instance.put(1, 1);
        //System.out.println(instance.toStringByLevels());
        assertEquals(1, instance.getMaxDepth());
        //
        for(int i=2; i<=3; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(2, instance.getMaxDepth());
        //
        for(int i=4; i<=5; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(3, instance.getMaxDepth());
        //
        for(int i=10; i>5; i--){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(5, instance.getMaxDepth());
        for(int i=11; i<=50; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertEquals(9, instance.getMaxDepth());
    }
    
    @Test
    public void testIntegraty(){
        LongTreeMapTestAux instance = new LongTreeMapTestAux();
        //
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        instance.put(1, 1);
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(int i=2; i<=3; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(int i=4; i<=5; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
        //
        for(int i=10; i>5; i--){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        assertTrue(instance.checkIntegrity());
    }
    
    @Test
    public void testDelete_Root(){
        RBTreeTestCases.DeleteTestCaseData data = RBTreeTestCases.DELETE_ROOT;
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
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
        testDeleteNode(new LongTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 100, 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testLevels(){
        LongTreeMapTestAux instance = new LongTreeMapTestAux();
        for(int i=1; i<=52; i++){
            instance.put(i, i);
        }
//        System.out.println(instance.toStringByLevels());
        TestTreeNode[][] expected = new TestTreeNode[][] {
            { new TestTreeNode(16, null, false) },
            { new TestTreeNode(8, 16, false), new TestTreeNode(24, 16, false) },
            { new TestTreeNode(4, 8, false), new TestTreeNode(12, 8, false), 
                new TestTreeNode(20, 24, false), new TestTreeNode(32, 24, true) },
            { new TestTreeNode(2, 4, false), new TestTreeNode(6, 4, false), 
                new TestTreeNode(10, 12, false), new TestTreeNode(14, 12, false), 
                new TestTreeNode(18, 20, false), new TestTreeNode(22, 20, false), 
                new TestTreeNode(28, 32, false), new TestTreeNode(40, 32, false) },
            { new TestTreeNode(1, 2, false), new TestTreeNode(3, 2, false), 
                new TestTreeNode(5, 6, false), new TestTreeNode(7, 6, false), 
                new TestTreeNode(9, 10, false), new TestTreeNode(11, 10, false), 
                new TestTreeNode(13, 14, false), new TestTreeNode(15, 14, false), 
                new TestTreeNode(17, 18, false), new TestTreeNode(19, 18, false), 
                new TestTreeNode(21, 22, false), new TestTreeNode(23, 22, false), 
                new TestTreeNode(26, 28, false), new TestTreeNode(30, 28, false), 
                new TestTreeNode(36, 40, true), new TestTreeNode(44, 40, true) },
            { new TestTreeNode(25, 26, false), new TestTreeNode(27, 26, false), 
                new TestTreeNode(29, 30, false), new TestTreeNode(31, 30, false), 
                new TestTreeNode(34, 36, false), new TestTreeNode(38, 36, false), 
                new TestTreeNode(42, 44, false), new TestTreeNode(48, 44, false) },
            { new TestTreeNode(33, 34, false), new TestTreeNode(35, 34, false), 
                new TestTreeNode(37, 38, false), new TestTreeNode(39, 38, false), 
                new TestTreeNode(41, 42, false), new TestTreeNode(43, 42, false), 
                new TestTreeNode(46, 48, true), new TestTreeNode(50, 48, true) },
            { new TestTreeNode(45, 46, false), new TestTreeNode(47, 46, false), 
                new TestTreeNode(49, 50, false), new TestTreeNode(51, 50, false) },
            { new TestTreeNode(52, 51, true) }
        };
        assertTrue(instance.checkLevels(expected));
        assertTrue(instance.checkIntegrity());
    }
    
    @Test
    public void testDelete_Random(){
        LongTreeMapTestAux instance = new LongTreeMapTestAux();
        for(int i=1; i<=100; i++){
            instance.put(i, i);
        }
        //System.out.println(instance.toStringByLevels());
        for(int i=1; i<=100; i++){
            assertEquals(i, instance.get(i));
        }
        instance.checkIntegrity();
        int addRefNum = 101;
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
        for(int i=1; i<addRefNum; i++){
            if(i==deleted && i<100){
                deleted+=3;
            } else {
                assertEquals(i, instance.get(i));
            }
        }
    }
    
    private static void testDeleteNode(LongTreeMapTestAux instance, long removeKey, 
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
        assertEquals(instance.getNull(), instance.get(removeKey));
        assertTrue(instance.checkLevels(expectedAfterDelete));
        for(long i=startValue; i<=endValue; i++){
            if(i!=removeKey){
                assertEquals(i*valueFactor, instance.get(i));
            }
        }
        assertTrue(instance.checkIntegrity());
    }
}
