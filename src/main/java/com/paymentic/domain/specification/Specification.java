package com.paymentic.domain.specification;

public interface Specification<T> {
  boolean IsSatisfiedBy(T element);

}
