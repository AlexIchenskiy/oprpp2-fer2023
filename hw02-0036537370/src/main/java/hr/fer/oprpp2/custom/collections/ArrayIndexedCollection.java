package hr.fer.oprpp2.custom.collections;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Class representing an indexed array collection.
 */
public class ArrayIndexedCollection implements List {

	/**
	 * Constant representing arrays initial capacity.
	 */
	private static final int INITIAL_CAPACITY = 16;

	/**
	 * Constant representing the memory multiplier factor when used memory is exceeded.
	 */
	private static final int MEMORY_MULTIPLIER = 2;

	/**
	 * Number of the elements in the array.
	 */
	private int size;

	/**
	 * Number of previous array modifications.
	 */
	private long modificationCount = 0;

	/**
	 * Internal array for storing elements.
	 */
	private Object[] elements;

	/**
	 * Local implementation of ElementsGetter for indexed array.
	 */
	private static class ElementsGetterAIC implements ElementsGetter {

		/**
		 * Parent collection.
		 */
		private final ArrayIndexedCollection aic;

		/**
		 * Index of the current getter position.
		 */
		private int pointer;

		/**
		 * Modification count at the moment of getter creation.
		 */
		private final long savedModificationCount;

		private ElementsGetterAIC(ArrayIndexedCollection aic) {
			if (aic == null) {
				throw new NullPointerException("Array cant be null!");
			}

			this.aic = aic;
			this.pointer = 0;
			this.savedModificationCount = aic.modificationCount;
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public boolean hasNextElement() {
			if (this.savedModificationCount != aic.modificationCount) {
				throw new ConcurrentModificationException();
			}

			return pointer < aic.size();
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public Object getNextElement() {
			if (this.savedModificationCount != aic.modificationCount) {
				throw new ConcurrentModificationException();
			}

			if (!this.hasNextElement()) {
				throw new NoSuchElementException("No elements left!");
			}

			return this.aic.elements[pointer++];
		}
	}

	/**
	 * Constructs an array with initial capacity of 16.
	 */
	public ArrayIndexedCollection() {
		this(INITIAL_CAPACITY);
	}

	/**
	 * Constructs an array with given initial capacity.
	 * @param initialCapacity Initial capacity for an array
	 */
	public ArrayIndexedCollection(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity cant be lower than 1!");
		}

		this.elements = new Object[initialCapacity];
		this.size = 0;
	}

	/**
	 * Constructs an array filled with the elements from the other collection.
	 * @param other Other collection which elements are copied into the array
	 */
	public ArrayIndexedCollection(Collection other) {
		this(other, 1);
	}

	/**
	 * Constructs an array with initial capacity filled with the elements from the other collection. If the initial
	 * capacity is lower than the size of other collection, it is set to the size of the other collection.
	 * @param other Other collection which elements are copied into the array
	 * @param initialCapacity Initial capacity for an array
	 */
	public ArrayIndexedCollection(Collection other, int initialCapacity) {
		if (other == null) {
			throw new NullPointerException("Collection cant be null!");
		}

		if (other.size() < 1 && initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity cant be lower than 1!");
		}

		this.elements = new Object[Math.max(initialCapacity, other.size())];
		this.addAll(other);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add (Object value) {
		if (value == null) {
			throw new NullPointerException("Value cant be null!");
		}

		if (this.size == this.elements.length) {
			Object[] temp = this.elements;
			this.elements = new Object[this.elements.length * MEMORY_MULTIPLIER];
			this.modificationCount++;

			for (int i = 0; i < this.size; i++) {
				this.elements[i] = temp[i];
			}

			this.elements[this.size] = value;
			this.size++;

			return;
		}

		this.elements[this.size] = value;
		this.size++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object value) {
		for (int i = 0; i < this.size; i++) {
			if (this.elements[i] == value) {
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object value) {
		boolean isPassed = false;

		for (int i = 0; i < this.size; i++) {
			if (this.elements[i].equals(value) && !isPassed) {
				isPassed = true;
				this.modificationCount++;
				this.size--;
			}

			if (isPassed) {
				if (i == this.size) {
					this.elements[i] = null;
					continue;
				}

				this.elements[i] = this.elements[i + 1];
			}
		}

		return isPassed;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		Object[] temp = new Object[this.size];

		for (int i = 0; i < this.size; i++) {
			temp[i] = this.elements[i];
		}

		return temp;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		for (int i = 0; i < this.size; i++) {
			this.elements[i] = null;
		}

		this.modificationCount++;
		this.size = 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ElementsGetter createElementsGetter() {
		return new ElementsGetterAIC(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object get(int index) {
		if (index < 0 || index > this.size - 1) {
			throw new IndexOutOfBoundsException("Index cant be out of range!");
		}

		return elements[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void insert (Object value, int position) {
		if (position < 0 || position > this.size) {
			throw new IndexOutOfBoundsException("Index cant be out of range!");
		}

		if (value == null) {
			throw new NullPointerException("Value cant be null");
		}

		if (position == this.size) {
			this.add(value);
			return;
		}

		Object[] temp = new Object[this.elements.length];
		for (int i = 0; i < this.elements.length; i++) {
			temp[i] = this.elements[i];
		}
		this.elements = new Object[this.elements.length];
		int count = this.size;
		this.size = 0;

		for (int i = 0; i < count + 1; i++) {
			if (i < position) {
				this.add(temp[i]);
				continue;
			}

			if (i == position) {
				this.add(value);
				continue;
			}

			this.add(temp[i - 1]);
		}

		this.modificationCount++;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf (Object value) {
		for (int i = 0; i < this.size; i++) {
			if (this.elements[i].equals(value)) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(int index) {
		if (index < 0 || index > this.size - 1) {
			throw new IndexOutOfBoundsException("Index cant be out of range!");
		}

		for (int i = 0; i < this.size; i++) {
			if (i >= index) {
				if (i == this.size - 1) {
					this.elements[i] = null;
					continue;
				}

				this.elements[i] = this.elements[i + 1];
			}
		}

		this.size--;
		this.modificationCount++;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ArrayIndexedCollection)) return false;
		ArrayIndexedCollection that = (ArrayIndexedCollection) o;
		return size == that.size && Arrays.equals(elements, that.elements);
	}

	@Override
	public int hashCode() {
		int result = Objects.hash(size, modificationCount);
		result = 31 * result + Arrays.hashCode(elements);
		return result;
	}

}
