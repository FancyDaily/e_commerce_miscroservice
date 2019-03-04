package com.e_commerce.miscroservice.commons.util.colligate;
/**
 * 功能描述:
 * 模块:
 * 项目:
 * 版本号:
 * 部门:技术研发部
 * 公司:浙江晓时信息技术有限公司
 * 作者:丁卫江
 * 邮箱:1664315156@qq.com
 * 创建时间:Sep 7, 2017 7:40:07 PM
 *************************************
 *************************************
 * 修改人:
 * 修改时间:
 * 修改内容:
 * 1.
 * 2.
 */
public class IdWorker {
	private static long workerId = 2L;
	private final static long twepoch = 1361753741828L;

	private long sequence = 0L;

	private final static long workerIdBits = 4L;

	public final static long maxWorkerId = -1L ^ -1L << workerIdBits;

	private final static long sequenceBits = 10L;

	private final static long workerIdShift = sequenceBits;

	private final static long timestampLeftShift = sequenceBits + workerIdBits;

	public final static long sequenceMask = -1L ^ -1L << sequenceBits;

	private long lastTimestamp = -1L;

	private static volatile IdWorker worker = new IdWorker(IdWorker.workerId);
	
	private IdWorker( long workerId) {

		super();

		if (workerId > maxWorkerId || workerId < 0) {

			throw new IllegalArgumentException(String.format(

					"worker Id can't be greater than %d or less than 0",

					maxWorkerId));

		}

		IdWorker.workerId = workerId;

	}

	public static IdWorker getInstance(){		
		return worker;
	}
	
	
	public synchronized long nextId() {

		long timestamp = this.timeGen();

		if (this.lastTimestamp == timestamp) {

			this.sequence = (this.sequence + 1) & sequenceMask;

			if (this.sequence == 0) {

				System.out.println("###########" + sequenceMask);

				timestamp = this.tilNextMillis(this.lastTimestamp);

			}

		} else {

			this.sequence = 0;

		}

		if (timestamp < this.lastTimestamp) {

			try {

				throw new Exception(

						String.format(

								"Clock moved backwards.  Refusing to generate id for %d milliseconds",

								this.lastTimestamp - timestamp));

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		this.lastTimestamp = timestamp;

		long nextId = ((timestamp - twepoch << timestampLeftShift))

				| (IdWorker.workerId << workerIdShift) | (this.sequence);

		return nextId;

	}

	private long tilNextMillis(final long lastTimestamp) {

		long timestamp = this.timeGen();

		while (timestamp <= lastTimestamp) {

			timestamp = this.timeGen();

		}

		return timestamp;

	}

	private long timeGen() {

		return System.currentTimeMillis();

	}
	
	
	public static void setWorkerId(long workerId) {
		IdWorker.workerId = workerId;
	}

	public static void main(String[] args) {

		IdWorker.setWorkerId(2);
		System.out.println(IdWorker.workerId);
		System.out.println(IdWorker.getInstance().nextId());
		
		IdWorker.setWorkerId(3);
		System.out.println(IdWorker.workerId);
		System.out.println(IdWorker.getInstance().nextId());
		
		IdWorker.setWorkerId(4);
		System.out.println(IdWorker.workerId);
		System.out.println(IdWorker.getInstance().nextId());
		
		IdWorker.setWorkerId(5);
		System.out.println(IdWorker.workerId);
		System.out.println(IdWorker.getInstance().nextId());
		
		IdWorker.setWorkerId(6);
		System.out.println(IdWorker.workerId);
		System.out.println(IdWorker.getInstance().nextId());

	}

}
