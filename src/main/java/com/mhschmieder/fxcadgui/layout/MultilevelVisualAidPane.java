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
import com.mhschmieder.fxcadgraphics.GraphicalObjectCollection;
import com.mhschmieder.fxcadgraphics.MultilevelVisualAid;
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

public final class MultilevelVisualAidPane extends VBox {

    public VisualAidPropertiesPane            _visualAidPropertiesPane;
    public MultilevelVisualAidPlacementPane   _multilevelVisualAidPlacementPane;

    /** Layer Collection reference. */
    private ObservableList< LayerProperties > _layerCollection;

    /** Client Properties(System Type, Locale, etc.). */
    public ClientProperties                 _clientProperties;

    public MultilevelVisualAidPane( final ClientProperties pClientProperties,
                                    final GraphicalObjectCollection< MultilevelVisualAid > multilevelVisualAidCollection,
                                    final String targetPlaneType ) {
        // Always call the superclass constructor first!
        super();

        _clientProperties = pClientProperties;

        // Avoid chicken-or-egg null pointer problems during startup.
        _layerCollection = LayerUtilities.makeLayerCollection();

        try {
            initPane( multilevelVisualAidCollection, targetPlaneType );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    public String getNewMultilevelVisualAidLabelDefault() {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane.getNewVisualAidLabelDefault();
    }

    public String getUniqueMultilevelVisualAidLabel( final String multilevelVisualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane
                .getUniqueVisualAidLabel( multilevelVisualAidLabelCandidate );
    }

    public VisualAidProperties getVisualAidProperties() {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane.getVisualAidProperties();
    }

    private void initPane( final GraphicalObjectCollection< MultilevelVisualAid > multilevelVisualAidCollection,
                           final String targetPlaneType) {
        final String multilevelVisualAidLabelDefault = MultilevelVisualAid
                .getMultilevelVisualAidLabelDefault();
        _visualAidPropertiesPane = new VisualAidPropertiesPane( _clientProperties,
                                                                multilevelVisualAidLabelDefault,
                                                                multilevelVisualAidCollection,
                                                                targetPlaneType );

        _multilevelVisualAidPlacementPane = new MultilevelVisualAidPlacementPane( _clientProperties );

        final ObservableList< Node > layout = getChildren();
        layout.addAll( _visualAidPropertiesPane, _multilevelVisualAidPlacementPane );

        setSpacing( 12 );
        setPadding( new Insets( 6 ) );

        // Make sure the Placement Pane always gets grow priority.
        VBox.setVgrow( _multilevelVisualAidPlacementPane, Priority.ALWAYS );

        // If the Target Plane status changes in any way, update the Preview.
        _visualAidPropertiesPane._visualAidPropertiesControls._useAsTargetPlaneCheckBox
                .selectedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _visualAidPropertiesPane._visualAidPropertiesControls._targetZonesSelector
                .setOnAction( evt -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );

        // Make sure that any edits to any of the coordinates or angles, update
        // the model so that the preview stays in sync.
        // NOTE: We make a dummy object to serve as an intermediary for now,
        // until we move the coordinate system transform code to a utility
        // class, so that we don't prematurely apply changes and prevent
        // reversion to a previous state.
        _multilevelVisualAidPlacementPane._inclinometerPositionPane._xPositionEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._inclinometerPositionPane._yPositionEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._startPolarPositionPane._anglePane._angleEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._startPolarPositionPane._anglePane._angleSlider
                .valueProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._startPolarPositionPane._distanceEditor.focusedProperty()
                .addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._endPolarPositionPane._anglePane._angleEditor
                .focusedProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._endPolarPositionPane._anglePane._angleSlider
                .valueProperty().addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
        _multilevelVisualAidPlacementPane._endPolarPositionPane._distanceEditor.focusedProperty()
                .addListener( ( observable, oldValue, newValue ) -> {
                    final MultilevelVisualAid multilevelVisualAid = new MultilevelVisualAid();
                    syncMultilevelVisualAidToView( multilevelVisualAid );
                } );
    }

    public boolean isMultilevelVisualAidLabelUnique( final String multilevelVisualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Properties Pane.
        return _visualAidPropertiesPane.isVisualAidLabelUnique( multilevelVisualAidLabelCandidate );
    }

    public void saveEdits() {
        // NOTE: We only need to save edits in non-bean-based components.
        _multilevelVisualAidPlacementPane.saveEdits();
    }

    public void setGesturesEnabled( final boolean gesturesEnabled ) {
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.setGesturesEnabled( gesturesEnabled );
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
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.setScrollingSensitivity( scrollingSensitivity );
    }

    public void syncMultilevelVisualAidToView( final MultilevelVisualAid multilevelVisualAid ) {
        // Get all of the Visual Aid Properties.
        final VisualAidProperties visualAidProperties = getVisualAidProperties();
        multilevelVisualAid.setLabel( visualAidProperties.getLabel() );

        // Cache the current Layer selection via Layer Name lookup.
        final String layerName = visualAidProperties.getLayerName();
        final LayerProperties layer = LayerUtilities.getLayerByName( _layerCollection, layerName );
        multilevelVisualAid.setLayer( layer );

        // Update the Target Plane values.
        multilevelVisualAid.setUseAsTargetPlane( visualAidProperties.isUseAsTargetPlane() );
        multilevelVisualAid.setNumberOfTargetZones( visualAidProperties.getNumberOfTargetZones() );

        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.syncMultilevelVisualAidToView( multilevelVisualAid );
    }

    public void syncToSelectedLayerName( final MultilevelVisualAid multilevelVisualAid ) {
        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.syncToSelectedLayerName( multilevelVisualAid );
    }

    public void syncViewToMultilevelVisualAid( final MultilevelVisualAid multilevelVisualAid ) {
        // Forward this method to the Visual Aid Properties Pane.
        _visualAidPropertiesPane.syncViewToVisualAidProperties( multilevelVisualAid );

        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.syncViewToMultilevelVisualAid( multilevelVisualAid );
    }

    public void toggleGestures() {
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.toggleGestures();
    }

    public void updateAngleUnit( final AngleUnit angleUnit ) {
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.updateAngleUnit( angleUnit );
    }

    public void updateDistanceUnit( final DistanceUnit distanceUnit ) {
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.updateDistanceUnit( distanceUnit );
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

    public void updatePositioning( final MultilevelVisualAid multilevelVisualAid ) {
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.updatePositioning( multilevelVisualAid );
    }

    public void updatePreview( final MultilevelVisualAid multilevelVisualAidCurrent ) {
        // Forward this method to the Multilevel Visual Aid Placement Pane.
        _multilevelVisualAidPlacementPane.updatePreview( multilevelVisualAidCurrent );
    }

}
