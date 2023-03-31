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
package com.mhschmieder.fxcadgui.layout;

import com.mhschmieder.commonstoolkit.util.ClientProperties;
import com.mhschmieder.fxcadgraphics.ArchitecturalVisualAid;
import com.mhschmieder.fxcadgraphics.GraphicalObjectCollection;
import com.mhschmieder.fxcadgui.model.VisualAidProperties;
import com.mhschmieder.fxguitoolkit.ScrollingSensitivity;
import com.mhschmieder.fxlayergraphics.LayerUtilities;
import com.mhschmieder.fxlayergraphics.model.LayerProperties;
import com.mhschmieder.physicstoolkit.AngleUnit;
import com.mhschmieder.physicstoolkit.DistanceUnit;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public final class ArchitecturalVisualAidPane extends VBox {

    public VisualAidPropertiesPane             _visualAidPropertiesPane;
    public ArchitecturalVisualAidPlacementPane _architecturalVisualAidPlacementPane;

    /** Layer Collection reference. */
    private ObservableList< LayerProperties >  _layerCollection;

    /** Client Properties (System Type, Locale, etc.). */
    public ClientProperties                  _clientProperties;

    public ArchitecturalVisualAidPane( final ClientProperties pClientProperties,
                                       final GraphicalObjectCollection< ArchitecturalVisualAid > architecturalVisualAidCollection ) {
        // Always call the superclass constructor first!
        super();

        _clientProperties = pClientProperties;

        // Avoid chicken-or-egg null pointer problems during startup.
        _layerCollection = LayerUtilities.makeLayerCollection();

        try {
            initPane( architecturalVisualAidCollection );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    public String getNewArchitecturalVisualAidLabelDefault() {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane.getNewVisualAidLabelDefault();
    }

    public String getUniqueArchitecturalVisualAidLabel( final String architecturalVisualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane
                .getUniqueVisualAidLabel( architecturalVisualAidLabelCandidate );
    }

    public VisualAidProperties getVisualAidProperties() {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane.getVisualAidProperties();
    }

    private void initPane( final GraphicalObjectCollection< ArchitecturalVisualAid > architecturalVisualAidCollection ) {
        final String architecturalVisualAidLabelDefault = ArchitecturalVisualAid
                .getArchitecturalVisualAidLabelDefault();
        _visualAidPropertiesPane = new VisualAidPropertiesPane( _clientProperties,
                                                                architecturalVisualAidLabelDefault,
                                                                architecturalVisualAidCollection );

        _architecturalVisualAidPlacementPane =
                                             new ArchitecturalVisualAidPlacementPane( _clientProperties );

        setSpacing( 12 );
        setPadding( new Insets( 6 ) );

        final ObservableList< Node > layout = getChildren();
        layout.addAll( _visualAidPropertiesPane, _architecturalVisualAidPlacementPane );

        // Make sure the Placement Pane always gets grow priority.
        VBox.setVgrow( _architecturalVisualAidPlacementPane, Priority.ALWAYS );

        // If the Listener Plane status changes in any way, update the Preview.
        _visualAidPropertiesPane._visualAidPropertiesControls._useAsListenerPlaneCheckBox
                .selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                } );
        _visualAidPropertiesPane._visualAidPropertiesControls._targetZonesSelector
                .setOnAction( evt -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                } );

        // Make sure that any edits to one end position control affect the
        // others, so that the two coordinate systems are always in sync.
        // NOTE: We also update the model if the start position changes, in
        // order to get the preview to update.
        // NOTE: We make a dummy object to serve as an intermediary for now,
        // until we move the coordinate system transform code to a utility
        // class, so that we don't prematurely apply changes and prevent
        // reversion to a previous state.
        _architecturalVisualAidPlacementPane._startCartesianPositionPane._xPositionEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                } );
        _architecturalVisualAidPlacementPane._startCartesianPositionPane._yPositionEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                } );
        _architecturalVisualAidPlacementPane._endPositionPane._cartesianPositionPane._xPositionEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                    syncViewToArchitecturalVisualAid( architecturalVisualAid );
                } );
        _architecturalVisualAidPlacementPane._endPositionPane._cartesianPositionPane._yPositionEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                    syncViewToArchitecturalVisualAid( architecturalVisualAid );
                } );
        _architecturalVisualAidPlacementPane._endPositionPane._polarPositionPane._anglePane._angleEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                    syncViewToArchitecturalVisualAid( architecturalVisualAid );
                } );
        _architecturalVisualAidPlacementPane._endPositionPane._polarPositionPane._anglePane._angleSlider
                .valueProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                    syncViewToArchitecturalVisualAid( architecturalVisualAid );
                } );
        _architecturalVisualAidPlacementPane._endPositionPane._polarPositionPane._distanceEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final ArchitecturalVisualAid architecturalVisualAid =
                                                                        new ArchitecturalVisualAid();
                    syncArchitecturalVisualAidToView( architecturalVisualAid );
                    syncViewToArchitecturalVisualAid( architecturalVisualAid );
                } );
    }

    public boolean isArchitecturalVisualAidLabelUnique( final String architecturalVisualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane
                .isVisualAidLabelUnique( architecturalVisualAidLabelCandidate );
    }

    public void saveEdits() {
        // NOTE: We only need to save edits in non-bean-based components.
        _architecturalVisualAidPlacementPane.saveEdits();
    }

    public void setGesturesEnabled( final boolean gesturesEnabled ) {
        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.setGesturesEnabled( gesturesEnabled );
    }

    public void setLayerCollection( final ObservableList< LayerProperties > layerCollection ) {
        // Cache a local copy of the Layer Collection.
        _layerCollection = layerCollection;

        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.setLayerCollection( layerCollection );
    }

    /**
     * Set the new Scrolling Sensitivity for all of the sliders.
     *
     * @param scrollingSensitivity
     *            The sensitivity of the mouse scroll wheel
     */
    public void setScrollingSensitivity( final ScrollingSensitivity scrollingSensitivity ) {
        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.setScrollingSensitivity( scrollingSensitivity );
    }

    public void syncArchitecturalVisualAidToView( final ArchitecturalVisualAid architecturalVisualAid ) {
        // Get all of the Visual Aid properties.
        final VisualAidProperties visualAidProperties = getVisualAidProperties();
        architecturalVisualAid.setLabel( visualAidProperties.getLabel() );

        // Cache the current Layer selection via Layer Name lookup.
        final String layerName = visualAidProperties.getLayerName();
        final LayerProperties layer = LayerUtilities.getLayerByName( _layerCollection, layerName );
        architecturalVisualAid.setLayer( layer );

        // Update the Listener Plane values.
        architecturalVisualAid.setUseAsListenerPlane( visualAidProperties.isUseAsListenerPlane() );
        architecturalVisualAid
                .setNumberOfTargetZones( visualAidProperties.getNumberOfTargetZones() );

        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane
                .syncArchitecturalVisualAidToView( architecturalVisualAid );
    }

    public void syncToSelectedLayerName( final ArchitecturalVisualAid architecturalVisualAid ) {
        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.syncToSelectedLayerName( architecturalVisualAid );
    }

    public void syncViewToArchitecturalVisualAid( final ArchitecturalVisualAid architecturalVisualAid ) {
        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.syncViewToVisualAidProperties( architecturalVisualAid );

        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane
                .syncViewToArchitecturalVisualAid( architecturalVisualAid );
    }

    public void toggleGestures() {
        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.toggleGestures();
    }

    public void updateAngleUnit( final AngleUnit angleUnit ) {
        // Forward this method to the Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.updateAngleUnit( angleUnit );
    }

    public void updateDistanceUnit( final DistanceUnit distanceUnit ) {
        // Forward this method to the Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.updateDistanceUnit( distanceUnit );
    }

    public void updateLayerNames( final boolean preserveSelectedLayerByIndex,
                                  final boolean preserveSelectedLayerByName ) {
        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.updateLayerNames( preserveSelectedLayerByIndex,
                                                   preserveSelectedLayerByName );
    }

    public void updateLayerNames( final LayerProperties currentLayer ) {
        final ObservableList< LayerProperties > layerCollection = _layerCollection;
        final int currentLayerIndex = LayerUtilities.getLayerIndex( layerCollection, currentLayer );

        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.updateLayerNames( currentLayerIndex );
    }

    public void updatePositioning( final ArchitecturalVisualAid architecturalVisualAid ) {
        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.updatePositioning( architecturalVisualAid );
    }

    public void updatePreview( final ArchitecturalVisualAid architecturalVisualAidCurrent ) {
        // Forward this method to the Architectural Visual Aid Placement Pane.
        _architecturalVisualAidPlacementPane.updatePreview( architecturalVisualAidCurrent );
    }

}
