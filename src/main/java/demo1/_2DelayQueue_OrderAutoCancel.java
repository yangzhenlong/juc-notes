package demo1;


import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * _2DelayQueue类描述: 延迟队列 订单自动取消demo
 *
 *
 * @author yangzhenlong
 * @since 2018/2/6
 */
public class _2DelayQueue_OrderAutoCancel {
    @Test
    public void testDelayQueue() throws InterruptedException, IOException {
        DelayQueue<OrderDelayed> queue = new DelayQueue<OrderDelayed>();
        //1.创建订单Order 同时创建订单队列元素OrderDelayed
        for(int i=1;i<=100;i++){
            Order order = new Order(i, "待支付");
            OrderDelayed delayed = new OrderDelayed(order, System.currentTimeMillis() + i*50,  6000);//6秒后过期
            queue.add(delayed);
        }
        //支付线程
        Thread pay = new Thread(new PayOrder(queue));
        //取消线程
        Thread cancel = new Thread(new CancelOrder(queue));
        //2个线程竞争queue的锁，对于未及时支付的订单进行取消
        pay.start();
        cancel.start();

        System.in.read();
    }

    private static class Order{
        private int orderNo;
        private String status;//待支付、已支付、已取消

        public Order(int orderNo, String status) {
            this.orderNo = orderNo;
            this.status = status;
        }

        @Override
        public boolean equals(Object obj) {
            Order order = (Order)obj;
            return this.orderNo == order.orderNo;
        }

        @Override
        public String toString() {
            return "Order{" +
                    "orderNo=" + orderNo +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    /**
     * 队列元素类
     */
    private static class OrderDelayed implements Delayed{
        private Order order;//订单
        private long createTime;//元素创建时间
        private long expTime;//过期时间

        public OrderDelayed(Order order, long createTime, long expTime) {
            this.order = order;
            this.createTime = createTime;
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
            OrderDelayed myo = (OrderDelayed)o;
            if(this.createTime < myo.createTime){
                return 0;
            }else {
                return 1;
            }
        }
    }

    /**
     * 支付订单 线程
     */
    private static class PayOrder implements Runnable{
        private DelayQueue<OrderDelayed> queue;
        public PayOrder(DelayQueue<OrderDelayed> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (!queue.isEmpty()){
                    OrderDelayed take = queue.take();
                    Order order = take.order;
                    order.status = "已支付";//处理已支付流程...
                    System.out.println("---------> 订单：id=【" + order.orderNo + "】已支付，orderInfo=" + order);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 取消订单 线程
     */
    private static class CancelOrder implements Runnable{
        private DelayQueue<OrderDelayed> queue;
        public CancelOrder(DelayQueue<OrderDelayed> queue) {
            this.queue = queue;
        }

        public void run() {
            try {
                while (!queue.isEmpty()){
                    OrderDelayed take = queue.take();
                    Order order = take.order;
                    order.status = "已取消";//处理已取消流程...
                    System.out.println("==================================> 订单：id=【" + order.orderNo + "】超过支付时间未支付，已取消，orderInfo=" + order);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
