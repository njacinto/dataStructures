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
package jav.util.int_.concurrent;

import jav.util.int_.IntMap;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author njacinto
 */
public final class IntMapConcurrencyTestUtil {
    private static final Logger log = Logger.getLogger(IntMapConcurrencyTestUtil.class.getName());

    private IntMapConcurrencyTestUtil() {
    }
    
    public static void setLogLevel(Level level){
        log.setLevel(level);
    } 
    
    public static boolean testInt(IntConcurrentHashMapAbstract map, int numElements, int numOfThreads, 
            int numOfIteractions, int numTestArray, int numTestIterator, boolean throwsConcurrentModification){
        final int numOfElementsPerThread = numElements/numOfThreads;
        List<Callable<Boolean>> callables = new ArrayList<>(numOfThreads+numTestIterator);
        for(int i=0; i<numOfThreads; i++){
            callables.add(
                    new DataOperationsRunnable(map, numOfIteractions, numOfElementsPerThread, i*numOfElementsPerThread));
        }
        if(numTestIterator>0){
            for(int i=0; i<numTestIterator; i++){
                callables.add(new IteratorTestRunnable(map, throwsConcurrentModification));
            }
        }
        if(numTestArray>0){
            for(int i=0; i<numTestArray; i++){
                callables.add(new ArrayTestRunnable(map));
            }
        }
        ExecutorService executer = Executors.newFixedThreadPool(callables.size());
        long timestamp = System.currentTimeMillis();
        List<Future<Boolean>> results;
        try {
            results = executer.invokeAll(callables);
        } catch(InterruptedException ex){
            log.log(Level.SEVERE, "Unexpected InterruptedException: "+ex.getMessage(), ex);
            return false;
        }
        boolean executionResult = true;
        for(Future<Boolean> result : results){
            try {
                executionResult &= result.get();
                log.log(Level.FINER, "Execution result: {0}", executionResult);
            } catch (InterruptedException | ExecutionException ex) {
                log.log(Level.SEVERE, ex.getMessage(), ex);
                return false;
            }
        }
        log.log(Level.FINEST, "Execution time: {0}", ((System.currentTimeMillis()-timestamp)));
        return executionResult && checkMap(map, numElements, numOfThreads);
    }
    
    
    public static boolean checkMap(IntMap map, int numElements, int numOfThreads){
        final int numOfElementsPerThread = numElements/numOfThreads;
        final int expected = numOfElementsPerThread*numOfThreads+numOfElementsPerThread;
        if(expected!=map.size()){
            log.log(Level.SEVERE, "Missing elements: expected={0} found={1}", new Object[]{expected, map.size()});
            return false;
        }
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<map.size(); i++){
            if(!map.containsKey(i)){
                sb.append(i).append(", ");
                log.log(Level.SEVERE, "Missing value: {0}", i);
            }
        }
        if(sb.length()>0){
            sb.setLength(sb.length()-2);
            log.log(Level.SEVERE, "Missing elements: {0}", sb.toString());
            return false;
        }
        return true;
    }
    
    private static class DataOperationsRunnable implements Callable<Boolean> {
        private final IntMap map;
        private final int numElements;
        private final int numOfIteractions;
        private final int startIndex;

        public DataOperationsRunnable(IntMap map, int numOfIteractions, int numElements, int startIndex) {
            this.map = map;
            this.numElements = numElements;
            this.numOfIteractions = numOfIteractions;
            this.startIndex = startIndex;
        }
        
        @Override
        public Boolean call() {
            try {
                for(int j=0; j<numOfIteractions; j++){
                    for(int i=startIndex; i<startIndex+numElements; i++){
                        map.put(i,i);
                    }
                    for(int i=(startIndex+numElements+numElements-1); i>=startIndex+numElements; i--){
                        map.put(i, i);
                    }
                    for(int i=(startIndex+numElements+numElements-1); i>=startIndex+numElements; i--){
                        map.remove(i);
                    }
                    for(int i=(startIndex+numElements+numElements-1); i>=startIndex+numElements; i--){
                        if(!map.containsKey(i)){
                            map.put(i, i);
                        }
                    }
                }
                return true;
            } catch(Exception ex){
                log.log(Level.SEVERE, "Concurrency test fail: "+ex.getMessage(), ex);
                return false;
            }
        }
    }
    
    private static class IteratorTestRunnable implements Callable<Boolean> {
        private final Iterable<IntMap.Entry> iterable;
        private final boolean throwsConcurrentModification;

        public IteratorTestRunnable(Iterable<IntMap.Entry> iterable, boolean throwsConcurrentModification) {
            this.iterable = iterable;
            this.throwsConcurrentModification = throwsConcurrentModification;
        }
        
        @Override
        public Boolean call() {
            if(iterable!=null){
                try {
                    for(int j=0; j<10; j++){
                        Iterator<IntMap.Entry> it = iterable.iterator();
                        try {
                            while(it.hasNext()){
                                IntMap.Entry e = it.next();
                            }
                        } catch(ConcurrentModificationException ex){
                            if(!throwsConcurrentModification){
                                log.log(Level.SEVERE, "Unexpected concurrent modification exception on Iterator.", ex);
                                return false;
                            }
                        }
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            log.log(Level.SEVERE, null, ex);
                        }
                    }
                } catch(Exception ex){
                    log.log(Level.SEVERE, "Iterator test fail: "+ex.getMessage(), ex);
                    return false;
                }
            }
            return true;
        }
    }
    
    private static class ArrayTestRunnable implements Callable<Boolean> {
        private final IntConcurrentHashMapAbstract map;

        public ArrayTestRunnable(IntConcurrentHashMapAbstract map) {
            this.map = map;
        }
        
        @Override
        public Boolean call() {
            if(map!=null){
                try {
                    for(int j=0; j<10; j++){
                        map.toArray();
                        map.valuesToArray();
                        map.keysToArray();
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException ex) {
                            log.log(Level.SEVERE, null, ex);
                        }
                    }
                } catch(Exception ex){
                    log.log(Level.SEVERE, "Iterator test fail: "+ex.getMessage(), ex);
                    return false;
                }
            }
            return true;
        }
    }
}
