package com.apkextractor.android.installedapps.adapter;

class SlimItem {
    private Object data;
    private int sectionFirstPosition;

    SlimItem(Object data, int sectionFirstPosition) {
        this.data = data;
        this.sectionFirstPosition = sectionFirstPosition;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getSectionFirstPosition() {
        return sectionFirstPosition;
    }

    public void setSectionFirstPosition(int sectionFirstPosition) {
        this.sectionFirstPosition = sectionFirstPosition;
    }
}