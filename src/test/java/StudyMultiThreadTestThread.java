public class StudyMultiThreadTestThread implements Runnable {
    int i;
    Object obj = new Object(); //同步的标记对象

    public StudyMultiThreadTestThread(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        synchronized (obj) { //不会有两个线程同时访问代码块
            for (i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + i);
            }
        }
    }
}
