import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    private final Resume[] storage = new Resume[10000];
    private int amount;

    public void clear() {
        Arrays.fill(storage, 0, amount, null);
        amount = 0;
    }

    public void save(Resume resume) {
        if (amount < storage.length) {
            storage[amount++] = resume;
        }
    }

    public Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) return null;
        return storage[index];
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) return;
        System.arraycopy(storage, index + 1, storage, index, amount - index - 1);
        storage[--amount] = null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    public Resume[] getAll() {
        return Arrays.copyOf(storage, amount);
    }

    public int size() {
        return amount;
    }

    private int findIndex(String uuid) {
        for (int i = 0; i < amount; i++) {
            if (uuid.equals(storage[i].uuid)) {
                return i;
            }
        }
        return -1;
    }
}
