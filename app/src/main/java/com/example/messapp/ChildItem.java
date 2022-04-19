package com.example.messapp;

public class ChildItem {


    private String ChildItemTitle;

    public ChildItem(String childItemTitle)
    {
        this.ChildItemTitle = childItemTitle;
    }

    public String getChildItemTitle()
    {
        return ChildItemTitle;
    }

    public void setChildItemTitle(
            String childItemTitle)
    {
        ChildItemTitle = childItemTitle;
    }
}
