package org.techtown.samplerelativelayout;

import android.text.Editable;

public class Todo {
    //아이템의 해야할 내용과
    String todo;
    //할 일의 해결 유무
    boolean checkbox;

    public Todo(String a, boolean b) {
        this.todo=a;
        this.checkbox=b;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }
}
