package de.codecentric.janus.plugin;
l=namespace(lib.LayoutTagLib)

l.header()
l.side_panel {
    l.tasks {
        l.task(icon:"images/24x24/up.png", href:rootURL+'/', title:_("Back to Dashboard"))
        l.task(icon:"images/24x24/setting.png", href:"${rootURL}/manage", title:_("Manage Jenkins"), permission:app.ADMINISTER, it:app)
        if (!app.updateCenter.jobs.isEmpty()) {
            l.task(icon:"images/24x24/plugin.png", href:"${rootURL}/updateCenter/", title:_("Update Center"))
        }
    }
}