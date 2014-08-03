package org.abner.manager.repository;

import java.util.List;

public interface DataProvider<M> {

    List<M> find();

}
