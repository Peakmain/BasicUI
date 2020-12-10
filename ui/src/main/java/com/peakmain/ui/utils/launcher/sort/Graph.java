package com.peakmain.ui.utils.launcher.sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Vector;

/**
 * author ：Peakmain
 * createTime：2020/12/10
 * mail:2726449200@qq.com
 * describe：有向无环图拓扑算法
 */
public class Graph {
    //顶点数量
    private int mVerticeCount;
    //领接表
    private List<Integer>[] adj;

    //边的数量
    private int mEdgeCount;

    public Graph(int count) {
        this.mVerticeCount = count;
        adj = new ArrayList[count];
        for (int i = 0; i < mVerticeCount; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    /**
     * 添加边,方向u->v
     *
     * @param u from
     * @param v to
     */
    public void addEdge(int u, int v) {
        adj[u].add(v);
        mEdgeCount++;
    }

    //获取边的数量
    public int getEdgeCount() {
        return mEdgeCount;
    }

    /**
     * 拓扑排序
     */
    public Vector<Integer> topologicalSort() {
        //1、遍历所有的顶点的邻接表，添加入度
        int[] indegree = new int[mVerticeCount];
        for (int i = 0; i < mVerticeCount; i++) {
            ArrayList<Integer> temp = (ArrayList<Integer>) adj[i];
            for (int node : temp) {
                indegree[node]++;
            }
        }
        //2、找到入度为0的head task
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < mVerticeCount; i++) {
            if (indegree[i] == 0) {
                queue.add(i);
            }
        }
        //3、进行拓扑排序
        int cnt = 0;
        Vector<Integer> result = new Vector<>();
        while (!queue.isEmpty()) {
            Integer u = queue.poll();
            result.add(u);
            //0的所有领接表
            for (Integer node : adj[u]) {
                //如果入度数为0，则添加队列中
                if (--indegree[node] == 0) {
                    queue.add(node);
                }
            }
            cnt++;
        }
        //4、判断当前是否有环
        if (cnt != mVerticeCount) {
            throw new IllegalStateException("图中不可存在环");
        }
        return result;
    }

}
