package edu.fptu.thomas.dang.repository.csv;

public interface DataMapper<T, K> {
    T mapToObject(K data);
}
