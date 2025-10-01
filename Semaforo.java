import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class Semaforo {
    public static void main(String[] args) throws InterruptedException {
        int nProd = args.length > 0 ? Integer.parseInt(args[0]) : 2;
        int nCons = args.length > 1 ? Integer.parseInt(args[1]) : 2;
        int bufferSize = args.length > 2 ? Integer.parseInt(args[2]) : 5;
        int itemsPerProducer = args.length > 3 ? Integer.parseInt(args[3]) : 10;

        BoundedBufferSem buffer = new BoundedBufferSem(bufferSize);

        List<Thread> threads = new LinkedList<>();

        for (int i = 0; i < nProd; i++) {
            Producer p = new Producer(buffer, itemsPerProducer, i);
            Thread tp = new Thread(p, "P-"+i);
            threads.add(tp);
            tp.start();
        }

        for (int i = 0; i < nCons; i++) {
            Consumer c = new Consumer(buffer, (nProd * itemsPerProducer) / nCons + 1, i);
            Thread tc = new Thread(c, "C-"+i);
            threads.add(tc);
            tc.start();
        }

        for (Thread t : threads) t.join();

        System.out.println("Fin de ejecución (SemaphoreSolution).");
    }
}

class BoundedBufferSem {
    private final int[] buffer;
    private int in = 0, out = 0;
    private final Semaphore items;
    private final Semaphore spaces;
    private final Semaphore mutex;

    public BoundedBufferSem(int size) {
        buffer = new int[size];
        items = new Semaphore(0);
        spaces = new Semaphore(size);
        mutex = new Semaphore(1);
    }

    public void put(int value) throws InterruptedException {
        spaces.acquire();
        mutex.acquire();
        try {
            buffer[in] = value;
            in = (in + 1) % buffer.length;
            System.out.printf("%s produjo %d (ocupado=%d/%d)\n",
                Thread.currentThread().getName(), value, items.availablePermits()+1, buffer.length);
        } finally {
            mutex.release();
            items.release();
        }
    }

    public int take() throws InterruptedException {
        items.acquire();
        mutex.acquire();
        try {
            int val = buffer[out];
            out = (out + 1) % buffer.length;
            System.out.printf("%s consumió %d (ocupado=%d/%d)\n",
                Thread.currentThread().getName(), val, items.availablePermits(), buffer.length);
            return val;
        } finally {
            mutex.release();
            spaces.release();
        }
    }
}

class Producer implements Runnable {
    private final BoundedBufferSem buffer;
    private final int count;
    private final int id;
    private final Random rnd = new Random();

    public Producer(BoundedBufferSem b, int count, int id) {
        this.buffer = b;
        this.count = count;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                int value = id * 1000 + i;
                buffer.put(value);
                Thread.sleep(rnd.nextInt(150));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer implements Runnable {
    private final BoundedBufferSem buffer;
    private final int count;
    private final int id;
    private final Random rnd = new Random();

    public Consumer(BoundedBufferSem b, int count, int id) {
        this.buffer = b;
        this.count = count;
        this.id = id;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                int val = buffer.take();
                Thread.sleep(rnd.nextInt(250));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
