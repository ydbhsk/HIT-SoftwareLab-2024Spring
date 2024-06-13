
public class MyTest {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            System.out.print("\r" + i);
            Thread.sleep(100);  // 暂停100毫秒以模拟实时更新
        }
    }
}
