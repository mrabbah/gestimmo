package com.choranet.omnidior.gesticom.util;

import org.zkoss.zul.DefaultTreeNode;

public class ProjectTreeNode extends DefaultTreeNode {

    // private static final long serialVersionUID = -8085873079938209759L;
    // Node Control the default open
    private boolean open = false;

    public ProjectTreeNode(Object data, ProjectTreeNodeCollection children, boolean open) {
        super(data, children);
        this.setOpen(open);
    }

    public ProjectTreeNode(Object data, ProjectTreeNodeCollection children) {
        super(data, children);
    }

    public ProjectTreeNode(Object data) {
        super(data);
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
    
}


/*
import java.util.ArrayList;

public abstract class ProjectTreeNode {

    ArrayList<ProjectTreeNode> children = null;

    public abstract void readChildren();

    public ArrayList<ProjectTreeNode> getChildren() {
        return children;
    }

    public ProjectTreeNode getChild(int arg1) {
        ProjectTreeNode child = null;

        if (children == null) {
            readChildren();
        }

        if (children != null && (arg1 > -1 && arg1 < children.size())) {
            child = children.get(arg1);
        }

        return child;
    }

    public int getChildCount() {
        if (children == null) {
            readChildren();
        }

        if (children != null) {
            return children.size();
        }

        return 0;
    }

    public boolean isLeaf() {
        return (getChildCount() == 0);
    }
}

 */