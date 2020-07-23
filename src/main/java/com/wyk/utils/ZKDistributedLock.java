package com.wyk.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZKDistributedLock {

	public static long value = 1L;

	public static final Logger LOGGER = LoggerFactory.getLogger(ZKDistributedLock.class);

	private static CuratorFramework curator = null;
	private static zkListener listener = null;
	private static ThreadLocal< Map<String,InterProcessSemaphoreMutex> > lockLocal = new ThreadLocal<Map<String,InterProcessSemaphoreMutex>>();

	private static String lock = "/lock/";

	public synchronized static void init(String servers, String pwd) {
		if (curator == null) {
			curator = CuratorFrameworkFactory.builder().sessionTimeoutMs(30000).connectionTimeoutMs(30000)
					.retryPolicy(new ExponentialBackoffRetry(1000, Integer.MAX_VALUE)).connectString(servers).build();
			listener = new zkListener();
			curator.getConnectionStateListenable().addListener(listener);
			curator.start();
		}
	}

	public static boolean acquire(String key) {
		if (lockLocal.get() == null){
			Map<String,InterProcessSemaphoreMutex> lockMap = new HashMap<>();
			lockLocal.set(lockMap);
		}
		try {
			InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(ZKDistributedLock.curator, ZKDistributedLock.lock + key);
			lock.acquire();
			lockLocal.get().put(key,lock);


		} catch (IllegalMonitorStateException e) {
			LOGGER.warn("ZKDistributedLock acquire error", e);
			return false;
		} catch (Exception e) {
			LOGGER.warn("ZKDistributedLock acquire error", e);
			return false;
		}

		return true;
	}

	public static boolean acquire(String key, long timeout) {
		try {
			InterProcessSemaphoreMutex lock = new InterProcessSemaphoreMutex(ZKDistributedLock.curator, ZKDistributedLock.lock + key);
			lock.acquire(timeout, TimeUnit.SECONDS);
			lockLocal.get().put(key,lock);

		} catch (Exception e) {
			LOGGER.warn("ZKDistributedLock acquire error", e);
			return false;
		}

		return true;
	}

	public static void release(String key) {
		try {
			InterProcessSemaphoreMutex lock = lockLocal.get().get(key);
			if(lock != null){
				lock.release();
				lockLocal.get().remove(key);
			}
		} catch (Exception e) {
			LOGGER.warn("ZKDistributedLock release error", e);
		}
	}



}

class zkListener implements ConnectionStateListener {

	public static final Logger LOGGER = LoggerFactory.getLogger(ZKDistributedLock.class);

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState state) {
		if (state == ConnectionState.LOST) {
			// 连接丢失
			LOGGER.warn("ZKDistributedLock lost session with zookeeper");
			// System.out.println("ZKDistributedLock lost session with zookeeper");
		} else if (state == ConnectionState.CONNECTED) {
			// 连接新建
			LOGGER.warn("ZKDistributedLock connected with zookeeper");
			// System.out.println("ZKDistributedLock connected with zookeeper");
		} else if (state == ConnectionState.RECONNECTED) {
			// 重新连接
			LOGGER.warn("ZKDistributedLock reconnected with zookeeper");
			// System.out.println("ZKDistributedLock reconnected with zookeeper");
		}
	}

}
