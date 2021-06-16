package com.peakmain.ui.utils.launcher.sort

import androidx.collection.ArraySet
import com.peakmain.ui.utils.LogUtils
import com.peakmain.ui.utils.launcher.task.Task
import java.util.*
import kotlin.collections.ArrayList

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：
 */
object TaskSortUtil {
    // 高优先级的Task
    private val sNewTasksHigh: MutableList<Task> = ArrayList()
    var isPrintTask = false

    /**
     * 任务的有向无环图的拓扑排序
     */
    @JvmStatic
    @Synchronized
    fun getSortResult(originTasks: List<Task>,
                      clsLaunchTasks: List<Class<out Task>>): List<Task> {
        val makeTime = System.currentTimeMillis()
        val dependSet: MutableSet<Int> = ArraySet()
        val graph = Graph(originTasks.size)
        //添加边
        for (i in originTasks.indices) {
            val task = originTasks[i]
            if (task.isSend || task.dependsOn() == null || task.dependsOn()?.size == 0) continue
            task.dependsOn()!!.forEach { cls ->
                val index = getIndexOfTask(originTasks, clsLaunchTasks, cls!!)
                check(index >= 0) {
                    task.javaClass.simpleName +
                            " depends on " + cls.simpleName + " can not be found in task list "
                }
                dependSet.add(index)
                graph.addEdge(index, i)
            }
        }
        val indexList = graph.topologicalSort()
        val newTasksAll = getResultTasks(originTasks, dependSet, indexList)
        LogUtils.normal("——————————————————————-拓扑排序后的结果——————————————————————-")
        printAllTaskName(newTasksAll)
        return newTasksAll
    }

    /**
     * @param originTasks 原本的taks集合
     * @param dependSet   依赖的所有的index集合
     * @param indexList   拓扑排序后的index集合
     */
    private fun getResultTasks(originTasks: List<Task>,
                               dependSet: Set<Int>, indexList: List<Int>): List<Task> {
        LogUtils.normal("——————————————————————-拓扑排序前的结果——————————————————————-")
        printAllTaskName(originTasks)
        val newsTaskAll: MutableList<Task> = ArrayList(originTasks.size)
        //被别人依赖的
        val newsTaskDepended: MutableList<Task> = ArrayList()
        // 没有依赖的
        val newTasksWithOutDepend: MutableList<Task> = ArrayList()
        // 需要提升自己优先级的，先执行（这个先是相对于没有依赖的先）
        val newTasksRunAsSoon: MutableList<Task> = ArrayList()
        for (index in indexList) {
            if (dependSet.contains(index)) {
                newsTaskDepended.add(originTasks[index])
            } else {
                val task = originTasks[index]
                if (task.needRunAsSoon()) {
                    newTasksRunAsSoon.add(task)
                } else {
                    newTasksWithOutDepend.add(task)
                }
            }
        }
        // 顺序：被别人依赖的————》需要提升自己优先级的————》需要被等待的————》没有依赖的
        sNewTasksHigh.addAll(newsTaskDepended)
        sNewTasksHigh.addAll(newTasksRunAsSoon)
        newsTaskAll.addAll(sNewTasksHigh)
        newsTaskAll.addAll(newTasksWithOutDepend)
        return newsTaskAll
    }

    private fun printAllTaskName(newTasksAll: List<Task>) {
        if (isPrintTask) {
            return
        }
        val sb = StringBuilder()
        for (task in newTasksAll) {
            sb.append(task.javaClass.simpleName).append("->")
        }
        sb.substring(0, sb.length - 1)
        LogUtils.i(sb.toString())
    }

    /**
     * 获取任务在任务列表中的index
     *
     * @param originTasks    originTasks
     * @param clsLaunchTasks clsLaunchTasks
     */
    private fun getIndexOfTask(originTasks: List<Task>,
                               clsLaunchTasks: List<Class<out Task>>, cls: Class<out Task>): Int {
        val index = clsLaunchTasks.indexOf(cls)
        if (index >= 0) {
            return index
        }

        // 仅仅是保护性代码
        val size = originTasks.size
        for (i in 0 until size) {
            if (cls.simpleName == originTasks[i].javaClass.simpleName) {
                return i
            }
        }
        return index
    }
}