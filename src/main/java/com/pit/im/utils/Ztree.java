package com.pit.im.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Ztree树结构实体类
 *
 * @author deng
 */
@Data
public class Ztree implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 节点ID
     */
    private String id;

    /**
     * 节点父ID
     */
    private String pId;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否勾选
     */
    private boolean checked = false;

    /**
     * 是否展开
     */
    private boolean open = false;

    /**
     * 是否能勾选
     */
    private boolean nocheck = false;

    /**
     * 是否启用
     */
    private boolean isEnable;

    /**
     * 排序字段
     */
    private Integer orderId;

    /**
     * 子级树结构
     */
    private List<Ztree> Children;


}
