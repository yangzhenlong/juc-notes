package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * _1BlockingQueue类描述:
 *  java.util.concurrent 包里的 BlockingQueue 接口表示一个线程安全放入和提取实例的队列
 *  通常用于一个线程生产对象，而另外一个线程消费这些对象的场景
 *
 * @author yangzhenlong
 * @since 2018/2/5
 */
public class _1BlockingQueue {
    @Test
    public void testBlockingQueue() throws IOException {
        //阻塞队列
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<Integer>(100);
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new Producer(queue));
        executorService.submit(new Consumer(queue));

        System.in.read();
    }

    private static class Producer implements Runnable{
        BlockingQueue queue = null;
        public Producer(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                for(int i=1;i<=100;i++){
                    //put()队列满时线程等待 offer()队列满时返回false。所以此处用put
                    queue.put(i);
                    System.out.println("生产者---------->" + i);
                    Thread.sleep(100);
                }
                System.out.println("生产者----------> end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Consumer implements Runnable{
        BlockingQueue queue = null;
        public Consumer(BlockingQueue queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (!queue.isEmpty()){//此处避免使用queue.size()>0 因为size() 会遍历整个队列，效率差
                    Object poll = queue.poll();
                    System.out.println("消费者==================================>" + poll);
                    Thread.sleep(200);
                }

                System.out.println("消费者==================================> end...");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
