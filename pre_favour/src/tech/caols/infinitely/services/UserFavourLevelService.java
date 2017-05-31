package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;
import tech.caols.infinitely.server.PreReq;
import tech.caols.infinitely.server.PreRes;

public interface UserFavourLevelService {

    PreRes userFavourLevel(PreReq preReq, HttpResponse response);

}
