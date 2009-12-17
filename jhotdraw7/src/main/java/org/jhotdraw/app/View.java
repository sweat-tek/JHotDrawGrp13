/*
 * @(#)View.java
 *
 * Copyright (c) 1996-2009 by the original authors of JHotDraw
 * and all its contributors.
 * All rights reserved.
 *
 * The copyright of this software is owned by the authors and  
 * contributors of the JHotDraw project ("the copyright holders").  
 * You may not use, copy or modify this software, except in  
 * accordance with the license agreement you entered into with  
 * the copyright holders. For details see accompanying license terms. 
 */

package org.jhotdraw.app;

import org.jhotdraw.gui.URIChooser;
import java.io.*;
import java.beans.*;
import java.net.URI;
import javax.swing.*;
import org.jhotdraw.beans.Disposable;

/**
 * Provides a <em>view</em> on a document or a set of related documents within
 * an {@link Application}.
 * <p>
 * After a view has been initialized using {@code init()},
 * Immediately after {@code init()}, method {@code clear()} or method
 * {@code read()} must be called to fully initialize the View.
 *
 * <hr>
 * <b>Design Patterns</b>
 *
 * <p><em>Framework</em><br>
 * The interfaces and classes listed below together with the {@code Action}
 * classes in the org.jhotddraw.app.action package define the contracts of a
 * framework for document oriented applications:<br>
 * Contract: {@link Application}, {@link ApplicationModel}, {@link View}.
 * <hr>
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public interface View {
    /**
     * The name of the uri property.
     */
    public final static String URI_PROPERTY = "uri";
    /**
     * The name of the application property.
     */
    public final static String APPLICATION_PROPERTY = "application";
    /**
     * The name of the title property.
     */
    public final static String TITLE_PROPERTY = "title";
    /**
     * The name of the enabled property.
     */
    public final static String ENABLED_PROPERTY = "enabled";
    /**
     * The name of the hasUnsavedChanges property.
     */
    public final static String HAS_UNSAVED_CHANGES_PROPERTY = "hasUnsavedChanges";
    /**
     * The name of the multipleOpenId property.
     */
    public final static String MULTIPLE_OPEN_ID_PROPERTY = "multipleOpenId";
    /**
     * The name of the showing property.
     */
    public final static String SHOWING_PROPERTY = "showing";
    /**
     * Gets the application to which this view belongs.
     */
    public Application getApplication();
    
    /**
     * Sets the application of the view.
     * By convention, this is only invoked by Application.add() and
     * Application.remove().
     * This is a bound property.
     */
    public void setApplication(Application newValue);
    
    /**
     * Returns the visual component of the view.
     */
    public JComponent getComponent();
    
    /**
     * Returns the enabled state of the view.
     */
    public boolean isEnabled();
    
    /**
     * Sets the enabled state of the view.
     *
     * The enabled state is used to prevent parallel invocation of actions
     * on the view. If an action consists of a sequential part and a
     * concurrent part, it must disable the view only for the sequential
     * part.
     *
     * Actions that act on the view must check in their actionPerformed
     * method whether the view is enabled.
     * If the view is disabled, they must do nothing.
     * If the view is enabled, they must disable the view,
     * perform the action and then enable the view again.
     *
     * This is a bound property.
     */
    public void setEnabled(boolean newValue);
    
    /**
     * Clears the view, for example by emptying the contents of
     * the view, or by reading a template contents from a file.
     * By convention this method is never invoked on the AWT Event Dispatcher Thread.
     */
    public void clear();
    
    
    /**
     * Returns true, if the view has unsaved changes.
     * This is a bound property.
     */
    public boolean hasUnsavedChanges();
    /**
     * Marks all changes as saved.
     * This changes the state of hasUnsavedChanges to false.
     */
    public void markChangesAsSaved();
    
    /**
     * Executes the specified runnable on the worker thread of the view.
     * Execution is perfomred sequentially in the same sequence as the
     * runnables have been passed to this method.
     */
    public void execute(Runnable worker);
    
    /**
     * Initializes the view.
     * This is invoked right before the application shows the view.
     * A view must not consume many resources before method init() is called.
     * This is crucial for the responsivenes of an application.
     * <p>
     * After a view has been initialized using init(),
     * either method clear() must be called
     * or method read, in order to fully initialize a  View.
     */
    public void init();
    
    /**
     * Starts the view.
     * Invoked after a view has been made visible to the user.
     * Multiple view can be visible at the same time.
     */
    public void start();
    /**
     * Activates the view.
     * This occurs, when the user activated the parent window of the view.
     * Only one view can be active at any given time.
     * This method is only invoked on a started view.
     */
    public void activate();
    /**
     * Deactivates the view.
     * This occurs, when the user closes the view, or activated another view.
     * This method is only invoked on a started view.
     */
     public void deactivate();    
    /**
     * Stops the view.
     * Invoked after a view window has been minimized or made invisible.
     */
     public void stop();    
    /**
     * Gets rid of all the resources of the view.
     * No other methods should be invoked on the view afterwards.
     * A view must not consume many resources after method dispose() has been called.
     * This is crucial for the responsivenes of an application.
     */
    public void dispose();
    
    /**
     * Returns the action with the specified id.
     */
    public Action getAction(String id);
    
    /**
     * Puts an action with the specified id.
     */
    public void putAction(String id, Action action);
    
    /**
     * Adds a property change listener.
     */
    public void addPropertyChangeListener(PropertyChangeListener l);
    
    /**
     * Removes a property change listener.
     */
    public void removePropertyChangeListener(PropertyChangeListener l);
    
    /**
     * Sets the multiple open id.
     * The id is used to help distinguish multiply opened views.
     * The id should be displayed in the title of the view.
     */
    public void setMultipleOpenId(int newValue);
    
    /**
     * Returns the multiple open id.
     * If a view is open only once this should be 1.
     */
    public int getMultipleOpenId();
    
    /**
     * This is used by Application to keep track if a view is showing.
     */
    public boolean isShowing();
    /**
     * This is used by Application to keep track if a view is showing.
     */
    public void setShowing(boolean newValue);
    
    /**
     * Sets the title of the view. 
     * <p>
     * The title is generated by the application, based on the current
     * URI of the view. The application ensures that the title uniquely
     * identifies each open view.
     * <p> 
     * The application displays the title in the title bar of the view 
     * window and in all windows which are associated to the view.
     * <p>
     * This is a bound property.
     */
    public void setTitle(String newValue);
    
    /**
     * Gets the title of the view. 
     */
    public String getTitle();
    
    /**
     * Adds a disposable object, which will be disposed when the view
     * is disposed.
     *
     * @param disposable
     */
    public void addDisposable(Disposable disposable);

    /**
     * Returns the uri which holds the document of the view.
     */
    public URI getURI();

    /**
     * Sets the uri of the view.
     * This is a bound property.
     */
    public void setURI(URI newValue);

    /**
     * Returns true, if this view can be saved to the specified URI.
     * A reason why the view can't be saved to a URI, is that the
     * view is unable to write to a file-URI with the given filename
     * extension without losing data.
     * <p>
     * The SaveAction uses this method to decide, whether to display
     * a save dialog before saving the URI.
     *
     * @param uri An URI. If this parameter is null, a NullPointerException
     * is thrown.
     */
    public boolean canSaveTo(URI uri);

    /**
     * Writes the view to the specified URI.
     * By convention this method is never invoked on the AWT Event Dispatcher Thread.
     */
    public void write(URI uri) throws IOException;

    /**
     * Reads the view from the specified URI.
     * By convention this method is never invoked on the AWT Event Dispatcher Thread.
     */
    public void read(URI uri) throws IOException;

    /**
     * Gets the open chooser for the view.
     */
    public URIChooser getOpenChooser();
    /**
     * Gets the save chooser for the view.
     */
    public URIChooser getSaveChooser();

}
