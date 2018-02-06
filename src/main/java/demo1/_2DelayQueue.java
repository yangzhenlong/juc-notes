package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * _2DelayQueue类描述: 延迟队列 简单示例
 *
 *
 * @author yangzhenlong
 * @since 2018/2/6
 */
public class _2DelayQueue {

    @Test
    public void testDelayQueue() throws InterruptedException, IOException {
        //往队列添加元素
        DelayQueue delayQueue = new DelayQueue();
        MyDelayed myDelayed = new MyDelayed(1, 5000);
        delayQueue.add(myDelayed);
        System.out.println("------添加元素成功----");
        //取出元素（到了延迟时间，才能取到）
        long start = System.currentTimeMillis();
        System.out.println("startTime: " + start);
        System.out.println("第一次取元素（poll）：" + delayQueue.poll());//因为没到到期时间，取不到，返回null
        System.out.println("第二次取元素（take）：" + delayQueue.take());//取不到阻塞，直到到了过期时间，取到元素
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start));
    }

    /**
     * 队列元素类
     */
    private static class MyDelayed implements Delayed{
        private int id;//元素Id
        private long createTime = System.currentTimeMillis();//元素创建时间
        private long expTime;//过期时间

        public MyDelayed(int id, long expTime) {
            this.id = id;
            this.expTime = expTime;
        }

        /**
         * 计算延迟到期的时间
         * @param unit
         * @return
         */
        public long getDelay(TimeUnit unit) {
            // (创建时间+过期时间) - 当前时间 = 到期时间
            long convert = unit.convert((createTime + expTime) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
            return convert;
        }

        /**
         * 当前元素与参数o元素 比较
         * @param o
         * @return
         */
        public int compareTo(Delayed o) {
            MyDelayed myo = (MyDelayed)o;
            if(this.createTime < myo.createTime){
                return 0;
            }else {
                return 1;
            }
        }

        public int getId() {
            return id;
        }

        public long getCreateTime() {
            return createTime;
        }

        public long getExpTime() {
            return expTime;
        }

        @Override
        public String toString() {
            return "MyDelayed{" +
                    "id=" + id +
                    ", createTime=" + createTime +
                    ", expTime=" + expTime +
                    '}';
        }
    }
}
