package com.baayso.bms.privilege;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baayso.bms.role.Role;

/**
 * 实体：权限表
 * 
 * @author ChenFangjie
 * 
 */
public class Privilege implements Serializable {

    private static final long serialVersionUID = 1305149266320579502L;

    private String id;
    private String name;
    private String url; // 一个权限对应一个功能，一个功能对应一个URL（对功能的控制就是对URL的访问控制）
    private String parentId;

    private List<Role> roles = new ArrayList<Role>();
    private Privilege parent; // 所属权限
    private List<Privilege> children = new ArrayList<Privilege>(); // 子权限

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Privilege getParent() {
        return parent;
    }

    public void setParent(Privilege parent) {
        this.parent = parent;
    }

    public List<Privilege> getChildren() {
        return children;
    }

    public void setChildren(List<Privilege> children) {
        this.children = children;
    }

}
