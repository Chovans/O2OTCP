package com.dotnar.bean.git;

/**
 * @author chovans on 15/8/16.
 */
public class GitInfoResponse {
    private String templateName = "";
    private String _package = "";
    private String path = "";
    private String[]  relationPaths = {};
    private String[] relation = {};

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getRelation() {
        return relation;
    }

    public void setRelation(String[] relation) {
        this.relation = relation;
    }

    public String[] getRelationPaths() {
        return relationPaths;
    }

    public void setRelationPaths(String[] relationPaths) {
        this.relationPaths = relationPaths;
    }
}
