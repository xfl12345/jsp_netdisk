package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.DirFile;
import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbAccountGroupInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TbAccountGroupInfo)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:16
 */
public interface TbAccountGroupInfoDao {

    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbAccountGroupInfo 实例对象
     * @return long型的总数
     */
    long countAll(TbAccountGroupInfo tbAccountGroupInfo);

    /**
     * 查询指定行数据
     *
     * @param tbAccountGroupInfo 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbAccountGroupInfo> queryAllByLimit(
            @Param("tbAccountGroupInfo")TbAccountGroupInfo tbAccountGroupInfo,
            @Param("offset") int offset,
            @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 对象列表
     */
    List<TbAccountGroupInfo> queryAll(TbAccountGroupInfo tbAccountGroupInfo);

    /**
     * 新增数据
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 影响行数
     */
    int insert(TbAccountGroupInfo tbAccountGroupInfo);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbAccountGroupInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbAccountGroupInfo> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbAccountGroupInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TbAccountGroupInfo> entities);

    /**
     * 通过实体作为筛选条件删除
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 影响行数
     */
    int deleteAll(TbAccountGroupInfo tbAccountGroupInfo);

}

