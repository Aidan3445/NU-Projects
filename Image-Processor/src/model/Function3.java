package model;

/**
 * Represents a function that accepts 3 arguments and produces a results.
 * This is a functional interface whose functional method is apply(Object).
 *
 * @param <T> the first type of input to the function
 * @param <Q> the second type of input to the function
 * @param <S> the third type of input to the function
 * @param <R> the return type of input
 */
public interface Function3<T, Q, S, R> {
  /**
   * Applies this function to the given arguments.
   * @param input1 the first function argument
   * @param input2 the second function argument
   * @param input3 the third function argument
   * @return the function result
   */
  R apply(T input1, Q input2, S input3);
}