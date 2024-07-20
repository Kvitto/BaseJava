import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SortedArrayStorage;

/**
 * Test for your com.urise.webapp.storage.ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final SortedArrayStorage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        Resume r2 = new Resume();
        Resume r3 = new Resume();
        Resume r4 = new Resume();
        Resume r5 = new Resume();

        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r5);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r4);
        ARRAY_STORAGE.save(r1);

        System.out.println("Get r1: " + ARRAY_STORAGE.get(r1.getUuid()));
        System.out.println("Size: " + ARRAY_STORAGE.getSize());

        System.out.println("Get dummy: " + ARRAY_STORAGE.get("dummy"));

        Resume r6 = new Resume();
        ARRAY_STORAGE.update(r6);

        printAll();
        ARRAY_STORAGE.delete(r1.getUuid());
        printAll();
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.getSize());
    }

    static void printAll() {
        System.out.println("\nGet All");
        for (Resume r : ARRAY_STORAGE.getAll()) {
            System.out.println(r);
        }
    }
}
