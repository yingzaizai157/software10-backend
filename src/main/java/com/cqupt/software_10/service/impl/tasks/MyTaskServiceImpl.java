package com.cqupt.software_10.service.impl.tasks;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.UpdateChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtQueryChainWrapper;
import com.baomidou.mybatisplus.extension.kotlin.KtUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cqupt.software_10.dao.TaskMapper;
import com.cqupt.software_10.entity.Task;
import com.cqupt.software_10.entity.tasks.MyTask;
import com.cqupt.software_10.mapper.tasks.MyTaskMapper;
import com.cqupt.software_10.service.tasks.MyTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


@Service
public class MyTaskServiceImpl extends ServiceImpl<MyTaskMapper, MyTask> implements MyTaskService {
    @Resource
    MyTaskMapper myTaskMapper;

    @Override
    public boolean save(MyTask entity) {
        return super.save(entity);
    }

    @Override
    public boolean saveBatch(Collection<MyTask> entityList) {
        return super.saveBatch(entityList);
    }

    @Override
    public boolean saveOrUpdateBatch(Collection<MyTask> entityList) {
        return super.saveOrUpdateBatch(entityList);
    }

    @Override
    public boolean removeById(Serializable id) {
        return super.removeById(id);
    }

    @Override
    public boolean removeById(MyTask entity) {
        return super.removeById(entity);
    }

    @Override
    public boolean removeByMap(Map<String, Object> columnMap) {
        return super.removeByMap(columnMap);
    }

    @Override
    public boolean remove(Wrapper<MyTask> queryWrapper) {
        return super.remove(queryWrapper);
    }

    @Override
    public boolean removeByIds(Collection<? extends Serializable> idList) {
        return super.removeByIds(idList);
    }

    @Override
    public boolean updateById(MyTask entity) {
        return super.updateById(entity);
    }

    @Override
    public boolean update(Wrapper<MyTask> updateWrapper) {
        return super.update(updateWrapper);
    }

    @Override
    public boolean update(MyTask entity, Wrapper<MyTask> updateWrapper) {
        return super.update(entity, updateWrapper);
    }

    @Override
    public boolean updateBatchById(Collection<MyTask> entityList) {
        return super.updateBatchById(entityList);
    }

    @Override
    public MyTask getById(Serializable id) {
        return super.getById(id);
    }

    @Override
    public List<MyTask> listByIds(Collection<? extends Serializable> idList) {
        return super.listByIds(idList);
    }

    @Override
    public List<MyTask> listByMap(Map<String, Object> columnMap) {
        return super.listByMap(columnMap);
    }

    @Override
    public MyTask getOne(Wrapper<MyTask> queryWrapper) {
        return super.getOne(queryWrapper);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public long count(Wrapper<MyTask> queryWrapper) {
        return super.count(queryWrapper);
    }

    @Override
    public List<MyTask> list(Wrapper<MyTask> queryWrapper) {
        return super.list(queryWrapper);
    }

    @Override
    public List<MyTask> list() {
        return super.list();
    }

    @Override
    public <E extends IPage<MyTask>> E page(E page, Wrapper<MyTask> queryWrapper) {
        return super.page(page, queryWrapper);
    }

    @Override
    public <E extends IPage<MyTask>> E page(E page) {
        return super.page(page);
    }

    @Override
    public List<Map<String, Object>> listMaps(Wrapper<MyTask> queryWrapper) {
        return super.listMaps(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> listMaps() {
        return super.listMaps();
    }

    @Override
    public List<Object> listObjs() {
        return super.listObjs();
    }

    @Override
    public <V> List<V> listObjs(Function<? super Object, V> mapper) {
        return super.listObjs(mapper);
    }

    @Override
    public List<Object> listObjs(Wrapper<MyTask> queryWrapper) {
        return super.listObjs(queryWrapper);
    }

    @Override
    public <V> List<V> listObjs(Wrapper<MyTask> queryWrapper, Function<? super Object, V> mapper) {
        return super.listObjs(queryWrapper, mapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page, Wrapper<MyTask> queryWrapper) {
        return super.pageMaps(page, queryWrapper);
    }

    @Override
    public <E extends IPage<Map<String, Object>>> E pageMaps(E page) {
        return super.pageMaps(page);
    }

    @Override
    public QueryChainWrapper<MyTask> query() {
        return super.query();
    }

    @Override
    public LambdaQueryChainWrapper<MyTask> lambdaQuery() {
        return super.lambdaQuery();
    }

    @Override
    public KtQueryChainWrapper<MyTask> ktQuery() {
        return super.ktQuery();
    }

    @Override
    public KtUpdateChainWrapper<MyTask> ktUpdate() {
        return super.ktUpdate();
    }

    @Override
    public UpdateChainWrapper<MyTask> update() {
        return super.update();
    }

    @Override
    public LambdaUpdateChainWrapper<MyTask> lambdaUpdate() {
        return super.lambdaUpdate();
    }

    @Override
    public boolean saveOrUpdate(MyTask entity, Wrapper<MyTask> updateWrapper) {
        return super.saveOrUpdate(entity, updateWrapper);
    }

    @Override
    public MyTask getlistbyId(Integer id) {
        return myTaskMapper.getlistbyId(id);
    }

    @Override
    public String getFeatureByTasknameAndModelname(String taskname, String modelname) {
        return myTaskMapper.getFeatureByTasknameAndModelname(taskname, modelname);
    }

}