package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbDirectory;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TbDirectory)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:16
 */
public interface TbDirectoryDao {

    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbDirectory 实例对象
     * @return long型的总数
     */
    long countAll(TbDirectory tbDirectory);
    /**
     * 通过ID查询单条数据
     *
     * @param directoryId 主键
     * @return 实例对象
     */
    TbDirectory queryById(Long directoryId);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbDirectory> queryAllByLimit(
            @Param("tbDirectory") TbDirectory tbDirectory,
            @Param("offset") int offset,
            @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbDirectory 实例对象
     * @return 对象列表
     */
    List<TbDirectory> queryAll(TbDirectory tbDirectory);

    /**
     * 新增数据
     *
     * @param tbDirectory 实例对象
     * @return 影响行数
     */
    int insert(TbDirectory tbDirectory);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbDirectory> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbDirectory> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbDirectory> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TbDirectory> entities);

    /**
     * 修改数据
     *
     * @param tbDirectory 实例对象
     * @return 影响行数
     */
    int update(TbDirectory tbDirectory);

    /**
     * 通过主键删除数据
     *
     * @param directoryId 主键
     * @return 影响行数
     */
    int deleteById(Long directoryId);

}

