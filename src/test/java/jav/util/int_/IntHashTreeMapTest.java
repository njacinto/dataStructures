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

import jav.util.RBTreeTestCases;
import jav.util.RBTreeUtil.TestTreeNode;
import java.util.PrimitiveIterator;
import org.junit.Test;
import static org.junit.Assert.*;
import static junit.framework.Assert.assertEquals;

public class IntHashTreeMapTest extends IntMapTest {
    
    @Override
    protected IntMap map() {
        return new IntHashTreeMap();
    }
    
    public IntHashTreeMapTest() {
    }
    
    @Test
    public void testResize(){
        IntHashTreeMap instance = new IntHashTreeMap(16);
        for(int i=0; i<28; i++){
            instance.put(8<<i, i);
            //System.out.println((1<<i)+"<>"+i);
        }
        for(int i=0; i<28; i++){
            assertEquals(i, instance.get(8<<i));
        }
    }
    
    @Test
    public void testOrder(){
        IntHashTreeMap instance = new IntHashTreeMap(16);
        for(int i=10; i>-11; i--){
            instance.put(i, i);
        }
        IntCollection collection = instance.intValues();
        PrimitiveIterator.OfInt it = collection.iterator();
        long expected = -10;
        while(it.hasNext()){
            assertEquals(expected++, it.nextInt());
        }
    }
    
    @Test
    public void testDuplicatedValues(){
        long limit = 100;
        IntHashTreeMapTestAux instance = new IntHashTreeMapTestAux();
        for(int i=0; i<limit; i++){
            instance.put(i, i&65532);
        }
        //System.out.println(instance.toStringByLevels());
        IntCollection collection = instance.intValues();
        PrimitiveIterator.OfInt it = collection.iterator();
        long expected = 0;
        while(it.hasNext()){
            assertEquals(expected&65532, it.nextInt());
            expected++;
        }
        assertEquals(limit, expected);
    }
    
    // Tree Test
    @Test
    public void testDepth(){
        IntHashTreeMapTestAux instance = new IntHashTreeMapTestAux();
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
        IntHashTreeMapTestAux instance = new IntHashTreeMapTestAux();
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
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
        testDeleteNode(new IntTreeMapTestAux(), 
                data.getRemoveValue(),
                data.getStartValue(),
                data.getEndValue(), 
                data.getExpectedAfterBuild(),
                data.getExpectedAfterDelete(),
                data.isSplitInsert(), false);
    }
    
    @Test
    public void testDelete_Random(){
        IntHashTreeMapTestAux instance = new IntHashTreeMapTestAux();
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
    
    
    private static void testDeleteNode(IntTreeMapTestAux instance, int removeKey, 
            int startValue, int endValue, 
            TestTreeNode[][] expectedAfterBuild, TestTreeNode[][] expectedAfterDelete, 
            boolean splitInsert, boolean printOut){
        if(splitInsert){
            int middle = (int)(((endValue-startValue)/2.0)+0.5);
            for(int i=startValue; i<=middle; i++){
                instance.put(i, i);
            }
            for(int i=endValue; i>middle; i--){
                instance.put(i, i);
            }
        } else {
            for(int i=startValue; i<=endValue; i++){
                instance.put(i, i);
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
        for(int i=startValue; i<=endValue; i++){
            if(i!=removeKey){
                assertEquals(i, instance.get(i));
            }
        }
        assertTrue(instance.checkIntegrity());
    }
}
