package com.baayso.bms.role;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.baayso.bms.privilege.Privilege;
import com.baayso.bms.user.User;

/**
 * 实体：角色表
 * 
 * @author ChenFangjie
 * 
 */
public class Role implements Serializable {

    private static final long serialVersionUID = -3648584535334830042L;

    private String id;
    private String name;
    private String descriptions;

    private List<User> users = new ArrayList<User>();
    private List<Privilege> privileges = new ArrayList<Privilege>(); // 所拥有的权限

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

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<Privilege> privileges) {
        this.privileges = privileges;
    }

}
