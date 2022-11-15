package com.kkb.dts.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 带监控信息的阻塞队列，当达到 队列阈值的时候会开始计算队列增长率用于减缓 放入队列的速度
 * @author zhangyang
 */
@Slf4j
public class MetricLinkedBlockingQueue<E> implements BlockingQueue<E> {

    private final BlockingQueue<E> blockingQueue;
    private final ScheduledExecutorService queueMonitor;
    /**
     * 队列容量大小
     */
    private final int capacity;

    /**
     * 触发计算增长率的阈值
     */
    private final double threshold;

    /**
     * 队列名称
     */
    @Getter
    private final String queueName;

    /**
     * 队列空闲率
     */
    private volatile double remainingRatio;

    /**
     * 队列当前的使用量
     */
    private volatile int useCapacity;

    /**
     * 队列空闲大小
     */
    private volatile int remainingCapacity;

    /**
     * 最后一次记录的队列 大小
     */
    private volatile int lastSize;

    /**
     * 队列增长率, 通过增长率反向观察出队列的消费速率
     */
    private volatile double incrementRatio;

    /**
     * 队列消费速率
     */
    private volatile double consumeRatio;

    /**
     * 当前队列的监控的开启状态
     */
    private AtomicBoolean monitorStarted = new AtomicBoolean(false);

    public MetricLinkedBlockingQueue(String queueName, int capacity, double threshold) {
        this.blockingQueue = new LinkedBlockingDeque<>(capacity);
        this.capacity = capacity;
        this.queueMonitor = new ScheduledThreadPoolExecutor(2);
        this.threshold = threshold;
        this.queueName = queueName;
    }

    /**
     * 手动调用当前方法来打开当前的队列监控
     */
    public void startMonitor(){
        //每一秒统计一次当前队列使用率
        if (monitorStarted.compareAndSet(false, true))
            initMonitor();
    }

    private void initMonitor() {
        //计算队列的空闲率
        queueMonitor.scheduleAtFixedRate(this::computeCapacity, 0, 1, TimeUnit.SECONDS);
        //计算队列的增长率率
        queueMonitor.scheduleAtFixedRate(this::computeIncrementRatio, 0, 3, TimeUnit.SECONDS);
        //打印队列监控信息
        queueMonitor.scheduleAtFixedRate(this::printMetric, 0, 3, TimeUnit.SECONDS);
    }

    private void computeCapacity() {
        //查看当前队列空闲率
        this.remainingRatio = (double) blockingQueue.remainingCapacity() / (double) capacity;
        //查看当前队列的使用大小
        this.useCapacity = blockingQueue.size();
        //查看队列的空闲大小
        this.remainingCapacity = blockingQueue.remainingCapacity();
    }


    /**
     * 计算当前队列的增长率
     */
    private void computeIncrementRatio() {
        int size = blockingQueue.size();

        if (size >= capacity * threshold && lastSize > 0) {
            this.incrementRatio = ((double) (size - lastSize)) / (double) lastSize;
        }

        if (size < threshold) {
            incrementRatio = 0;
            lastSize = 0;
            return;
        }

        if (size == capacity)
            incrementRatio = 1;

        this.lastSize = size;
    }


    private void printMetric() {
        log.info("Queue for {} info: [useCapacity: {}, remainingRatio:{}, incrementRatio: {}]", queueName, useCapacity, remainingRatio, incrementRatio);
    }


    @Override
    public boolean add(E e) {
        return blockingQueue.add(e);
    }

    @Override
    public boolean offer(E e) {
        return blockingQueue.offer(e);
    }

    @Override
    public void put(E e) throws InterruptedException {
        blockingQueue.put(e);
    }

    @Override
    public boolean offer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        return blockingQueue.offer(e, timeout, unit);
    }

    @Override
    public E take() throws InterruptedException {
        return blockingQueue.take();
    }

    @Override
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return blockingQueue.poll(timeout, unit);
    }

    @Override
    public int remainingCapacity() {
        return blockingQueue.remainingCapacity();
    }

    @Override
    public boolean remove(Object o) {
        return blockingQueue.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return blockingQueue.contains(o);
    }

    @Override
    public int drainTo(Collection<? super E> c) {
        return blockingQueue.drainTo(c);
    }

    @Override
    public int drainTo(Collection<? super E> c, int maxElements) {
        return blockingQueue.drainTo(c, maxElements);
    }

    @Override
    public E remove() {
        return blockingQueue.remove();
    }

    @Override
    public E poll() {
        return blockingQueue.poll();
    }

    @Override
    public E element() {
        return blockingQueue.element();
    }

    @Override
    public E peek() {
        return blockingQueue.peek();
    }

    @Override
    public int size() {
        return blockingQueue.size();
    }

    @Override
    public boolean isEmpty() {
        return blockingQueue.isEmpty();
    }

    @Override
    public Iterator<E> iterator() {
        return blockingQueue.iterator();
    }

    @Override
    public Object[] toArray() {
        return blockingQueue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return blockingQueue.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return blockingQueue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        return blockingQueue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return blockingQueue.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return blockingQueue.retainAll(c);
    }

    @Override
    public void clear() {
        blockingQueue.clear();
    }
}
