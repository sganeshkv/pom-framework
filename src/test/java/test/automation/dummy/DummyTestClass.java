package test.automation.dummy;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Objects;

public class DummyTestClass {
	public static void main(String[] args) throws ParseException {
		// initializing Object array1
		Object[] b1 = new Object[] { 'a', 'b' };

		// initializing Object array2
		Object[] b2 = new Object[] { 'b', 'a','c' };

		// initializing Object array3
		Object[] b3 = new Object[] { 'x', 'y' };

		// checking array1 and array2 for equality
		System.out.println("Array1 and Array2 are equal:" + (Arrays.asList(b1).containsAll(Arrays.asList(b2)) && Arrays.asList(b2).containsAll(Arrays.asList(b1))));

		// checking array1 and array3 for equality
		System.out.println("Array1 and Array3 are equal:" + Objects.deepEquals(b1, b3));
	}
}
