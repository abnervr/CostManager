package org.abner.manager.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractModel implements Model, Serializable {

    private Long id;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && getClass().equals(o.getClass())) {
            return id.equals(((AbstractModel) o).id);
        }
        return false;
    }

}
