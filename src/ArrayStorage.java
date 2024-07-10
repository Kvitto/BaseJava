import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    int amount;

    void clear() {
        Arrays.fill(storage, 0, amount, null);
        amount = 0;
    }

    void save(Resume resume) {
        if (amount < storage.length) {
            storage[amount++] = resume;
        }
    }

    Resume get(String uuid) {
        for (Resume resume : getAll()) {
            if (uuid.equals(resume.uuid)) return resume;
        }
        return null;
    }

    void delete(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) return;
        System.arraycopy(storage, index + 1, storage, index, amount - index - 1);
        storage[--amount] = null;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOf(storage, amount);
    }

    int size() {
        return amount;
    }

    int findIndex(String uuid) {
        for (int i = 0; i < amount; i++) {
            if (uuid.equals(storage[i].uuid)) {
                return i;
            }
        }
        return -1;
    }
}
