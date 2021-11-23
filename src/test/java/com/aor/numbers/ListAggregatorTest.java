package com.aor.numbers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

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
        ListSorter sorter = new ListSorter();
        GenericListDeduplicator deduplicator = Mockito.mock(GenericListDeduplicator.class);
        Mockito.when(deduplicator.deduplicate(Mockito.anyList(), Mockito.any())).thenReturn(Arrays.asList(1, 2, 4, 5));
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
        GenericListDeduplicator deduplicator = Mockito.mock(GenericListDeduplicator.class);
        Mockito.when(deduplicator.deduplicate(Mockito.anyList(), Mockito.any())).thenReturn(Arrays.asList(1, 2, 4));
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

        GenericListSorter sorter = Mockito.mock(GenericListSorter.class);
        Mockito.when(sorter.sort(Mockito.anyList())).thenReturn(Arrays.asList(1, 2, 4));
        List<Integer> test = sorter.sort(list);

        Assertions.assertEquals(true, test.equals(Arrays.asList(1, 2, 4)));
    }

}
