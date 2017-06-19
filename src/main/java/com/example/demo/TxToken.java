package com.example.demo;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class TxToken implements Serializable {
	final ConcurrentMap<String, Set<Token>> tokens = new ConcurrentHashMap<>();
	final int tokenCapacity;

	public TxToken() {
		this(16);
	}

	public TxToken(int tokenCapacity) {
		this.tokenCapacity = tokenCapacity;
	}

	public static final String GLOBAL_NAMESPACE = "__GLOBAL__";

	public Token create(String namespace) {
		Set<Token> tokenSet = tokens.computeIfAbsent(namespace,
				ns -> new LinkedHashSet<>());
		Token token = Token.generate();
		synchronized (tokens) {
			tokenSet.add(token);
			if (tokenSet.size() > tokenCapacity) {
				Iterator<Token> iterator = tokenSet.iterator();
				iterator.next();
				iterator.remove(); // remove first
			}
		}
		System.out.println(tokens);
		return token;
	}

	public Token create() {
		return create(GLOBAL_NAMESPACE);
	}

	public boolean check(String namespace, Token token) {
		Set<Token> tokenSet = tokens.get(namespace);
		if (tokenSet == null) {
			return false;
		}
		synchronized (tokens) {
			if (tokenSet.contains(token)) {
				tokenSet.remove(token);
				return true;
			}
		}
		return false;
	}

	public boolean check(Token token) {
		return check(GLOBAL_NAMESPACE, token);
	}

	public static class Token {
		private final String value;
		private static SecureRandom random = new SecureRandom();

		public Token(String value) {
			this.value = value;
		}

		public static Token generate() {
			return new Token(BigInteger.valueOf(random.nextLong()).toString(32));
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof Token)) {
				return false;
			}
			Token token = (Token) o;
			return value.equals(token.value);
		}

		@Override
		public int hashCode() {
			return value.hashCode();
		}

		@Override
		public String toString() {
			return value;
		}
	}

}
