package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.viewmodels.LevelView;

import java.util.List;

public interface LevelService {

    List<LevelView> list();

    List<LevelView> save(LevelView levelView, HttpResponse response);

    List<LevelView> delete(List<Long> ids, HttpResponse response);

}
