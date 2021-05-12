package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.AccountFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TbPermission)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:16
 */
public interface TbPermissionDao {

    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbPermission 实例对象
     * @return long型的总数
     */
    long countAll(TbPermission tbPermission);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbPermission 实例对象
     * @return 对象列表
     */
    List<TbPermission> queryAll(TbPermission tbPermission);

    /**
     * 通过ID查询单条数据
     *
     * @param permissionId 主键
     * @return 实例对象
     */
    TbPermission queryById(Long permissionId);

    /**
     * 查询指定行数据
     *
     * @param tbPermission 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbPermission> queryAllByLimit(
            @Param("tbPermission") TbPermission tbPermission,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 新增数据
     *
     * @param tbPermission 实例对象
     * @return 影响行数
     */
    int insert(TbPermission tbPermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbPermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbPermission> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbPermission> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TbPermission> entities);

    /**
     * 修改数据
     *
     * @param tbPermission 实例对象
     * @return 影响行数
     */
    int update(TbPermission tbPermission);

    /**
     * 通过主键删除数据
     *
     * @param permissionId 主键
     * @return 影响行数
     */
    int deleteById(Long permissionId);

}

