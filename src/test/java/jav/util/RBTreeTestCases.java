/*
 * Copyright (C) 2015 njacinto.
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

import jav.util.RBTreeUtil.TestTreeNode;

/**
 *
 * @author njacinto
 */
public class RBTreeTestCases {
    public static final DeleteTestCaseData DELETE_ROOT
            = new DeleteTestCaseData(4, 4, 4, false,
                    new TestTreeNode[][]{
                        { blackNode(4, null) }
                    }, new TestTreeNode[][]{
                    });
    
    public static final DeleteTestCaseData DELETE_ROOT_WITH_CHILDREN
            = new DeleteTestCaseData(2, 1, 3, false,
                    new TestTreeNode[][]{
                        { blackNode(2, null) },
                        { redNode(1, 2), redNode(3, 2) }
                    }, new TestTreeNode[][]{
                        { blackNode(3, null) },
                        { redNode(1, 3) }
                    });

    public static final DeleteTestCaseData DELETE_ROOT_LEFT_CHILDREN
            = new DeleteTestCaseData(1, 1, 3, false,
                    new TestTreeNode[][]{
                        { blackNode(2, null) },
                        { redNode(1, 2), redNode(3, 2) }
                    }, new TestTreeNode[][]{
                        { blackNode(2, null) },
                        { redNode(3, 2) }
                    });

    public static final DeleteTestCaseData DELETE_ROOT_RIGHT_CHILDREN
            = new DeleteTestCaseData(3, 1, 3, false,
                    new TestTreeNode[][]{
                        { blackNode(2, null) },
                        { redNode(1, 2), redNode(3, 2) }
                    }, new TestTreeNode[][]{
                        { blackNode(2, null) },
                        { redNode(1, 2) }
                    });

    public static final DeleteTestCaseData DELETE_LEAF_LEFT
            = new DeleteTestCaseData(1, 1, 10, true,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) },
                        { redNode(6, 5) }
                    }, new TestTreeNode[][]{
                        { blackNode(7, null) },
                        { blackNode(4, 7), blackNode(9, 7) },
                        { blackNode(2, 4), blackNode(5, 4), blackNode(8, 9), blackNode(10, 9) },
                        { redNode(3, 2), redNode(6, 5) }
                    });

    public static final DeleteTestCaseData DELETE_LEAF_RIGHT
            = new DeleteTestCaseData(3, 1, 10, true,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) },
                        { redNode(6, 5) }
                    },
                    new TestTreeNode[][]{
                        { blackNode(7, null) },
                        { blackNode(4, 7), blackNode(9, 7) },
                        { blackNode(2, 4), blackNode(5, 4), blackNode(8, 9), blackNode(10, 9) },
                        { redNode(1, 2), redNode(6, 5) }
                    });

    public static final DeleteTestCaseData DELETE_LEAF_RIGHT_RED
            = new DeleteTestCaseData(6, 1, 10, true,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) },
                        { redNode(6, 5) }
                    },
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) }
                    });

    public static final DeleteTestCaseData DELETE_NODE_BLACK
            = new DeleteTestCaseData(2, 1, 10, true,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) },
                        { redNode(6, 5) }
                    },
                    new TestTreeNode[][]{
                        { blackNode(7, null) },
                        { blackNode(4, 7), blackNode(9, 7) },
                        { blackNode(3, 4), blackNode(5, 4), blackNode(8, 9), blackNode(10, 9) },
                        { redNode(1, 1), redNode(6, 5) }
                    });

    public static final DeleteTestCaseData DELETE_NODE_RED
            = new DeleteTestCaseData(7, 1, 10, true,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) },
                        { redNode(6, 5) }
                    },
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(6, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) }
                    });

    public static final DeleteTestCaseData DELETE_NODE_BLACK_WITH_RED_CHILD
            = new DeleteTestCaseData(9, 1, 10, true,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(7, 9), blackNode(10, 9) },
                        { blackNode(5, 7), blackNode(8, 7) },
                        { redNode(6, 5) }
                    },
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(7, 4) },
                        { blackNode(1, 2), blackNode(3, 2), blackNode(5, 7), blackNode(10, 7) },
                        { redNode(6, 5), redNode(8, 10) }
                    });

    public static final DeleteTestCaseData DELETE_NODE_BLACK_WITH_RED_CHILDS
            = new DeleteTestCaseData(8, 1, 12, false,
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(8, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(6, 8), redNode(10, 8) },
                        { blackNode(5, 6), blackNode(7, 6), blackNode(9, 10), blackNode(11, 10) },
                        { redNode(12, 11) }
                    },
                    new TestTreeNode[][]{
                        { blackNode(4, null) },
                        { blackNode(2, 4), blackNode(9, 4) },
                        { blackNode(1, 2), blackNode(3, 2), redNode(6, 7), redNode(11, 7) },
                        { blackNode(5, 6), blackNode(7, 6), blackNode(10, 11), blackNode(12, 11) }
                    });
    
    // private
    
    private static TestTreeNode blackNode(Object key, Object parentKey){
        return new TestTreeNode(key, parentKey, false);
    }
    private static TestTreeNode redNode(Object key, Object parentKey){
        return new TestTreeNode(key, parentKey, true);
    }
    
    
    // classes

    public static class TestCaseData {

        private final TestTreeNode[][] expectedAfterBuild;
        private final int startValue;
        private final int endValue;
        private final boolean splitInsert;

        public TestCaseData(int startValue, int endValue, boolean splitInsert,
                TestTreeNode[][] expectedAfterBuild) {
            this.expectedAfterBuild = expectedAfterBuild;
            this.startValue = startValue;
            this.endValue = endValue;
            this.splitInsert = splitInsert;
        }

        public TestTreeNode[][] getExpectedAfterBuild() {
            return expectedAfterBuild;
        }

        public int getStartValue() {
            return startValue;
        }

        public int getEndValue() {
            return endValue;
        }

        public boolean isSplitInsert() {
            return splitInsert;
        }
    }

    public static class DeleteTestCaseData extends TestCaseData {

        private final TestTreeNode[][] expectedAfterDelete;
        private final int removeValue;

        public DeleteTestCaseData(int removeValue, int startValue, int endValue,
                boolean splitInsert, TestTreeNode[][] expectedAfterBuild, TestTreeNode[][] expectedAfterDelete) {
            super(startValue, endValue, splitInsert, expectedAfterBuild);
            this.expectedAfterDelete = expectedAfterDelete;
            this.removeValue = removeValue;
        }

        public TestTreeNode[][] getExpectedAfterDelete() {
            return expectedAfterDelete;
        }

        public int getRemoveValue() {
            return removeValue;
        }
    }
}
