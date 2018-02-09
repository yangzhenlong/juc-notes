package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.SynchronousQueue;

/**
 * _5SynchronousQueue类描述: 每次队列中只有一个元素
 *
 * @author yangzhenlong
 * @since 2018/2/7
 */
public class _5SynchronousQueue {
    static Random random = new Random();
    //add方法抛异常 java.lang.IllegalStateException: Queue full
    @Test
    public void test1() throws InterruptedException, IOException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
        queue.put(1);
        System.out.println(queue.poll());
        //queue.add(1);//抛异常 java.lang.IllegalStateException: Queue full
        boolean offer = queue.offer(1);
        System.out.println(offer);
    }

    //添加一个元素后，不能再添加（线程阻塞中）
    @Test
    public void test2() throws InterruptedException, IOException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
        System.out.println("添加1");
        queue.put(1);//如果没有线程消费，则一直阻塞在这里，后面的代码不会执行
        System.out.println("添加2");
        queue.put(2);
    }

    @Test
    public void testSynchronousQueue2() throws IOException {
        SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();
        new Put(queue).start();//生产者
        new Take(queue).start();//消费者
        System.in.read();//阻塞主线程
    }

    static class Put extends Thread{
        SynchronousQueue<Integer> queue;
        public Put(SynchronousQueue<Integer> queue){
            this.queue = queue;
        }
        public void run(){
            while (true){
                try {
                    int i = random.nextInt(10);
                    System.out.println("put------------>" + i);
                    queue.put(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class Take extends Thread{
        SynchronousQueue<Integer> queue;
        public Take(SynchronousQueue<Integer> queue){
            this.queue = queue;
        }
        public void run(){
            while (true){
                try {
                    Thread.sleep(1000);
                    System.out.println("take========================================>" + queue.take());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
