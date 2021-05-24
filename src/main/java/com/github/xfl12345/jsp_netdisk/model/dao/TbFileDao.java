package com.github.xfl12345.jsp_netdisk.model.dao;

import com.github.xfl12345.jsp_netdisk.model.pojo.database.TbFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (TbFile)表数据库访问层
 *
 * @author makejava
 * @since 2021-04-19 16:13:16
 */
public interface TbFileDao {


    /**
     * 通过MD5和SHA256的哈希值去查询唯一确定的文件
     */
    TbFile queryByMD5andSHA256(TbFile tbFile);

    /**
     * 通过实体作为筛选条件统计
     *
     * @param tbFile 实例对象
     * @return long型的总数
     */
    long countAll(TbFile tbFile);

    /**
     * 通过实体作为筛选条件查询
     *
     * @param tbFile 实例对象
     * @return 对象列表
     */
    List<TbFile> queryAll(TbFile tbFile);

    /**
     * 通过ID查询单条数据
     *
     * @param fileId 主键
     * @return 实例对象
     */
    TbFile queryById(Long fileId);

    /**
     * 查询指定行数据
     *
     * @param tbFile 实例对象
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbFile> queryAllByLimit(
            @Param("tbFile") TbFile tbFile,
            @Param("offset") int offset,
            @Param("limit") int limit);

    /**
     * 新增数据
     *
     * @param tbFile 实例对象
     * @return 影响行数
     */
    int insert(TbFile tbFile);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbFile> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<TbFile> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<TbFile> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<TbFile> entities);

    /**
     * 修改数据
     *
     * @param tbFile 实例对象
     * @return 影响行数
     */
    int update(TbFile tbFile);

    /**
     * 通过主键删除数据
     *
     * @param fileId 主键
     * @return 影响行数
     */
    int deleteById(Long fileId);

}

