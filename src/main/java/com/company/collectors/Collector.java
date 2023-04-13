package com.company.collectors;

import java.util.List;

public interface Collector<T> {

    List<T> collect(List<Integer> wantedIds);
}