package tech.caols.infinitely.base;

import org.apache.http.HttpResponse;

import java.util.List;

public interface BaseService<D, V> {

    List<V> list();

    List<V> save(V v, HttpResponse response);

    List<V> delete(List<Long> ids, HttpResponse response);

    List<V> softDelete(List<Long> ids, HttpResponse response);

}
