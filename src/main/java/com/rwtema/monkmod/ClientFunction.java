package com.rwtema.monkmod;


import javax.annotation.Nonnull;
import java.util.function.Function;

public interface ClientFunction<T, R> extends Function<T, R> {
	@Nonnull
	@Override
	default R apply(T t) {
		return applyFallback(t);
	}

	@Nonnull
	R applyFallback(T t);
}
