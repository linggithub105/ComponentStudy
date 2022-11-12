

![](E:\workspace_idea\ComponentStudy\springboot-thread\src\main\java\com\itjing\thread\多线程永动任务\images\7b23528936ad425a9838d94711b4524btplv-k3u1fbpfcp-zoom-in-crop-mark3024000.webp)

大家好，我是楼仔！

今天教大家撸一个 Java 的多线程永动任务，**这个示例的原型是公司自研的多线程异步任务项目**，我把里面涉及到多线程的代码抽离出来，然后进行一定的改造。

里面涉及的知识点非常多，特别适合有**一定工作经验**的同学学习，或者可以直接拿到项目中使用。

文章结构非常简单：

![](E:\workspace_idea\ComponentStudy\springboot-thread\src\main\java\com\itjing\thread\多线程永动任务\images\b19742c6854d4811b5f2f98f4be96ad2tplv-k3u1fbpfcp-zoom-in-crop-mark3024000.webp)

1\. 功能说明
--------

做这个多线程异步任务，主要是因为我们有很多永动的异步任务，什么是永动呢？就是**任务跑起来后，需要一直跑下去。**

比如消息 Push 任务，因为一直有消息过来，所以需要一直去消费 DB 中的未推送消息，就需要整一个 Push 的永动异步任务。

我们的需求其实不难，简单总结一下：

1.  **能同时执行多个永动的异步任务**；
2.  每个异步任务，支持开**多个线程**去消费这个任务的数据；
3.  支持永动异步任务的**优雅关闭**，即关闭后，需要把所有的数据消费完毕后，再关闭。

完成上面的需求，需要注意几个点：

1.  每个**永动任务**，可以开一个线程去执行；
2.  每个**子任务**，因为需要支持并发，需要用线程池控制；
3.  永动任务的关闭，需要通知子任务的并发线程，并**支持永动任务和并发子任务的优雅关闭**。

2\. 多线程任务示例
-----------

### 2.1 线程池

对于子任务，需要支持并发，如果每个并发都开一个线程，用完就关闭，对资源消耗太大，所以引入线程池：

    public class TaskProcessUtil {
        // 每个任务，都有自己单独的线程池
        private static Map<String, ExecutorService> executors = new ConcurrentHashMap<>();
    
        // 初始化一个线程池
        private static ExecutorService init(String poolName, int poolSize) {
            return new ThreadPoolExecutor(poolSize, poolSize,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    new ThreadFactoryBuilder().setNameFormat("Pool-" + poolName).setDaemon(false).build(),
                    new ThreadPoolExecutor.CallerRunsPolicy());
        }
    
        // 获取线程池
        public static ExecutorService getOrInitExecutors(String poolName,int poolSize) {
            ExecutorService executorService = executors.get(poolName);
            if (null == executorService) {
                synchronized (TaskProcessUtil.class) {
                    executorService = executors.get(poolName);
                    if (null == executorService) {
                        executorService = init(poolName, poolSize);
                        executors.put(poolName, executorService);
                    }
                }
            }
            return executorService;
        }
    
        // 回收线程资源
        public static void releaseExecutors(String poolName) {
            ExecutorService executorService = executors.remove(poolName);
            if (executorService != null) {
                executorService.shutdown();
            }
        }
    }
    复制代码

这是一个线程池的工具类，这里初始化线程池和回收线程资源很简单，我们主要讨论获取线程池。

获取线程池可能会存在并发情况，所以需要加一个 synchronized 锁，然后锁住后，需要对 executorService 进行二次判空校验。

### 2.2 单个任务

为了更好讲解单个任务的实现方式，我们的任务主要就是把 Cat 的数据打印出来，Cat 定义如下：

    @Data
    @Service
    public class Cat {
        private String catName;
        public Cat setCatName(String name) {
            this.catName = name;
            return this;
        }
    }
    复制代码

单个任务主要包括以下功能：

*   **获取永动任务数据**：这里一般都是扫描 DB，我直接就简单用 queryData() 代替。
*   **多线程执行任务**：需要把数据拆分成 4 份，然后分别由多线程并发执行，这里可以通过线程池支持；
*   **永动任务优雅停机**：当外面通知任务需要停机，需要执行完剩余任务数据，并回收线程资源，退出任务；
*   **永动执行**：如果未收到停机命令，任务需要一直执行下去。

直接看代码：

    public class ChildTask {
    
        private final int POOL_SIZE = 3; // 线程池大小
        private final int SPLIT_SIZE = 4; // 数据拆分大小
        private String taskName;
    
        // 接收jvm关闭信号，实现优雅停机
        protected volatile boolean terminal = false;
    
        public ChildTask(String taskName) {
            this.taskName = taskName;
        }
    
        // 程序执行入口
        public void doExecute() {
            int i = 0;
            while(true) {
                System.out.println(taskName + ":Cycle-" + i + "-Begin");
                // 获取数据
                List<Cat> datas = queryData();
                // 处理数据
                taskExecute(datas);
                System.out.println(taskName + ":Cycle-" + i + "-End");
                if (terminal) {
                    // 只有应用关闭，才会走到这里，用于实现优雅的下线
                    break;
                }
                i++;
            }
            // 回收线程池资源
            TaskProcessUtil.releaseExecutors(taskName);
        }
    
        // 优雅停机
        public void terminal() {
            // 关机
            terminal = true;
            System.out.println(taskName + " shut down");
        }
    
        // 处理数据
        private void doProcessData(List<Cat> datas, CountDownLatch latch) {
            try {
                for (Cat cat : datas) {
                    System.out.println(taskName + ":" + cat.toString() + ",ThreadName:" + Thread.currentThread().getName());
                    Thread.sleep(1000L);
                }
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            } finally {
                if (latch != null) {
                    latch.countDown();
                }
            }
        }
    
        // 处理单个任务数据
        private void taskExecute(List<Cat> sourceDatas) {
            if (CollectionUtils.isEmpty(sourceDatas)) {
                return;
            }
            // 将数据拆成4份
            List<List<Cat>> splitDatas = Lists.partition(sourceDatas, SPLIT_SIZE);
            final CountDownLatch latch = new CountDownLatch(splitDatas.size());
    
            // 并发处理拆分的数据，共用一个线程池
            for (final List<Cat> datas : splitDatas) {
                ExecutorService executorService = TaskProcessUtil.getOrInitExecutors(taskName, POOL_SIZE);
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        doProcessData(datas, latch);
                    }
                });
            }
    
            try {
                latch.await();
            } catch (Exception e) {
                System.out.println(e.getStackTrace());
            }
        }
    
        // 获取永动任务数据
        private List<Cat> queryData() {
            List<Cat> datas = new ArrayList<>();
            for (int i = 0; i < 5; i ++) {
                datas.add(new Cat().setCatName("罗小黑" + i));
            }
            return datas;
        }
    }
    复制代码

简单解释一下：

*   **queryData**：用于获取数据，实际应用中其实是需要把 queryData 定为抽象方法，然后由各个任务实现自己的方法。
*   **doProcessData**：数据处理逻辑，实际应用中其实是需要把 doProcessData 定为抽象方法，然后由各个任务实现自己的方法。
*   **taskExecute**：将数据拆分成 4 份，获取该任务的线程池，并交给线程池并发执行，然后通过 latch.await() 阻塞。当这 4 份数据都执行成功后，阻塞结束，该方法才返回。
*   **terminal**：仅用于接受停机命令，这里该变量定义为 volatile，所以多线程内存可见；
*   **doExecute**：程序执行入口，封装了每个任务执行的流程，当 terminal=true 时，先执行完任务数据，然后回收线程池，最后退出。

### 2.3 任务入口

直接上代码：

    public class LoopTask {
        private List<ChildTask> childTasks;
        public void initLoopTask() {
            childTasks = new ArrayList();
            childTasks.add(new ChildTask("childTask1"));
            childTasks.add(new ChildTask("childTask2"));
            for (final ChildTask childTask : childTasks) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        childTask.doExecute();
                    }
                }).start();
            }
        }
        public void shutdownLoopTask() {
            if (!CollectionUtils.isEmpty(childTasks)) {
                for (ChildTask childTask : childTasks) {
                    childTask.terminal();
                }
            }
        }
        public static void main(String args[]) throws Exception{
            LoopTask loopTask = new LoopTask();
            loopTask.initLoopTask();
            Thread.sleep(5000L);
            loopTask.shutdownLoopTask();
        }
    }
    复制代码

每个任务都开一个单独的 Thread，这里我初始化了 2 个永动任务，分别为 childTask1 和 childTask2，然后分别执行，后面 Sleep 了 5 秒后，再关闭任务，我们可以看看是否可以按照我们的预期优雅退出。

### 2.4 结果分析

执行结果如下：

    childTask1:Cycle-0-Begin
    childTask2:Cycle-0-Begin
    childTask1:Cat(catName=罗小黑0),ThreadName:Pool-childTask1
    childTask1:Cat(catName=罗小黑4),ThreadName:Pool-childTask1
    childTask2:Cat(catName=罗小黑4),ThreadName:Pool-childTask2
    childTask2:Cat(catName=罗小黑0),ThreadName:Pool-childTask2
    childTask1:Cat(catName=罗小黑1),ThreadName:Pool-childTask1
    childTask2:Cat(catName=罗小黑1),ThreadName:Pool-childTask2
    childTask2:Cat(catName=罗小黑2),ThreadName:Pool-childTask2
    childTask1:Cat(catName=罗小黑2),ThreadName:Pool-childTask1
    childTask2:Cat(catName=罗小黑3),ThreadName:Pool-childTask2
    childTask1:Cat(catName=罗小黑3),ThreadName:Pool-childTask1
    childTask2:Cycle-0-End
    childTask2:Cycle-1-Begin
    childTask1:Cycle-0-End
    childTask1:Cycle-1-Begin
    childTask2:Cat(catName=罗小黑0),ThreadName:Pool-childTask2
    childTask2:Cat(catName=罗小黑4),ThreadName:Pool-childTask2
    childTask1:Cat(catName=罗小黑4),ThreadName:Pool-childTask1
    childTask1:Cat(catName=罗小黑0),ThreadName:Pool-childTask1
    childTask1 shut down
    childTask2 shut down
    childTask2:Cat(catName=罗小黑1),ThreadName:Pool-childTask2
    childTask1:Cat(catName=罗小黑1),ThreadName:Pool-childTask1
    childTask1:Cat(catName=罗小黑2),ThreadName:Pool-childTask1
    childTask2:Cat(catName=罗小黑2),ThreadName:Pool-childTask2
    childTask1:Cat(catName=罗小黑3),ThreadName:Pool-childTask1
    childTask2:Cat(catName=罗小黑3),ThreadName:Pool-childTask2
    childTask1:Cycle-1-End
    childTask2:Cycle-1-End
    复制代码

输出数据：

*   “Pool-childTask” 是线程池名称；
*   “childTask” 是任务名称；
*   “Cat(catName=罗小黑)” 是执行的结果；
*   “childTask shut down” 是关闭标记；
*   “childTask:Cycle-X-Begin” 和“childTask:Cycle-X-End” 是每一轮循环的开始和结束标记。

我们分析一下执行结果：

*   childTask1 和 childTask2 分别执行，在第一轮循环中都正常输出了 5 条罗小黑数据；
*   第二轮执行过程中，我启动了关闭指令，这次第二轮执行没有直接停止，而是先执行完任务中的数据，再执行退出，所以完全符合我们的优雅退出结论。

### 2.5 源码地址

GitHub 地址：

    https://github.com/lml200701158/java-study/tree/master/src/main/java/com/java/parallel/pool/ofc
    复制代码

3\. 写在最后
========

对于这个经典的线程池使用示例，原项目是我好友**一灰**写的，技术水平对标阿里 P7，**实现得也非常优雅，涉及的知识点非常多**，非常值得大家学习。

如果对这个示例有任何疑问，可以加我微信，随时沟通交流哈。
