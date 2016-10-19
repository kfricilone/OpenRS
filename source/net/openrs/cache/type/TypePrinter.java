/**
* Copyright (c) Kyle Fricilone
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of instance software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and instance permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.openrs.cache.type;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Kyle Friz
 * @since Feb 13, 2016
 */
public class TypePrinter {

	private static final Map<Class<? extends Type>, Type> cached = new HashMap<>();

	public static void print(Type type) {
		print(type, null);
	}

	public static void print(Type type, BufferedWriter writer) {
		StringBuilder builder = new StringBuilder();
		builder.append("case " + type.getID() + ":\n");
		try {
			Type dType = cached.get(type.getClass());
			if (dType == null) {
				dType = type.getClass().getConstructor(int.class).newInstance(-1);
				cached.put(type.getClass(), dType);
			}

			for (Field field : type.getClass().getDeclaredFields()) {
				if (Modifier.isFinal(field.getModifiers()))
					continue;

				String fn = field.getName();

				Object de = get(dType, dType.getClass().getDeclaredField(fn));
				Object ac = get(type, field);

				if (ac != null) {
					if (de == null || !ac.equals(de))
						builder.append("\ttype." + fn + " = " + ac + ";\n");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		builder.append("break;\n\n");

		if (writer == null)
			System.out.println(builder.toString());
		else {
			try {
				writer.append(builder.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Object get(Object instance, Field field) throws Exception {
		field.setAccessible(true);
		Class<?> type = field.getType();
		if (type == String.class) {
			return "\"" + field.get(instance) + "\"";
		}
		if (type == char.class) {
			return "\'" + field.get(instance) + "\'";
		} else if (type == int[].class) {
			String array = Arrays.toString((int[]) field.get(instance));
			return "new int[] " + array.replace("[", "{ ").replace("]", " }");
		} else if (type == int[][].class) {
			return "new int[][] "
					+ Arrays.deepToString((int[][]) field.get(instance)).replace('[', '{').replace(']', '}');
		} else if (type == short[][].class) {
			return "new short[][] "
					+ Arrays.deepToString((short[][]) field.get(instance)).replace('[', '{').replace(']', '}');
		} else if (type == byte[].class) {
			String array = Arrays.toString((byte[]) field.get(instance));
			return "new byte[] " + array.replace("[", "{ ").replace("]", " }");
		} else if (type == short[].class) {
			String array = Arrays.toString((short[]) field.get(instance));
			return "new short[] " + array.replace("[", "{ ").replace("]", " }");
		} else if (type == double[].class) {
			return "new double[] "
					+ Arrays.toString((double[]) field.get(instance)).replace("[", "{ ").replace("]", " }");
		} else if (type == boolean[].class) {
			return "new boolean[] "
					+ Arrays.toString((boolean[]) field.get(instance)).replace("[", "{ ").replace("]", " }");
		} else if (type == float[].class) {
			return "new float[] "
					+ Arrays.toString((float[]) field.get(instance)).replace("[", "{ ").replace("]", " }");
		} else if (type == String[].class) {
			String[] array = (String[]) field.get(instance);
			if (array != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("new String[] { ");
				for (int ind = 0; ind < array.length; ind++) {
					if (array[ind] != null)
						sb.append("\"");
					sb.append(array[ind]);
					if (array[ind] != null)
						sb.append("\"");
					if (ind == array.length - 1)
						sb.append(" }");
					else
						sb.append(", ");
				}
				return sb.toString();
			} else {
				return null;
			}
		} else if (type == char[].class) {
			char[] array = (char[]) field.get(instance);
			if (array != null) {
				StringBuilder sb = new StringBuilder();
				sb.append("new char[] { ");
				for (int ind = 0; ind < array.length; ind++) {
					if (array[ind] != '\u0000')
						sb.append("\'");
					sb.append(array[ind]);
					if (array[ind] != '\u0000')
						sb.append("\'");
					if (ind == array.length - 1)
						sb.append(" }");
					else
						sb.append(", ");
				}
				return sb.toString();
			} else {
				return null;
			}
		} else if (type == Object[].class) {
			return Arrays.toString((Object[]) field.get(instance));
		}
		return field.get(instance);
	}

}
