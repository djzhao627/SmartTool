package com.djzhao.smarttool.db.chooseproblem;

import org.litepal.crud.DataSupport;

/**
 * Created by djzhao on 18/05/07.
 */
public class ChooseProblemItem extends DataSupport {
    private int id;
    private String title;
    private int parentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
