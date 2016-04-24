package com.android.liuzhuang.chochttplibrary.core;

import com.android.liuzhuang.chochttplibrary.request.BaseRequest;
import com.android.liuzhuang.chochttplibrary.utils.Logger;
import com.android.liuzhuang.chochttplibrary.utils.ThreadUtil;

import java.util.*;
import java.util.concurrent.*;

/**
 * <p>The dispatcher of ChocHttp. When a new task is added, this class will decide executing the task or adding to the queue. </p>
 * Created by liuzhuang on 16/3/29.
 */
public final class Dispatcher implements ICallFinishListener {
    public static volatile Dispatcher instance;
    private int maxRequests = 64;
    private int maxRequestsPerHost = 5;

    /** Executes calls. Created lazily. */
    private ExecutorService executorService;

    /** Ready async calls in the order they'll be run. */
    private final Deque<AsyncCall> readyAsyncCalls = new ArrayDeque<AsyncCall>();

    /** Running asynchronous calls. Includes canceled calls that haven't finished yet. */
    private final Deque<AsyncCall> runningAsyncCalls = new ArrayDeque<AsyncCall>();

    public static Dispatcher getInstance() {
        if (instance == null) {
            synchronized (Dispatcher.class) {
                if (instance == null) {
                    instance = new Dispatcher();
                }
            }
        }
        return instance;
    }

    private Dispatcher() {
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), ThreadUtil.getThreadFactory("ChocHttp Dispatcher", false));
        }
        return executorService;
    }

    public synchronized void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
        promoteCalls();
    }

    public synchronized void setMaxRequestsPerHost(int maxRequestsPerHost) {
        this.maxRequestsPerHost = maxRequestsPerHost;
        promoteCalls();
    }

    public synchronized void dispatch(AsyncCall call) {
        if (call == null) {
            throw new NullPointerException("the Call can not be null");
        }
        call.setListener(this);
        if (runningAsyncCalls.size() < maxRequests && runningCallsForHost(call) < maxRequestsPerHost) {
            runningAsyncCalls.add(call);
            executorService().execute(call);
        } else {
            readyAsyncCalls.add(call);
        }
    }

    /** Returns the number of running calls that share a host with {@code call}. */
    private int runningCallsForHost(AsyncCall call) {
        int result = 0;
        for (AsyncCall c : runningAsyncCalls) {
            if (c.getHost().equals(call.getHost())) result++;
        }
        return result;
    }

    /**
     * Cancel all tasks, both running and ready tasks.
     */
    public synchronized void cancelAll() {
        for (AsyncCall call : readyAsyncCalls) {
            call.cancel();
        }

        for (AsyncCall call : runningAsyncCalls) {
            call.cancel();
        }
    }

    /**
     * cancel a single task
     * @param call
     */
    public synchronized void cancel(AsyncCall call) {
        if (call != null) {
            call.cancel();
        }
    }

    /**
     * cancel a single task that has same request with param request.
     * @param request
     */
    public synchronized void cancel(BaseRequest request) {
        if (request != null) {
            for (AsyncCall call : runningAsyncCalls) {
                if (call.getRequest() == request) {
                    cancel(call);
                }
            }

            for (AsyncCall call : readyAsyncCalls) {
                if (call.getRequest() == request) {
                    cancel(call);
                }
            }
        }
    }

    /**
     * Check the readyAsyncCalls queue and execute task in it.
     */
    private void promoteCalls() {
        if (runningAsyncCalls.size() >= maxRequests) return; // Already running max capacity.
        if (readyAsyncCalls.isEmpty()) return; // No ready calls to promote.

        for (Iterator<AsyncCall> i = readyAsyncCalls.iterator(); i.hasNext(); ) {
            AsyncCall call = i.next();

            if (runningCallsForHost(call) < maxRequestsPerHost) {
                i.remove();
                runningAsyncCalls.add(call);
                executorService().execute(call);
            }

            if (runningAsyncCalls.size() >= maxRequests) return; // Reached max capacity.
        }
    }

    /**
     * A task is finished.
     * @param call
     */
    public synchronized void onFinish(AsyncCall call) {
        Logger.println("onFinish");
        if (runningAsyncCalls != null) {
            if (!runningAsyncCalls.remove(call)) {
                Logger.println("remove fail " + call);
            }
            promoteCalls();
        }
    }

    /**
     * A task is canceled.
     * @param call
     */
    public synchronized void onCanceled(AsyncCall call) {
        Logger.println("onCancel");
        if (runningAsyncCalls != null) {
            if (!runningAsyncCalls.remove(call)) {
                Logger.println("remove fail " + call);
            }
            promoteCalls();
        }
    }
}
