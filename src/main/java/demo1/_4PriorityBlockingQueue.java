package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * _4PriorityBlockingQueue类描述: 优先级队列
 *      示例场景：快递配送，距离远优先配送
 * @author yangzhenlong
 * @since 2018/2/6
 */
public class _4PriorityBlockingQueue {
    @Test
    public void testPriorityBlockingQueue() throws InterruptedException, IOException {
        PriorityBlockingQueue<User> queue = new PriorityBlockingQueue<User>(100);
        Random random = new Random();
        for(int i=1;i<=100;i++){
            User user = new User(i, random.nextInt(1000));
            queue.put(user);
            System.out.println("用户【" + user + "】已下单");
        }
        Thread.sleep(500);
        //消费队列
        new SendPackage(queue).start();

        System.in.read();
    }

    static class User implements Comparable{
        private int id;
        private int distance;//距离

        public User(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", distance=" + distance +
                    '}';
        }
        /**
         * 优先规则
         * @param o
         */
        public int compareTo(Object o) {
            User target = (User)o;
            //越远越优先
            return target.distance - this.distance;
        }
    }

    /**
     * 配送包裹线程
     */
    static class SendPackage extends Thread{
        PriorityBlockingQueue<User> queue;
        public SendPackage(PriorityBlockingQueue<User> queue){
            this.queue = queue;
        }
        public void run(){
            try {
                while (!queue.isEmpty()) {
                    User user = queue.take();
                    Thread.sleep(50);
                    System.out.println("配送开始--------------->" + user);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
