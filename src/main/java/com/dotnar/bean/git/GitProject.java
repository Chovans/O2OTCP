package com.dotnar.bean.git;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * @author chovans on 15/8/15.
 */
@Table
@Entity
public class GitProject {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(length = 40)
    private String id;
    private String name;
    private String url;
    private Date createOn;
    private Date updateOn;
    private String state;
    @Column(columnDefinition = "TEXT")
    private String _package;
    private String parentTemplateName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    public Date getUpdateOn() {
        return updateOn;
    }

    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getParentTemplateName() {
        return parentTemplateName;
    }

    public void setParentTemplateName(String parentTemplateName) {
        this.parentTemplateName = parentTemplateName;
    }

    public String get_package() {
        return _package;
    }

    public void set_package(String _package) {
        this._package = _package;
    }
}
