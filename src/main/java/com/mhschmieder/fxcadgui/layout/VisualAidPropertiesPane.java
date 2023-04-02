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
import com.mhschmieder.fxcadgraphics.GraphicalObject;
import com.mhschmieder.fxcadgraphics.GraphicalObjectCollection;
import com.mhschmieder.fxcadgraphics.VisualAid;
import com.mhschmieder.fxcadgui.control.VisualAidPropertiesControls;
import com.mhschmieder.fxcadgui.model.VisualAidProperties;
import com.mhschmieder.fxguitoolkit.GuiUtilities;
import com.mhschmieder.fxlayergraphics.LayerUtilities;
import com.mhschmieder.fxlayergraphics.model.LayerProperties;

import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class VisualAidPropertiesPane extends BorderPane {

    // Declare the table column header names.
    private static final String        COLUMN_HEADER_VISUAL_AID_LABEL = "Visual Aid Label";         //$NON-NLS-1$
    private static final String        COLUMN_HEADER_LAYER            = "Layer";                    //$NON-NLS-1$
    private static final String        COLUMN_HEADER_PROJECTOR_DEFAULT = "Projector";               //$NON-NLS-1$
    private static final String        COLUMN_HEADER_PROJECTION_ZONES_DEFAULT = "Projection Zones"; //$NON-NLS-1$

    // Declare static constant to use for symbolically referencing grid column
    // indices, to ensure no errors, and ease of extensibility.
    public static final int            COLUMN_FIRST                   = 0;
    public static final int            COLUMN_VISUAL_AID_LABEL        = COLUMN_FIRST;
    public static final int            COLUMN_LAYER                   = COLUMN_VISUAL_AID_LABEL + 1;
    public static final int            COLUMN_PROJECTOR               = COLUMN_LAYER + 1;
    public static final int            COLUMN_PROJECTION_ZONES        = COLUMN_PROJECTOR + 1;
    public static final int            COLUMN_LAST                    = COLUMN_PROJECTION_ZONES;
    public static final int            NUMBER_OF_COLUMNS              =
                                                         ( COLUMN_LAST - COLUMN_FIRST ) + 1;

    // Declare static constant to use for symbolically referencing grid row
    // indices, to ensure no errors, and ease of extensibility.
    public static final int            ROW_HEADER                     = 0;
    public static final int            ROW_PROPERTIES_FIRST           = ROW_HEADER + 1;
    public static final int            ROW_PROPERTIES_LAST            = ROW_PROPERTIES_FIRST;
    public static final int            ROW_LAST                       = ROW_PROPERTIES_LAST;

    // Keep track of how many unique Column Headers there are (due to spanning).
    public static final int            NUMBER_OF_COLUMN_HEADERS       = NUMBER_OF_COLUMNS;

    // Declare the main GUI nodes that are needed beyond initialization time.
    protected GridPane                 _visualAidPropertiesGrid;

    // Declare the Visual Aid Properties Controls.
    public VisualAidPropertiesControls _visualAidPropertiesControls;

    // Cache the Visual Aid Properties, for data binding.
    protected VisualAidProperties      _visualAidProperties;

    public VisualAidPropertiesPane( final ClientProperties pClientProperties,
                                    final String visualAidLabelDefault,
                                    final GraphicalObjectCollection< ? extends VisualAid > visualAidCollection ) {
        this( pClientProperties, 
              visualAidLabelDefault, 
              visualAidCollection, 
              COLUMN_HEADER_PROJECTOR_DEFAULT,
              COLUMN_HEADER_PROJECTION_ZONES_DEFAULT,
              null );
    }

    public VisualAidPropertiesPane( final ClientProperties pClientProperties,
                                    final String visualAidLabelDefault,
                                    final GraphicalObjectCollection< ? extends VisualAid > visualAidCollection,
                                    final String projectorType,
                                    final String projectionZonesType,
                                    final String projectionZonesUsageContext ) {
        // Always call the superclass constructor first!
        super();

        // Make default Visual Aid Properties to give a valid reference for
        // later updates.
        _visualAidProperties = new VisualAidProperties( visualAidLabelDefault,
                                                        LayerUtilities.DEFAULT_LAYER_NAME,
                                                        VisualAid.USE_AS_PROJECTOR_DEFAULT,
                                                        VisualAid.NUMBER_OF_PROJECTION_ZONES_DEFAULT );

        try {
            initPane( pClientProperties, 
                      visualAidLabelDefault, 
                      visualAidCollection, 
                      projectorType,
                      projectionZonesType,
                      projectionZonesUsageContext );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    private final void bindProperties() {
        // Bind the Visual Aid Properties to their respective controls.
        // NOTE: Bind the label property to our custom value property vs. the
        // editor's built-in text property, as this is designed to more reliably
        // reflect committed edits vs. incomplete or uncorrected typing.
        _visualAidPropertiesControls._visualAidLabelEditor.textProperty()
                .bindBidirectional( _visualAidProperties.labelProperty() );
        _visualAidPropertiesControls._layerSelector.valueProperty()
                .bindBidirectional( _visualAidProperties.layerNameProperty() );
        _visualAidPropertiesControls._useAsProjectorCheckBox.selectedProperty()
                .bindBidirectional( _visualAidProperties.useAsProjectorProperty() );
        _visualAidPropertiesControls._projectionZonesSelector.valueProperty()
                .bindBidirectional( _visualAidProperties.numberOfProjectionZonesProperty() );
    }

    public final String getNewVisualAidLabelDefault() {
        // Forward this method to the Visual Aid Properties Group.
        return _visualAidPropertiesControls.getNewVisualAidLabelDefault();
    }

    public final String getUniqueVisualAidLabel( final String visualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Properties Group.
        return _visualAidPropertiesControls.getUniqueVisualAidLabel( visualAidLabelCandidate );
    }

    public final VisualAidProperties getVisualAidProperties() {
        return _visualAidProperties;
    }

    private final void initPane( final ClientProperties pClientProperties,
                                 final String visualAidLabelDefault,
                                 final GraphicalObjectCollection< ? extends VisualAid > visualAidCollection,
                                 final String projectorType,
                                 final String projectionZonesType,
                                 final String projectionZonesUsageContext ) {
        // Make the grid of individual Visual Aid Properties controls.
        _visualAidPropertiesGrid = new GridPane();

        // We center the column header labels to follow Compass conventions.
        final Label visualAidLabelLabel = GuiUtilities
                .getColumnHeader( COLUMN_HEADER_VISUAL_AID_LABEL );
        final Label layerLabel = GuiUtilities.getColumnHeader( COLUMN_HEADER_LAYER );
        final Label projectorLabel = GuiUtilities.getColumnHeader( projectorType );
        final Label projectionZonesLabel = GuiUtilities.getColumnHeader( projectionZonesType );

        // Force all the labels to center within the grid.
        GridPane.setHalignment( visualAidLabelLabel, HPos.CENTER );
        GridPane.setHalignment( layerLabel, HPos.CENTER );
        GridPane.setHalignment( projectorLabel, HPos.CENTER );
        GridPane.setHalignment( projectionZonesLabel, HPos.CENTER );

        _visualAidPropertiesGrid.setPadding( new Insets( 6.0d ) );
        _visualAidPropertiesGrid.setHgap( 16d );
        _visualAidPropertiesGrid.setVgap( 2.0d );

        _visualAidPropertiesGrid.add( visualAidLabelLabel, COLUMN_VISUAL_AID_LABEL, ROW_HEADER );
        _visualAidPropertiesGrid.add( layerLabel, COLUMN_LAYER, ROW_HEADER );
        _visualAidPropertiesGrid.add( projectorLabel, COLUMN_PROJECTOR, ROW_HEADER );
        _visualAidPropertiesGrid.add( projectionZonesLabel, COLUMN_PROJECTION_ZONES, ROW_HEADER );

        // Make the individual Visual Aid Properties Controls and place in a
        // Grid.
        _visualAidPropertiesControls = new VisualAidPropertiesControls( pClientProperties,
                                                                        true,
                                                                        visualAidLabelDefault,
                                                                        visualAidCollection,
                                                                        projectorType,
                                                                        projectionZonesType,
                                                                        projectionZonesUsageContext );

        _visualAidPropertiesGrid.add( _visualAidPropertiesControls._visualAidLabelEditor,
                                      COLUMN_VISUAL_AID_LABEL,
                                      ROW_PROPERTIES_FIRST );
        _visualAidPropertiesGrid.add( _visualAidPropertiesControls._layerSelector,
                                      COLUMN_LAYER,
                                      ROW_PROPERTIES_FIRST );
        _visualAidPropertiesGrid.add( _visualAidPropertiesControls._useAsProjectorCheckBox,
                                      COLUMN_PROJECTOR,
                                      ROW_PROPERTIES_FIRST );
        _visualAidPropertiesGrid.add( _visualAidPropertiesControls._projectionZonesSelector,
                                      COLUMN_PROJECTION_ZONES,
                                      ROW_PROPERTIES_FIRST );

        // Center the grid, as that will always be the easiest on the eyes.
        _visualAidPropertiesGrid.setAlignment( Pos.CENTER );

        // Center the grid, as there are no other layout elements.
        setCenter( _visualAidPropertiesGrid );

        setPadding( new Insets( 6.0d ) );

        // Prevent small drop-lists from minimizing their width below wide
        // labels.
        _visualAidPropertiesControls._projectionZonesSelector.minWidthProperty()
                .bind( projectionZonesLabel.widthProperty() );

        // Bind the Visual Aid Properties to their respective controls.
        bindProperties();
    }

    // Find out if the candidate label is unique.
    public final boolean isVisualAidLabelUnique( final String visualAidLabelCandidate ) {
        // Forward this method to the Visual Aid Properties Group.
        return _visualAidPropertiesControls.isVisualAidLabelUnique( visualAidLabelCandidate );
    }

    public final void setLayerCollection( final ObservableList< LayerProperties > layerCollection ) {
        // Forward this method to the Visual Aid Properties Group.
        _visualAidPropertiesControls.setLayerCollection( layerCollection );
    }

    public final void syncToSelectedLayerName( final GraphicalObject visualAid ) {
        // Forward this method to the Visual Aid Properties Group.
        final String layerName = visualAid.getLayerName();
        final SingleSelectionModel< String > selectionModel =
                                                            _visualAidPropertiesControls._layerSelector
                                                                    .getSelectionModel();
        selectionModel.select( layerName );
    }

    public final void syncViewToVisualAidProperties( final VisualAid visualAid ) {
        // Sync the table to the Visual Aid Properties.
        final VisualAidProperties visualAidProperties = getVisualAidProperties();
        visualAidProperties.setLabel( visualAid.getLabel() );
        visualAidProperties.setLayerName( visualAid.getLayerName() );

        // Update the Projector values.
        visualAidProperties.setUseAsProjector( visualAid.isUseAsProjector() );
        visualAidProperties.setNumberOfProjectionZones( visualAid.getNumberOfProjectionZones() );

        // Make sure the cached editor value matches the latest saved label.
        _visualAidPropertiesControls._visualAidLabelEditor.setValue( visualAid.getLabel() );
    }

    public final void updateLayerNames( final boolean preserveSelectedLayerByIndex,
                                        final boolean preserveSelectedLayerByName ) {
        // Forward this method to the Visual Aid Properties Group.
        _visualAidPropertiesControls._layerSelector.updateLayerNames( preserveSelectedLayerByIndex,
                                                                      preserveSelectedLayerByName );
    }

    public final void updateLayerNames( final int currentSelectedIndex ) {
        updateLayerNames( currentSelectedIndex, true, false );
    }

    public final void updateLayerNames( final int currentSelectedIndex,
                                        final boolean preserveSelectedLayerByIndex,
                                        final boolean preserveSelectedLayerByName ) {
        // Forward this method to the Visual Aid Properties Group.
        _visualAidPropertiesControls._layerSelector.updateLayerNames( currentSelectedIndex,
                                                                      preserveSelectedLayerByIndex,
                                                                      preserveSelectedLayerByName );
    }

}
