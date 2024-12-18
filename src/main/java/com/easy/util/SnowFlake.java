package com.easy.util;

/**
 * Twitter的分布式自增ID雪花算法snowflake
 * SnowFlake的结构如下(每部分用-分开):
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 * 加起来刚好64位，为一个Long型。
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高。
 * 经测试，SnowFlake每秒能够产生26万ID左右。
 */
@SuppressWarnings("all")
public class SnowFlake {

    // 因为二进制里第一个 bit 为如果是 1，那么都是负数，但是我们生成的 id 都是正数，所以第一个 bit 统一都是 0

    /**
     * 起始的时间戳
     */
    private static final long START_STAMP = 1480166465631L;

    /**
     * 每一部分占用的位数
     */
    private static final long SEQUENCE_BIT = 12; // 序列号占用的位数
    private static final long MACHINE_BIT = 5; // 机器标识占用的位数
    private static final long DATACENTER_BIT = 5; // 数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    // 这个是一个意思，就是5 bit最多只能有31个数字，机房id最多只能是32以内
    private static final long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    // 这个是二进制运算，就是5 bit最多只能有31个数字，也就是说机器id最多只能是32以内
    private static final long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    // 每毫秒内产生的id数 2 的 12次方
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private static final long MACHINE_LEFT = SEQUENCE_BIT;
    private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private static final long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    // 机房ID 2进制5位  32位减掉1位 31个
    private long datacenterId; // 数据中心、机房ID
    // 机器ID  2进制5位  32位减掉1位 31个
    private long machineId; // 机器标识
    // 代表一毫秒内生成的多个id的最新序号  12位 4096 -1 = 4095 个
    private long sequence = 0L; // 序列号
    // 记录产生时间毫秒数，判断是否是同1毫秒
    private long lastTimestamp = -1L; // 上一次时间戳

    public SnowFlake(long machineId, long datacenterId) {
        // 检查机房id和机器id是否超过31 不能小于0
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException(
                    "datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException(
                    "machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * 这个是核心方法，通过调用nextId()方法，让当前这台机器上的snowflake算法程序生成一个全局唯一的id
     */
    public synchronized long nextId() {
        long currentTimestamp = getNewsTimestamp();
        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        // 下面是说假设在同一个毫秒内，又发送了一个请求生成一个id
        // 这个时候就得把seqence序号给递增1，最多就是4096
        if (currentTimestamp == lastTimestamp) {
            // 这个意思是说一个毫秒内最多只能有4096个数字，无论你传递多少进来，
            // 这个位运算保证始终就是在4096这个范围内，避免你自己传递个sequence超过了4096这个范围
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // 当某一毫秒的时间，产生的id数 超过4095，系统会进入等待，直到下一毫秒，系统继续产生ID
            if (sequence == 0L) {
                currentTimestamp = getNextMill();
            }
        } else {
            // 不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimestamp = currentTimestamp;
        // 最核心的二进制位运算操作，生成一个64bit的id
        // 先将当前时间戳左移，放到41 bit那儿；将机房id左移放到5 bit那儿；将机器id左移放到5 bit那儿；将序号放最后12 bit
        // 最后拼接起来成一个64 bit的二进制数字，转换成10进制就是个long型
        return (currentTimestamp - START_STAMP) << TIMESTMP_LEFT // 时间戳部分
                | datacenterId << DATACENTER_LEFT // 数据中心部分
                | machineId << MACHINE_LEFT // 机器标识部分
                | sequence; // 序列号部分
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long getNextMill() {
        long mill = getNewsTimestamp();
        while (mill <= lastTimestamp) {
            mill = getNewsTimestamp();
        }
        return mill;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return 当前时间(毫秒)
     */
    private long getNewsTimestamp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(1, 1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            System.out.println("当前生成的有序数字串 : " + (snowFlake.nextId()));
        }
        System.out.println("总耗时 : " + (System.currentTimeMillis() - start));
        System.out.println("MAX_MACHINE_NUM : " + MAX_MACHINE_NUM);
        System.out.println("MAX_DATACENTER_NUM : " + MAX_DATACENTER_NUM);
        System.out.println("MAX_SEQUENCE : " + MAX_SEQUENCE);
    }
}
