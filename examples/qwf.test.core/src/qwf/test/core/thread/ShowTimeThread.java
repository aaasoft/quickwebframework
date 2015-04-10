package qwf.test.core.thread;

import java.util.Calendar;

import org.springframework.stereotype.Component;

//@Component
public class ShowTimeThread extends Thread {

	public void run() {

		try {
			while (true) {
				Thread.sleep(1000);
				System.out.println(String.format("线程[%s]:%s", this.getId(),
						Calendar.getInstance().toString()));
			}
		} catch (InterruptedException e) {
			System.out.println(String.format("线程[%s]:已被中断。", this.getId()));
		}
	}
}
