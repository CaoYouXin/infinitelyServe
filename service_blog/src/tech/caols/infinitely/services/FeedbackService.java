package tech.caols.infinitely.services;

import org.apache.http.HttpResponse;

public interface FeedbackService {

    Integer like(Long id, HttpResponse response);

}
