package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.viewmodels.LeveledResourceDetailView;
import tech.caols.infinitely.viewmodels.LeveledResourceView;

import java.util.List;

public interface LeveledResourceService {

    List<LeveledResourceDetailView> findAll();

    List<LeveledResourceDetailView> save(LeveledResourceView leveledResourceView, HttpResponse response);

    List<LeveledResourceDetailView> delete(List<Long> ids, HttpResponse response);

}
