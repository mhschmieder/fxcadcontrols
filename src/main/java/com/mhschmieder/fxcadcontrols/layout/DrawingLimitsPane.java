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
package com.mhschmieder.fxcadcontrols.layout;

import com.mhschmieder.fxcadgraphics.DrawingLimits;
import com.mhschmieder.fxcontrols.GuiUtilities;
import com.mhschmieder.fxgraphics.geometry.Extents2D;
import com.mhschmieder.fxphysics.layout.ExtentsPane;
import com.mhschmieder.jcommons.util.ClientProperties;
import com.mhschmieder.jphysics.DistanceUnit;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;

public final class DrawingLimitsPane extends GridPane {

    public CheckBox         _autoSyncCheckBox;
    public ExtentsPane      _extentsPane;

    // Cache a reference to the global Drawing Limits.
    protected DrawingLimits drawingLimits;

    // Cache a reference to the Auto-Sync Boundary.
    protected Extents2D     autoSyncBoundary;

    public DrawingLimitsPane( final ClientProperties pClientProperties,
                              final String autoSyncLabel,
                              final boolean initialAutoSync,
                              final double extentsSizeMinimumMeters,
                              final double extentsSizeMaximumMeters,
                              final String propertiesCategory ) {
        // Always call the superclass constructor first!
        super();

        try {
            initPane( pClientProperties,
                      autoSyncLabel,
                      initialAutoSync,
                      extentsSizeMinimumMeters,
                      extentsSizeMaximumMeters,
                      propertiesCategory );
        }
        catch ( final Exception ex ) {
            ex.printStackTrace();
        }
    }

    private void bindProperties() {
        // Load the event handler for the Auto-Sync Check Box.
        _autoSyncCheckBox.selectedProperty()
                .addListener( ( observableValue,
                                oldValue,
                                newValue ) -> setDrawingLimitsToAutoSyncBoundary( newValue )

                );

        // The Auto-Sync flag is a simple boolean so can be bi-directionally
        // bound to its corresponding check box.
        _autoSyncCheckBox.selectedProperty().bindBidirectional( drawingLimits.autoSyncProperty() );

        // Bind Extents Pane enablement to the associated Auto-Sync Check Box.
        _extentsPane.disableProperty().bind( _autoSyncCheckBox.selectedProperty() );
    }

    private void initPane( final ClientProperties pClientProperties,
                           final String autoSyncLabel,
                           final boolean initialAutoSync,
                           final double extentsSizeMinimumMeters,
                           final double extentsSizeMaximumMeters,
                           final String propertiesCategory ) {
        _autoSyncCheckBox = GuiUtilities.getCheckBox( autoSyncLabel, initialAutoSync );

        _extentsPane = new ExtentsPane( pClientProperties,
                                        extentsSizeMinimumMeters,
                                        extentsSizeMaximumMeters,
                                        propertiesCategory );

        setHgap( 6.0d );
        setVgap( 6.0d );

        add( _autoSyncCheckBox, 0, 0 );
        add( _extentsPane, 0, 1 );

        setAlignment( Pos.CENTER );
        setPadding( new Insets( 6.0d ) );
    }

    public void setAutoSyncBoundary( final Extents2D pAutoSyncBoundary ) {
        // Cache the Auto-Sync Boundary reference.
        autoSyncBoundary = pAutoSyncBoundary;

        // Conditionally set the Drawing Limits to match the updated Auto-Sync
        // Boundary.
        setDrawingLimitsToAutoSyncBoundary( drawingLimits.isAutoSync() );
    }

    // Set and bind the Drawing Limits reference.
    // NOTE: This should be done only once, to avoid breaking bindings.
    public void setDrawingLimits( final DrawingLimits pDrawingLimits ) {
        // Cache the Drawing Limits reference.
        drawingLimits = pDrawingLimits;

        // Forward this reference to the Extents Pane.
        _extentsPane.setExtents( pDrawingLimits );

        // Bind the data model to the respective GUI components.
        bindProperties();
    }

    /*
     * Conditionally set the Drawing Limits to match the current Auto-Sync
     * Boundary.
     * <p>
     * TODO: Switch to unidirectional binding and unbinding of the four
     * individual boundary properties instead? This is easy to try and to test.
     */
    protected void setDrawingLimitsToAutoSyncBoundary( final boolean autoSync ) {
        if ( ( drawingLimits != null ) && ( autoSyncBoundary != null ) ) {
            if ( autoSync ) {
                // Check to see if anything changed, as the Rectangle class was
                // implemented by Oracle to automatically set its dirty flag
                // when its property setters are called, even if no change.
                if ( ( drawingLimits.getX() != autoSyncBoundary.getX() )
                        || ( drawingLimits.getY() != autoSyncBoundary.getY() )
                        || ( drawingLimits.getWidth() != autoSyncBoundary.getWidth() )
                        || ( drawingLimits.getHeight() != autoSyncBoundary.getHeight() ) ) {
                    drawingLimits.setExtents( autoSyncBoundary.getX(),
                                              autoSyncBoundary.getY(),
                                              autoSyncBoundary.getWidth(),
                                              autoSyncBoundary.getHeight() );
                }
            }
        }
    }

    /*
     * Propagate the new Distance Unit to the subcomponents.
     */
    public void updateDistanceUnit( final DistanceUnit distanceUnit ) {
        // Forward this method to the Extents Pane.
        _extentsPane.updateDistanceUnit( distanceUnit );
    }
}
