package com.aor.numbers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

public class ListAggregatorTest {

    List<Integer> list;

    @BeforeEach
    public void setUp(){
        list = Arrays.asList(1,2,4,2,5);
    }

    @Test
    public void sum() {
        ListAggregator aggregator = new ListAggregator();
        int sum = aggregator.sum(list);

        Assertions.assertEquals(14, sum);
    }

    @Test
    public void max() {
        ListAggregator aggregator = new ListAggregator();
        int max = aggregator.max(list);

        Assertions.assertEquals(5, max);
    }

    @Test
    public void min() {
        ListAggregator aggregator = new ListAggregator();
        int min = aggregator.min(list);

        Assertions.assertEquals(1, min);
    }

    @Test
    public void distinct() {
        ListAggregator aggregator = new ListAggregator();
        class StubGenericListDeduplicator implements GenericListDeduplicator {
            @Override
            public List<Integer> deduplicate(List<Integer> list, GenericListSorter sorter) {
                return Arrays.asList(1,2,4,5);
            }
        }
        StubGenericListDeduplicator deduplicator = new StubGenericListDeduplicator();
        ListSorter sorter = new ListSorter();
        int distinct = aggregator.distinct(list, deduplicator, sorter);

        Assertions.assertEquals(4, distinct);
    }

    @Test
    public void max_bug_7263() {
        list = Arrays.asList(-1, -4, -5);

        ListAggregator aggregator = new ListAggregator();
        int max = aggregator.max(list);

        Assertions.assertEquals(-1, max);
    }

    @Test
    public void distinct_bug_8726() {
        list = Arrays.asList(1, 2, 4, 2);

        ListAggregator aggregator = new ListAggregator();
        class StubGenericListDeduplicator implements GenericListDeduplicator {
            @Override
            public List<Integer> deduplicate(List<Integer> list, GenericListSorter sorter) {
                return Arrays.asList(1,2,4);
            }
        }
        StubGenericListDeduplicator deduplicator = new StubGenericListDeduplicator();
        ListSorter sorter = new ListSorter();

        int distinct = aggregator.distinct(list, deduplicator, sorter);

        Assertions.assertEquals(3, distinct);
    }

    @Test
    public void sorting() {
        list = Arrays.asList(1, 2, 4, 2);

        ListSorter sorter = new ListSorter();
        Assertions.assertEquals(true, sorter.sort(list).equals(Arrays.asList(1, 2, 2, 4)));
    }

    @Test
    public void deduplicating() {
        list = Arrays.asList(1, 2, 4, 2);

        ListDeduplicator deduplicator = new ListDeduplicator();
        class StubGenericListSorter implements GenericListSorter {

            @Override
            public List<Integer> sort(List<Integer> list) {
                return Arrays.asList(1,2,2,4);
            }

        }

        StubGenericListSorter sorter = new StubGenericListSorter();
        List<Integer> test = deduplicator.deduplicate(list, sorter);

        Assertions.assertEquals(true, test.equals(Arrays.asList(1, 2, 4)));
    }

}
