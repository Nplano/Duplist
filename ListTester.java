import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.Random;

public class ListTester implements Runnable {
	final private int numThr;
	final private List<Integer> list;
	final private CyclicBarrier interimBarr;
	final private CyclicBarrier printResBarr;
    final private CyclicBarrier mybarr;
	final private Random rand = new Random();

	ListTester(int _numThr, List<Integer> _list) {
		numThr = _numThr;
		list = _list;

		Runnable interimRun = new Runnable() {
			public void run() {
				System.out.println("Next test...");
				assert(list.isEmpty());
			}
		};
		Runnable printResRun = new Runnable() {
			public void run() {
				System.out.println("Tests complete!");
			}
		};
        Runnable test = new Runnable() {
			public void run() {


			}
		};

		interimBarr = new CyclicBarrier(numThr, interimRun);
		printResBarr = new CyclicBarrier(numThr, printResRun);
        mybarr = new CyclicBarrier(numThr, test);
	}

	/**
	 * Test to see if sentinels are in the list, and that removing items
	 * not in the list works. Be sure to write additional tests as well!
	 * NOTE: This means your implementation must have sentinel nodes,
	 * with minimum and maximum values at the beginning and end respectively.
	 */
	public void run() {
		// Child threads start here to begin testing-- tests are below

		// We suggest writing a series of zero-sum tests,
		// i.e. lists should be empty between tests.
		// The interimBarr enforces this.
		try {
			containsOnEmptyListTest();
			interimBarr.await();
			sentinelsInEmptyListTest();
            interimBarr.await();
            //test add 0-99 for each thread check that they are cotained in the
            //list then remove them and check that the list is empty 
            mytest_add();
            mybarr.await();
            mytest_contains_after_add();
            mybarr.await();
            mytest_remove();
            mybarr.await();
            mytest_contains_after_remove();
            interimBarr.await();


			 printResBarr.await();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private final static int NUMREP = 100000;
	private void containsOnEmptyListTest() {
		for (int i = 0; i < 10; i++) {
			// Remove values not in list. If this is ever true, crash!
			assert(list.contains(i) == false);
		}
	}


	private void sentinelsInEmptyListTest() {
		int i = 0;
		for (; i < NUMREP; i++) {
			// We use unique thread IDs as our values
			// After an add(), a subsequent contains() should report true
			assert(list.add(Integer.MIN_VALUE) == false);
			assert(list.contains(Integer.MIN_VALUE));
			assert(list.add(Integer.MAX_VALUE) == false);
			assert(list.contains(Integer.MAX_VALUE));
		}
	}
    private void mytest_add() {

        int i = 0;
        for (; i < 100; i++) {
            assert(list.add(i) == true);

        }

	}
    private void mytest_remove() {

        int i = 0;
        for (; i < 100; i++) {
            assert(list.remove(i) == true);

        }

	}
    private void mytest_contains_after_add() {

        int i = 0;
        for (; i < 100; i++) {
            assert(list.contains(i)==true);

        }

    }
    private void mytest_contains_after_remove() {

        int i = 0;
        for (; i < 100; i++) {
            assert(list.contains(i)==false);

        }

    }




}
