package com.rwtema.monkmod;


import java.util.function.Function;

public interface ClientFunction<T, R> extends Function<T, R> {
	@Override
	default R apply(T t) {
		return applyFallback(t);
	}

	R applyFallback(T t);
}
