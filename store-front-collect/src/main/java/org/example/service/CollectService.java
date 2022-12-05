package org.example.service;

import org.example.param.CollectParam;

public interface CollectService {
    Object save(CollectParam collectParam);

    Object list(CollectParam collectParam);

    Object remove(CollectParam collectParam);
}
