package tech.caols.infinitely.base;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.db.Repository;
import tech.caols.infinitely.db.helper.DBHelper;
import tech.caols.infinitely.rest.BeanUtils;
import tech.caols.infinitely.server.HttpUtils;
import tech.caols.infinitely.server.JsonRes;

import java.util.List;
import java.util.stream.Collectors;

public class BaseServiceImpl<D, V> implements BaseService<D, V> {

    private final Class<D> dClass;
    private final Class<V> vClass;

    private final Repository<D, Long> repository;

    public BaseServiceImpl(Class<D> dClass, Class<V> vClass) {
        this.dClass = dClass;
        this.vClass = vClass;
        this.repository = new Repository<>(dClass);
    }

    @Override
    public List<V> list() {
        return this.repository.findAll().stream().map(d -> {
            V v = DBHelper.buildOne(vClass);
            BeanUtils.copyBean(d, v);
            return v;
        }).collect(Collectors.toList());
    }

    @Override
    public List<V> save(V v, HttpResponse response) {
        D d = DBHelper.buildOne(dClass);
        BeanUtils.copyBean(v, d);

        if (!this.repository.save(d)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(String.format("保存%s失败! ", dClass.getName())));
            return null;
        }

        return this.list();
    }

    @Override
    public List<V> delete(List<Long> ids, HttpResponse response) {
        if (!this.repository.removeAll(ids)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(String.format("删除%s失败! ", dClass.getName())));
            return null;
        }

        return this.list();
    }

    @Override
    public List<V> softDelete(List<Long> ids, HttpResponse response) {
        if (!this.repository.softRemoveAll(ids)) {
            HttpUtils.response(response, JsonRes.getFailJsonRes(String.format("删除%s失败! ", dClass.getName())));
            return null;
        }

        return this.list();
    }


}
