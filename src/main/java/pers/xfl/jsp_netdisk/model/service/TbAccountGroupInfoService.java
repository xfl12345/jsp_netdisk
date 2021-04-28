package pers.xfl.jsp_netdisk.model.service;

import pers.xfl.jsp_netdisk.model.pojo.database.TbAccountGroupInfo;

import java.util.List;

/**
 * (TbAccountGroupInfo)表服务接口
 *
 * @author makejava
 * @since 2021-04-19 16:00:53
 */
public interface TbAccountGroupInfoService {

//    /**
//     * 通过ID查询单条数据
//     *
//     * @param 主键
//     * @param id
//     * @return 实例对象
//     */
//    TbAccountGroupInfo queryById(Long id);

    /**
     * 查询多条数据
     *
     * @param offset 查询起始位置
     * @param limit  查询条数
     * @return 对象列表
     */
    List<TbAccountGroupInfo> queryAllByLimit(int offset, int limit);

    /**
     * 新增数据
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 实例对象
     */
    TbAccountGroupInfo insert(TbAccountGroupInfo tbAccountGroupInfo);

    /**
     * 修改数据
     *
     * @param tbAccountGroupInfo 实例对象
     * @return 实例对象
     */
    TbAccountGroupInfo update(TbAccountGroupInfo tbAccountGroupInfo);

//    /**
//     * 通过主键删除数据
//     *
//     * @param 主键
//     * @return 是否成功
//     */
//    boolean deleteById();

}
