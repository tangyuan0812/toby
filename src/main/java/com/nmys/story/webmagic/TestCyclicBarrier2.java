package com.nmys.story.webmagic;

import java.util.concurrent.CyclicBarrier;

public class TestCyclicBarrier2 {

	private static CyclicBarrier cyclicBarrier;

	static class CyclicBarrierThread extends Thread {
		public void run() {
			System.out.println(Thread.currentThread().getName() + "到了");
			// 等待
			try {
				cyclicBarrier.await();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		System.out.println("start---------------");
		cyclicBarrier = new CyclicBarrier(100, new Runnable() {
			@Override
			public void run() {
				System.out.println("人到齐了，开会吧....");
				System.out.println("end---------------");
			}
		});

		for (int i = 0; i < 100; i++) {
			new CyclicBarrierThread().start();
		}
	}

}
