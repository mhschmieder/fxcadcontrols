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
package com.mhschmieder.fxcadgui.model;

import com.mhschmieder.fxgraphicstoolkit.LabelAssignable;
import com.mhschmieder.fxlayergraphics.model.LayerNameAssignable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VisualAidProperties implements LabelAssignable, LayerNameAssignable {

    private final StringProperty  label;
    private final StringProperty  layerName;
    private final BooleanProperty useAsListenerPlane;
    private final StringProperty  numberOfTargetZones;

    public VisualAidProperties( final String pLabel,
                                final String pLayerName,
                                final boolean pUseAsListenerPlane,
                                final int pNumberOfTargetZones ) {
        this( pLabel, pLayerName, pUseAsListenerPlane, Integer.toString( pNumberOfTargetZones ) );
    }

    public VisualAidProperties( final String pLabel,
                                final String pLayerName,
                                final boolean pUseAsListenerPlane,
                                final String pNumberOfTargetZones ) {
        label = new SimpleStringProperty( pLabel );
        layerName = new SimpleStringProperty( pLayerName );
        useAsListenerPlane = new SimpleBooleanProperty( pUseAsListenerPlane );
        numberOfTargetZones = new SimpleStringProperty( pNumberOfTargetZones );
    }

    @Override
    public final String getLabel() {
        return label.get();
    }

    @Override
    public final String getLayerName() {
        return layerName.get();
    }

    public final int getNumberOfTargetZones() {
        return Integer.parseInt( numberOfTargetZones.get() );
    }

    public final boolean isUseAsListenerPlane() {
        return useAsListenerPlane.get();
    }

    @Override
    public final StringProperty labelProperty() {
        return label;
    }

    @Override
    public final StringProperty layerNameProperty() {
        return layerName;
    }

    public final StringProperty numberOfTargetZonesProperty() {
        return numberOfTargetZones;
    }

    @Override
    public final void setLabel( final String pLabel ) {
        label.set( pLabel );
    }

    @Override
    public final void setLayerName( final String pLayerName ) {
        layerName.set( pLayerName );
    }

    public final void setNumberOfTargetZones( final int pNumberOfTargetZones ) {
        numberOfTargetZones.set( Integer.toString( pNumberOfTargetZones ) );
    }

    public final void setNumberOfTargetZones( final String pNumberOfTargetZones ) {
        numberOfTargetZones.set( pNumberOfTargetZones );
    }

    public final void setUseAsListenerPlane( final boolean pUseAsListenerPlane ) {
        useAsListenerPlane.set( pUseAsListenerPlane );
    }

    public final BooleanProperty useAsListenerPlaneProperty() {
        return useAsListenerPlane;
    }

}
