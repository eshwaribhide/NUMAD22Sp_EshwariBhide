package edu.neu.madcourse.numad22sp_eshwaribhide;


public class ListItem implements ListItemClickListener{
    private final String linkName;
    private final String linkValue;

    public ListItem(String linkName, String linkValue) {
        this.linkName = linkName;
        this.linkValue = linkValue;
    }

    public void listItemOnClick(int position) {
    }

    public String getlinkName() {
        return linkName;
    }

    public String getlinkValue() {
        return linkValue;
    }



}

