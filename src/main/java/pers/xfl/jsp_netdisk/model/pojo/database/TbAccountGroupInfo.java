package pers.xfl.jsp_netdisk.model.pojo.database;

import java.io.Serializable;

/**
 * (TbAccountGroupInfo)实体类
 *
 * @author makejava
 * @since 2021-04-19 16:00:53
 */
public class TbAccountGroupInfo implements Serializable {
    private static final long serialVersionUID = 514695625856770776L;
    /**
     * 管理员的账号ID
     */
    private Long groupAdminId;
    /**
     * 被管理员管理的账号ID
     */
    private Long groupMemberId;


    public Long getGroupAdminId() {
        return groupAdminId;
    }

    public void setGroupAdminId(Long groupAdminId) {
        this.groupAdminId = groupAdminId;
    }

    public Long getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(Long groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

}
