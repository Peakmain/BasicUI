package com.peakmain.ui.utils.launcher.sort;

import android.support.v4.util.ArraySet;
import android.util.Log;

import com.peakmain.ui.utils.LogUtils;
import com.peakmain.ui.utils.launcher.task.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
public class TaskSortUtil {
    // 高优先级的Task
    private static List<Task> sNewTasksHigh = new ArrayList<>();
    public static boolean isPrintTask = false;

    /**
     * 任务的有向无环图的拓扑排序
     */
    public static synchronized List<Task> getSortResult(List<Task> originTasks,
                                                        List<Class<? extends Task>> clsLaunchTasks) {
        long makeTime = System.currentTimeMillis();
        Set<Integer> dependSet = new ArraySet<>();
        Graph graph = new Graph(originTasks.size());
        //添加边
        for (int i = 0; i < originTasks.size(); i++) {
            Task task = originTasks.get(i);
            if (task.isSend() || task.dependsOn() == null || task.dependsOn().size() == 0)
                continue;
            for (Class<? extends Task> cls : task.dependsOn()) {
                int index = getIndexOfTask(originTasks, clsLaunchTasks, cls);
                if (index < 0) {
                    throw new IllegalStateException(task.getClass().getSimpleName() +
                            " depends on " + cls.getSimpleName() + " can not be found in task list ");
                }
                dependSet.add(index);
                graph.addEdge(index, i);
            }
        }
        Vector<Integer> indexList = graph.topologicalSort();
        List<Task> newTasksAll = getResultTasks(originTasks, dependSet, indexList);
        LogUtils.normal("——————————————————————-拓扑排序后的结果——————————————————————-");
        printAllTaskName(newTasksAll);
        return newTasksAll;
    }

    /**
     * @param originTasks 原本的taks集合
     * @param dependSet   依赖的所有的index集合
     * @param indexList   拓扑排序后的index集合
     */
    private static List<Task> getResultTasks(List<Task> originTasks,
                                             Set<Integer> dependSet, List<Integer> indexList) {
        LogUtils.normal("——————————————————————-拓扑排序前的结果——————————————————————-");
        printAllTaskName(originTasks);
        List<Task> newsTaskAll = new ArrayList<>(originTasks.size());
        //被别人依赖的
        List<Task> newsTaskDepended = new ArrayList<>();
        // 没有依赖的
        List<Task> newTasksWithOutDepend = new ArrayList<>();
        // 需要提升自己优先级的，先执行（这个先是相对于没有依赖的先）
        List<Task> newTasksRunAsSoon = new ArrayList<>();
        for (Integer index : indexList) {
            if (dependSet.contains(index)) {
                newsTaskDepended.add(originTasks.get(index));
            } else {
                Task task = originTasks.get(index);
                if (task.needRunAsSoon()) {
                    newTasksRunAsSoon.add(task);
                } else {
                    newTasksWithOutDepend.add(task);
                }
            }
        }
        // 顺序：被别人依赖的————》需要提升自己优先级的————》需要被等待的————》没有依赖的
        sNewTasksHigh.addAll(newsTaskDepended);
        sNewTasksHigh.addAll(newTasksRunAsSoon);
        newsTaskAll.addAll(sNewTasksHigh);
        newsTaskAll.addAll(newTasksWithOutDepend);
        return newsTaskAll;
    }

    private static void printAllTaskName(List<Task> newTasksAll) {
        if (isPrintTask) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (Task task : newTasksAll) {
            sb.append(task.getClass().getSimpleName()).append("->");
        }
        sb.substring(0, sb.length() - 1);
        LogUtils.i(sb.toString());
    }

    /**
     * 获取任务在任务列表中的index
     *
     * @param originTasks    originTasks
     * @param clsLaunchTasks clsLaunchTasks
     */
    private static int getIndexOfTask(List<Task> originTasks,
                                      List<Class<? extends Task>> clsLaunchTasks, Class<? extends Task> cls) {
        int index = clsLaunchTasks.indexOf(cls);
        if (index >= 0) {
            return index;
        }

        // 仅仅是保护性代码
        final int size = originTasks.size();
        for (int i = 0; i < size; i++) {
            if (cls.getSimpleName().equals(originTasks.get(i).getClass().getSimpleName())) {
                return i;
            }
        }
        return index;
    }
}
