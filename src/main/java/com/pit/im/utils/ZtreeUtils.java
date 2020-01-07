package com.pit.im.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author: deng
 * @Date: 2019/10/12 16:39
 * @Version 1.0
 */
public class ZtreeUtils {

    /**
     * 转树结构
     *
     * @param ztreeList
     * @return
     */
    public static List<Ztree> getZreeList(List<Ztree> ztreeList) {
        List<Ztree> parents = new ArrayList<>();
        for (Ztree ztree : ztreeList) {
            if ("null".equals(ztree.getPId()) || StringUtils.isEmpty(ztree.getPId())) {
                parents.add(ztree);
            }
        }
        for (Ztree p : parents) {
            List<Ztree> childPerms = getChildPerms(ztreeList, p.getId());
            p.setChildren(childPerms);
        }

        return parents;


    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public static List<Ztree> getChildPerms(List<Ztree> list, String parentId) {
        parentId = null == parentId ? "" : parentId;
        List<Ztree> returnList = new ArrayList<>();
        for (Iterator<Ztree> iterator = list.iterator(); iterator.hasNext(); ) {
            Ztree t = iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (parentId.equals(t.getPId())) {
                returnList.add(t);
                recursionFn(list, t);
            }
        }
        return returnList;
    }


    /**
     * 递归列表
     *
     * @param list
     * @param t
     */
    private static void recursionFn(List<Ztree> list, Ztree t) {
        // 得到子节点列表
        List<Ztree> childList = getChildList(list, t);
        t.setChildren(childList);
        for (Ztree tChild : childList) {
            if (hasChild(list, tChild)) {
                // 判断是否有子节点
                Iterator<Ztree> it = childList.iterator();
                while (it.hasNext()) {
                    Ztree n = (Ztree) it.next();
                    recursionFn(list, n);
                }
            }
        }
    }


    /**
     * 得到子节点列表
     */
    private static List<Ztree> getChildList(List<Ztree> list, Ztree t) {
        List<Ztree> tlist = new ArrayList<>();
        Iterator<Ztree> it = list.iterator();
        while (it.hasNext()) {
            Ztree n = it.next();
            if (t.getId().equals(n.getPId())) {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private static boolean hasChild(List<Ztree> list, Ztree t) {
        for (Ztree ztree : list) {
            if (t.getId().equals(ztree.getPId())) {
                return true;
            }
        }
        return false;
    }


}
