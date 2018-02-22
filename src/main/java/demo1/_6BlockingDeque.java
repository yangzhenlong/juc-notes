package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * _6BlockingDeque类描述:
 *
 * @author yangzhenlong
 * @since 2018/2/22
 */
public class _6BlockingDeque {

    @Test
    public void test1() throws InterruptedException {
        BlockingDeque deque = new LinkedBlockingDeque();
        deque.addFirst(1);
        deque.addLast(2);
        System.out.println(deque.takeLast());
        System.out.println(deque.takeFirst());
    }

    @Test
    public void test2() throws IOException {
        BlockingDeque deque = new LinkedBlockingDeque();
        Producer p1 = new Producer(deque, 0);
        Producer p2 = new Producer(deque, 1);
        Consumer c1 = new Consumer(deque, 0);
        Consumer c2 = new Consumer(deque, 1);
        p1.start();
        p2.start();
        c1.start();
        c2.start();

        System.in.read();
    }

    private static class Producer extends Thread{
        BlockingDeque<Integer> deque;
        int type;//0：从头取 1：从尾取
        public Producer(BlockingDeque deque, int type) {
            this.deque = deque;
            this.type = type;
        }

        public void run(){
            try {
                if(type ==0) {
                    for (int i = 1; i <= 100; i++) {
                        deque.addFirst(i);
                        System.out.println("生产者从【" + (type == 0 ? "头" : "尾") + "】添加数据：+++++++++++++++++++++++++++++++++++++>" + i);
                        Thread.sleep(2000);
                    }
                }else{
                    for (int i = 1001; i <= 1100; i++) {
                        deque.addLast(i);
                        System.out.println("生产者从【" + (type == 0 ? "头" : "尾") + "】添加数据：+++++++++++++++++++++++++++++++++++++>" + i);
                        Thread.sleep(2000);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private static class Consumer extends Thread{
        BlockingDeque<Integer> deque;
        int type;//0：从头取 1：从尾取
        public Consumer(BlockingDeque deque, int type) {
            this.deque = deque;
            this.type = type;
        }

        public void run(){
            try {
                int take = -1;
                while (true) {
                    if (type == 0) {
                        take = deque.takeFirst();
                    } else {
                        take = deque.takeLast();
                    }
                    System.out.println("消费者从【" + (type == 0 ? "头" : "尾") + "】取到数据：----->" + take);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
