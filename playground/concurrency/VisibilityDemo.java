/**
 * L1 — Visibility: volatile vs plain field.
 *
 * Mục tiêu phỏng vấn: giải thích Java Memory Model, visibility, happens-before.
 *
 * Cách luyện:
 *  1. Chạy thử, quan sát thread reader có thể không thấy flag đổi (hoặc thấy chậm).
 *  2. Làm TODO-1: thêm `volatile` vào flag, chạy lại.
 *  3. Làm TODO-2: thay bằng AtomicBoolean, chạy lại.
 *
 * Run: javac VisibilityDemo.java && java -cp . VisibilityDemo
 */
import java.util.concurrent.atomic.AtomicBoolean;

public class VisibilityDemo {

    // TODO-1: thêm `volatile` vào dòng dưới rồi chạy lại, so sánh kết quả.
    static boolean running = true;

    // TODO-2: uncomment để thử bản Atomic (comment `running` ở trên lại).
    // static AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        Thread reader = new Thread(() -> {
            long spins = 0;
            while (running) { // TODO-2: đổi thành running.get() khi dùng AtomicBoolean
                spins++;
                if (spins % 50_000_000 == 0) {
                    System.out.println("... vẫn đang đợi flag (spins=" + spins + ")");
                    break; // tránh treo máy khi demo field không volatile
                }
            }
            System.out.println("reader thoát, spins=" + spins);
        });

        reader.start();
        Thread.sleep(300);
        System.out.println("main đổi flag -> false");
        running = false; // TODO-2: đổi thành running.set(false)
        reader.join(2000);

        if (reader.isAlive()) {
            System.out.println("KẾT QUẢ: reader chưa thấy flag (visibility problem!) -> thử thêm volatile.");
            reader.interrupt();
            reader.join();
        } else {
            System.out.println("KẾT QUẢ: OK — reader đã thấy flag.");
        }

        System.out.println("\nCâu hỏi phỏng vấn tự trả lời:");
        System.out.println("1. Vì sao plain boolean có thể không thấy đổi giữa 2 thread?");
        System.out.println("2. volatile đảm bảo gì (visibility + ordering), KHÔNG đảm bảo gì (atomicity)?");
        System.out.println("3. Khi nào dùng AtomicBoolean thay vì volatile?");
    }
}
