package com.example.debatetrackerog;

public class DebateStyle {
    private String mTitle;
    private String mDescription;
    private int mImageResource;
    //0 = Public Forum
    //1 = Lincoln Douglas
    //2 = Policy
    //3 = Big Questions
    //4 = Extemporaneous
    //5 = World Schools
    private int mTypeOfDebate;
    public DebateStyle(String title, String description, int imageresource, int type) {
        mTitle = title;
        mDescription = description;
        mImageResource = imageresource;
        mTypeOfDebate = type;
    }
    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String title) {
        mTitle = title;
    }
    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public int getImageResource() {
        return mImageResource;
    }
    public void setImageResource(int imageResource) {
        mImageResource = imageResource;
    }
    public int getTypeOfDebate() {
        return mTypeOfDebate;
    }
    public void setTypeOfDebate(int typeOfDebate) {
        mTypeOfDebate = typeOfDebate;
    }
}
