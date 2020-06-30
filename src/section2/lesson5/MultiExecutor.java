package section2.lesson5;

import java.util.List;

public class MultiExecutor {

    List<Runnable> tasks;
    /**
     * @param tasks to be executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        this.tasks = tasks;
    }

    /**
     * Starts and executes all the tasks concurrently
     */
    public void executeAll() {
        for (Runnable r : tasks) {
            new Thread(r).start();
        }
    }
}
