package pers.xfl.jsp_netdisk.model.service;

import pers.xfl.jsp_netdisk.model.pojo.database.TbFile;

import java.util.List;

/**
 * (TbFile)表服务接口
 *
 * @author makejava
 * @since 2021-04-19 16:00:53
 */
public interface TbFileService {

    /**
     * 通过ID查询单条数据
     *
     * @param fileId 主键
     * @return 实例对象
     */
    TbFile queryById(Long fileId);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbFile> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tbFile 实例对象
     * @return 实例对象
     */
    TbFile insert(TbFile tbFile);

    /**
     * 修改数据
     *
     * @param tbFile 实例对象
     * @return 实例对象
     */
    TbFile update(TbFile tbFile);

    /**
     * 通过主键删除数据
     *
     * @param fileId 主键
     * @return 是否成功
     */
    boolean deleteById(Long fileId);

}
