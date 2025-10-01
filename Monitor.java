import java.util.LinkedList;
import java.util.List;
import java.util.Random;


public class Monitor {
    public static void main(String[] args) throws InterruptedException {
        int nProd = args.length > 0 ? Integer.parseInt(args[0]) : 2;
        int nCons = args.length > 1 ? Integer.parseInt(args[1]) : 2;
        int bufferSize = args.length > 2 ? Integer.parseInt(args[2]) : 5;
        int itemsPerProducer = args.length > 3 ? Integer.parseInt(args[3]) : 10;

        BoundedBufferMonitor buffer = new BoundedBufferMonitor(bufferSize);

        List<Thread> threads = new LinkedList<>();

        for (int i = 0; i < nProd; i++) {
            ProducerM p = new ProducerM(buffer, itemsPerProducer, i);
            Thread tp = new Thread(p, "P-"+i);
            threads.add(tp);
            tp.start();
        }

        for (int i = 0; i < nCons; i++) {
            ConsumerM c = new ConsumerM(buffer, (nProd * itemsPerProducer) / nCons + 1, i);
            Thread tc = new Thread(c, "C-"+i);
            threads.add(tc);
            tc.start();
        }

        for (Thread t : threads) t.join();

        System.out.println("Fin de ejecución (MonitorSolution).");
    }
}

class BoundedBufferMonitor {
    private final int[] buffer;
    private int in = 0, out = 0, count = 0;

    public BoundedBufferMonitor(int size) {
        buffer = new int[size];
    }

    public synchronized void put(int value) throws InterruptedException {
        while (count == buffer.length) wait();
        buffer[in] = value;
        in = (in + 1) % buffer.length;
        count++;
        System.out.printf("%s produjo %d (ocupado=%d/%d)\n",
            Thread.currentThread().getName(), value, count, buffer.length);
        notifyAll();
    }

    public synchronized int take() throws InterruptedException {
        while (count == 0) wait();
        int val = buffer[out];
        out = (out + 1) % buffer.length;
        count--;
        System.out.printf("%s consumió %d (ocupado=%d/%d)\n",
            Thread.currentThread().getName(), val, count, buffer.length);
        notifyAll();
        return val;
    }
}

class ProducerM implements Runnable {
    private final BoundedBufferMonitor buffer;
    private final int count;
    private final int id;
    private final Random rnd = new Random();

    public ProducerM(BoundedBufferMonitor b, int count, int id) {
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

class ConsumerM implements Runnable {
    private final BoundedBufferMonitor buffer;
    private final int count;
    private final int id;
    private final Random rnd = new Random();

    public ConsumerM(BoundedBufferMonitor b, int count, int id) {
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
