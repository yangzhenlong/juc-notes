package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * _3LinkedBlockingQueue类描述: 链表阻塞队列
 *
 * @author yangzhenlong
 * @since 2018/2/6
 */
public class _3LinkedBlockingQueue {

    @Test
    public void testLinkedBlockingQueue() throws InterruptedException, IOException {
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue(10);
        new Producer(queue).start();
        Thread.sleep(10);//等待队列中有数据，才开始消费
        new Consumer(queue).start();
        //主线程阻塞
        System.in.read();
    }

    static class Producer extends Thread{
        private LinkedBlockingQueue<Integer> queue;
        public Producer(LinkedBlockingQueue<Integer> queue){
            this.queue = queue;
        }
        public void run(){
            try {
                for(int i=1;i<=100;i++){
                    Thread.sleep(10);
                    queue.put(i);
                    System.out.println("----> 生产者添加数据：【" + i + "】");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Consumer extends Thread{
        private LinkedBlockingQueue<Integer> queue;
        public Consumer(LinkedBlockingQueue<Integer> queue){
            this.queue = queue;
        }
        public void run(){
            try {
                while (!queue.isEmpty()) {
                    Thread.sleep(50);
                    System.out.println("=====================> 消费者获取数据：【" + queue.take() + "】");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
