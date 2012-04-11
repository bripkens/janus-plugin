package de.codecentric.janus.plugin.bootstrap;

import de.codecentric.janus.plugin.ci.CIConfiguration;
import de.codecentric.janus.plugin.vcs.VCSConfiguration;
import de.codecentric.janus.scaffold.CatalogEntry;

import java.util.Map;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
class ParsedFormData {

    enum Status {
        OK,ERROR
    }
    
    private Status status;
    
    private String name, pckg, description;
    private CatalogEntry scaffold;
    private VCSConfiguration vcsConfiguration;
    private CIConfiguration ciConfiguration;
    private Map<String, String> context;

    ParsedFormData() {
    }

    ParsedFormData(Status status) {
        this();
        this.status = status;
    }

    Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getPckg() {
        return pckg;
    }

    void setPckg(String pckg) {
        this.pckg = pckg;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    VCSConfiguration getVcsConfiguration() {
        return vcsConfiguration;
    }

    void setVcsConfiguration(VCSConfiguration vcsConfiguration) {
        this.vcsConfiguration = vcsConfiguration;
    }

    CatalogEntry getScaffold() {
        return scaffold;
    }

    void setScaffold(CatalogEntry scaffold) {
        this.scaffold = scaffold;
    }

    Map<String, String> getContext() {
        return context;
    }

    void setContext(Map<String, String> context) {
        this.context = context;
    }

    CIConfiguration getCiConfiguration() {
        return ciConfiguration;
    }

    void setCiConfiguration(CIConfiguration ciConfiguration) {
        this.ciConfiguration = ciConfiguration;
    }
}
