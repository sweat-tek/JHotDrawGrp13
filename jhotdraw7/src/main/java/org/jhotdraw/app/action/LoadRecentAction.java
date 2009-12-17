/*
 * @(#)LoadRecentAction.java
 *
 * Copyright (c) 1996-2006 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */
package org.jhotdraw.app.action;

import org.jhotdraw.gui.*;
import org.jhotdraw.gui.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.URI;
import org.jhotdraw.app.Application;
import org.jhotdraw.app.View;
import org.jhotdraw.net.URIUtil;
import org.jhotdraw.util.ResourceBundleUtil;

/**
 * LoadRecentAction.
 *
 * @author Werner Randelshofer.
 * @version $Id$
 */
public class LoadRecentAction extends AbstractSaveBeforeAction {

    public final static String ID = "file.loadRecent";
    private URI uri;

    /** Creates a new instance. */
    public LoadRecentAction(Application app, URI uri) {
        super(app);
        this.uri = uri;
        putValue(Action.NAME, URIUtil.getName(uri));
    }

    public void doIt(final View view) {
        final Application app = getApplication();
        app.setEnabled(true);

        // If there is another view with we set the multiple open
        // id of our view to max(multiple open id) + 1.
        int multipleOpenId = 1;
        for (View aView : app.views()) {
            if (aView != view &&
                    aView.getURI() != null &&
                    aView.getURI().equals(uri)) {
                multipleOpenId = Math.max(multipleOpenId, aView.getMultipleOpenId() + 1);
            }
        }
        view.setMultipleOpenId(multipleOpenId);

        // Open the file
        view.execute(new Worker() {

            protected Object construct() throws IOException {
                boolean exists = true;
                try {
                    File f = new File(uri);
                    exists = f.exists();
                } catch (IllegalArgumentException e) {
                    // The URI does not denote a file, thus we can not check whether the file exists.
                }
                if (exists) {
                    view.read(uri);
                    return null;
                } else {
                    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");
                    throw new IOException(labels.getFormatted("file.load.fileDoesNotExist.message", URIUtil.getName(uri)));
                }
            }

            @Override
            protected void done(Object value) {
                final Application app = getApplication();
                view.setURI(uri);
                view.setEnabled(true);
                Frame w = (Frame) SwingUtilities.getWindowAncestor(view.getComponent());
                if (w != null) {
                    w.setExtendedState(w.getExtendedState() & ~Frame.ICONIFIED);
                    w.toFront();
                }
                view.getComponent().requestFocus();
                if (app != null) {
                    app.setEnabled(true);
                }
            }

            @Override
            protected void failed(Throwable error) {
                final Application app = getApplication();
                error.printStackTrace();
                    ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.app.Labels");

                JSheet.showMessageSheet(view.getComponent(),
                        "<html>" + UIManager.getString("OptionPane.css") +
                        "<b>" + labels.getFormatted("file.load.couldntLoad.message", URIUtil.getName(uri)) + "</b><p>" +
                        error,
                        JOptionPane.ERROR_MESSAGE, new SheetListener() {

                    public void optionSelected(SheetEvent evt) {
                        // app.dispose(view);
                    }
                });
            }
        });
    }
}
