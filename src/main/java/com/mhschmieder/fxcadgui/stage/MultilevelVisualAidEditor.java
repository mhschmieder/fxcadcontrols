/**
 * MIT License
 *
 * Copyright (c) 2020, 2023 Mark Schmieder
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * This file is part of the FxCadGui Library
 *
 * You should have received a copy of the MIT License along with the FxCadGui
 * Library. If not, see <https://opensource.org/licenses/MIT>.
 *
 * Project: https://github.com/mhschmieder/fxcadgui
 */
package com.mhschmieder.fxcadgui.stage;

import com.mhschmieder.commonstoolkit.branding.ProductBranding;
import com.mhschmieder.commonstoolkit.util.ClientProperties;
import com.mhschmieder.fxcadgraphics.GraphicalObjectCollection;
import com.mhschmieder.fxcadgraphics.MultilevelVisualAid;
import com.mhschmieder.fxcadgui.layout.MultilevelVisualAidPane;
import com.mhschmieder.fxguitoolkit.ScrollingSensitivity;
import com.mhschmieder.fxguitoolkit.stage.ObjectPropertiesEditor;
import com.mhschmieder.fxlayergraphics.model.LayerProperties;
import com.mhschmieder.physicstoolkit.AngleUnit;
import com.mhschmieder.physicstoolkit.DistanceUnit;

import javafx.collections.ObservableList;
import javafx.scene.Node;

public final class MultilevelVisualAidEditor extends ObjectPropertiesEditor {

    // Declare a constant for the object type, to avoid cut/paste errors.
    public static final String                                 OBJECT_TYPE =
                                                                           "Multilevel Visual Aid"; //$NON-NLS-1$

    // Declare the main content pane.
    protected MultilevelVisualAidPane                          _multilevelVisualAidPane;

    // Maintain a reference to the current Multilevel Visual Aid object.
    protected MultilevelVisualAid                              _multilevelVisualAidReference;

    // Maintain a reference to the Multilevel Visual Aid collection.
    protected GraphicalObjectCollection< MultilevelVisualAid > _multilevelVisualAidCollection;
    
    // Allow for customization of Projector Type (name identifier, not behavior).
    protected String _projectorType;

    // Allow for customization of Projection Zones Type (name identifier, not behavior).
    protected String _projectionZonesType;
    
    // Projection Zones usage context, for constructing tooltips.
    protected String _projectionZonesUsageContext;

    @SuppressWarnings("nls")
    public MultilevelVisualAidEditor( final boolean insertMode,
                                      final GraphicalObjectCollection< MultilevelVisualAid > multilevelVisualAidCollection,
                                      final ProductBranding productBranding,
                                      final ClientProperties pClientProperties,
                                      final boolean pResetApplicable,
                                      final String projectorType,
                                      final String projectionZonesType,
                                      final String projectionZonesUsageContext ) {
        // Always call the superclass constructor first!
        super( insertMode, 
               OBJECT_TYPE, 
               "multilevelVisualAid", 
               productBranding, 
               pClientProperties,
               pResetApplicable );
        
        _multilevelVisualAidCollection = multilevelVisualAidCollection;
        
        _projectorType = projectorType;
        _projectionZonesType = projectionZonesType;
        _projectionZonesUsageContext = projectionZonesUsageContext;

        // Start with a default Multilevel Visual Aid until editing.
        _multilevelVisualAidReference = MultilevelVisualAid.getDefaultMultilevelVisualAid();

        try {
            initStage();
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    // Open the editor initialized to a mouse-selected Multilevel Visual Aid.
    public void editMultilevelVisualAid( final MultilevelVisualAid multilevelVisualAid ) {
        // Make sure an active editing session is always enabled when visible.
        setDisable( false );

        // Make sure the Layer Names are up-to-date, and that we avoid any side
        // effects against the selected Layer for the new Multilevel Visual Aid
        // Reference.
        final LayerProperties currentLayer = multilevelVisualAid.getLayer();
        updateLayerNames( currentLayer );

        // Ensure that an object already being edited doesn't reset and re-sync
        // the reference lest it flush its view for the last cached values.
        if ( _multilevelVisualAidReference.equals( multilevelVisualAid ) ) {
            return;
        }

        // Replace the current Multilevel Visual Aid reference with the one
        // selected for the Edit action when opening this window.
        setMultilevelVisualAidReference( multilevelVisualAid );

        // Sync the editor to the selected Multilevel Visual Aid.
        syncViewToModel();
    }

    public MultilevelVisualAid getMultilevelVisualAidReference() {
        return _multilevelVisualAidReference;
    }

    public String getNewMultilevelVisualAidLabelDefault() {
        // Forward this method to the Multilevel Visual Aid Pane.
        return _multilevelVisualAidPane.getNewMultilevelVisualAidLabelDefault();
    }

    private void initStage() {
        // First have the superclass initialize its content.
        initStage( "/com/fatCow/icons/Measure16.png", //$NON-NLS-1$
                   OBJECT_TYPE,
                   1200,
                   420,
                   false,
                   false,
                   false );
    }

    @Override
    protected Node loadContent() {
        // Instantiate and return the custom Content Node.
        _multilevelVisualAidPane = new MultilevelVisualAidPane( clientProperties,
                                                                _multilevelVisualAidCollection,
                                                                _projectorType,
                                                                _projectionZonesType,
                                                                _projectionZonesUsageContext );
        return _multilevelVisualAidPane;
    }

    @Override
    protected void reset() {
        // Cache the current values that we want to preserve.
        // TODO: Determine whether location is the best positional field to
        // save/restore.
        // final Point2D location = _multilevelVisualAidReference.getLocation();
        final String multilevelVisualAidLabel = _multilevelVisualAidReference.getLabel();

        // Make a default Multilevel Visual Aid to effectively reset all the
        // fields.
        final MultilevelVisualAid multilevelVisualAidDefault = MultilevelVisualAid
                .getDefaultMultilevelVisualAid();
        _multilevelVisualAidReference.setMultilevelVisualAid( multilevelVisualAidDefault );

        // Restore the fields we want to preserve.
        // _multilevelVisualAidReference.setLocation( location );
        _multilevelVisualAidReference.setLabel( multilevelVisualAidLabel );

        // Update the view to match the new model, but don't apply it yet.
        syncViewToModel();
    }

    @Override
    public void setDisable( final boolean disable ) {
        // First, disable anything that is shared as part of the parent class,
        // such as the action buttons.
        super.setDisable( disable );

        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.setDisable( disable );
    }

    public void setGesturesEnabled( final boolean gesturesEnabled ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.setGesturesEnabled( gesturesEnabled );
    }

    public void setLayerCollection( final ObservableList< LayerProperties > layerCollection ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.setLayerCollection( layerCollection );
    }

    public void setMultilevelVisualAidReference( final MultilevelVisualAid multilevelVisualAid ) {
        _multilevelVisualAidReference = multilevelVisualAid;
    }

    /**
     * Set the new Scrolling Sensitivity for all of the sliders.
     *
     * @param scrollingSensitivity
     *            The sensitivity of the mouse scroll wheel
     */
    public void setScrollingSensitivity( final ScrollingSensitivity scrollingSensitivity ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.setScrollingSensitivity( scrollingSensitivity );
    }

    @Override
    protected void syncEditorToObjectReference() {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.syncViewToMultilevelVisualAid( _multilevelVisualAidReference );
    }

    @Override
    protected void syncObjectReferenceToEditor() {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.syncMultilevelVisualAidToView( _multilevelVisualAidReference );
    }

    public void syncToSelectedLayerName() {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.syncToSelectedLayerName( _multilevelVisualAidReference );
    }

    public void toggleGestures() {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.toggleGestures();
    }

    public void updateAngleUnit( final AngleUnit angleUnit ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.updateAngleUnit( angleUnit );

        // Make sure all displayed fields update to the new Angle Unit.
        // NOTE: We skip this if running as a modal dialog, as this change can
        // only come from the dialog not showing anyway, and can hit
        // performance.
        if ( isEditMode() ) {
            syncEditorToObjectReference();
        }
    }

    public void updateDistanceUnit( final DistanceUnit distanceUnit ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.updateDistanceUnit( distanceUnit );

        // Make sure all displayed fields update to the new Distance Unit.
        // NOTE: We skip this if running as a modal dialog, as this change can
        // only come from the dialog not showing anyway, and can hit
        // performance.
        if ( isEditMode() ) {
            syncEditorToObjectReference();
        }
    }

    public void updateLayerNames( final boolean preserveSelectedLayerByIndex,
                                  final boolean preserveSelectedLayerByName ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.updateLayerNames( preserveSelectedLayerByIndex,
                                                   preserveSelectedLayerByName );
    }

    public void updateLayerNames( final LayerProperties currentLayer ) {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.updateLayerNames( currentLayer );
    }

    // TODO: Verify whether we need to synchronize both positions.
    @Override
    public void updatePositioning() {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.updatePositioning( _multilevelVisualAidReference );
    }

    @Override
    public void updatePreview() {
        // Forward this method to the Multilevel Visual Aid Pane.
        _multilevelVisualAidPane.updatePreview( _multilevelVisualAidReference );
    }

}
