package com.raz.algotoolbox.containers;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TreeListTest {

	// TODO add unit tests

	@Test
	public void testGenericToArray() {
		List<String> list = new TreeList<>();
		String[] expected = { "Hello", "Hi", "Goodbye" };
		list.addAll(Arrays.asList(expected));
		String[] actual = list.toArray(new String[0]);
		assertEquals(expected.length, actual.length);
		for (int i = 0; i < expected.length; ++i) {
			assertEquals(expected[i], actual[i]);
		}
	}

}
