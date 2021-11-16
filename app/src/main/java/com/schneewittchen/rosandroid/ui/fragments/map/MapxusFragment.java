package com.schneewittchen.rosandroid.ui.fragments.map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.OnSymbolClickListener;
import com.mapbox.mapboxsdk.plugins.annotation.SymbolManager;
import com.mapxus.map.mapxusmap.api.map.MapViewProvider;
import com.mapxus.map.mapxusmap.api.map.MapxusMap;
import com.mapxus.map.mapxusmap.api.map.interfaces.OnMapxusMapReadyCallback;
import com.mapxus.map.mapxusmap.api.map.model.MapxusMarkerOptions;
import com.mapxus.map.mapxusmap.api.services.RoutePlanning;
import com.mapxus.map.mapxusmap.impl.MapboxMapViewProvider;
import com.schneewittchen.rosandroid.R;
import com.schneewittchen.rosandroid.model.entities.widgets.BaseEntity;
import com.schneewittchen.rosandroid.model.repositories.rosRepo.node.BaseData;
import com.schneewittchen.rosandroid.ui.fragments.viz.WidgetViewGroup;
import com.schneewittchen.rosandroid.ui.general.DataListener;
import com.schneewittchen.rosandroid.ui.general.WidgetChangeListener;
import com.schneewittchen.rosandroid.viewmodel.VizViewModel;
import com.schneewittchen.rosandroid.widgets.pose.PoseView;
import com.schneewittchen.rosandroid.ui.fragments.map.MapxusPoseViewGroup;
import com.schneewittchen.rosandroid.ui.fragments.map.MapxusViewModel;
import org.jetbrains.annotations.NotNull;

public class MapxusFragment extends Fragment implements OnMapReadyCallback, OnMapxusMapReadyCallback, DataListener, WidgetChangeListener {

    private RoutePlanning routePlanning = RoutePlanning.newInstance();
    private MapView mapView;
    private MapViewProvider mapViewProvider;
    private MapboxMap mapboxMap;
    private MapxusViewModel mapxusViewModel;
    private MapxusPoseViewGroup mapxusPoseViewGroup;
    private SymbolManager symbolManager;
    private OnSymbolClickListener onMapboxMarkerClickListener;
    private LocationComponentOptions mapBoxDefaultLocationComponentOptions;
    private LocationComponentActivationOptions mapBoxCustomLocationComponentActivationOptions;
    private Object mapBoxDefaultLocationComponentActivationOptions;
    private LocationComponentOptions mapBoxCustomLocationOptions;


    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mapxus_map, container, false);

        mapView = v.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        mapViewProvider = new MapboxMapViewProvider(requireActivity(), mapView);

        mapViewProvider.getMapxusMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(@NonNull @NotNull MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;

        mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.33446, 114.263551), 18), 500);

        mapboxMap.addOnCameraIdleListener(new MapboxMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                // show camera coordinate in developer mode

                LatLng latLng = mapboxMap.getCameraPosition().target;

                String latString = String.format("%.6f", latLng.getLatitude());
                String lngString = String.format("%.6f", latLng.getLongitude());

                double bearing = mapboxMap.getCameraPosition().bearing;
                double zoomLevel = mapboxMap.getCameraPosition().zoom;

                String message = String.format("Camera: %s,%s\nBearing: %f Zoom lv: %f",
                        latString, lngString, bearing, zoomLevel);

            }
        });
        mapboxMap.getStyle(style -> {

            prepareMapBoxLocationComponent(style);
            symbolManager = new SymbolManager(mapView, mapboxMap, style);
            symbolManager.setIconAllowOverlap(true);
            symbolManager.setIconIgnorePlacement(true);
            symbolManager.setIconOptional(false);
            });
        /*// click listener for mapxus marker
        this.mapboxMap.setOnMarkerClickListener(new MapboxMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull com.mapbox.mapboxsdk.annotations.Marker marker) {
                try {

                    String latString = String.format("%.6f", marker.getPosition().getLatitude());
                    String lngString = String.format("%.6f", marker.getPosition().getLongitude());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                return false;
            }
        });*/
    }
    @SuppressWarnings({"MissingPermission"})
    private void prepareMapBoxLocationComponent(@NonNull Style loadedMapStyle) {
        mapBoxDefaultLocationComponentOptions = LocationComponentOptions.builder(getActivity())
                .elevation(6)
                .backgroundTintColor(Color.WHITE)
                .build();
        mapBoxDefaultLocationComponentActivationOptions = LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                .locationComponentOptions(mapBoxDefaultLocationComponentOptions)
                .build();
        mapBoxCustomLocationOptions = LocationComponentOptions.builder(getActivity())
                .elevation(0)
                .accuracyAlpha(0.3f)
                .enableStaleState(false)
                .foregroundTintColor(Color.parseColor("#00000000"))
                //.foregroundDrawable(R.drawable.transparent_circle)
                .backgroundTintColor(Color.parseColor("#00000000"))
                //.backgroundDrawable(R.drawable.transparent_circle)
                //.foregroundTintColor(Color.parseColor("#e1c03a"))
                //.backgroundTintColor(Color.parseColor("#19b1dc"))
                //.backgroundTintColor(Color.parseColor("#e1c03a"))
                .build();
        mapBoxCustomLocationComponentActivationOptions =
                LocationComponentActivationOptions.builder(getActivity(), loadedMapStyle)
                        .locationComponentOptions(mapBoxCustomLocationOptions)
                        .build();
        //mapBoxLocationComponent = mapboxMap.getLocationComponent();
        //mapBoxLocationComponent.activateLocationComponent(customlocationComponentActivationOptions);
        //mapBoxLocationComponent.activateLocationComponent(mapBoxDefaultLocationComponentActivationOptions);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapxusPoseViewGroup = view.findViewById(R.id.mapView);
        mapxusPoseViewGroup.setDataListener(this);
        mapxusPoseViewGroup.setOnWidgetDetailsChanged(this);

        mapxusViewModel = new ViewModelProvider(this).get(MapxusViewModel.class);

        mapxusViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), widgetEntities -> {
            mapxusPoseViewGroup.setWidgets(widgetEntities);
        });

        mapxusViewModel.getData().observe(getViewLifecycleOwner(), data -> {
            mapxusPoseViewGroup.onNewData(data);
        });
    }



    @Override
    public void onDestroyView() {
        System.gc();
        super.onDestroyView();
    }


    @Override
    public void onMapxusMapReady(MapxusMap mapxusMap) {
        symbolManager.addClickListener(onMapboxMarkerClickListener);
        //this.mapxusMap = mapxusMap;
        MapxusMarkerOptions robotMarker = new MapxusMarkerOptions()
                .setPosition(new com.mapxus.map.mapxusmap.api.map.model.LatLng(22.334499, 114.263551))
                .setFloor("3F")
                .setBuildingId("31742af5bc8446acad14e0c053ae468a")//.setIcon(Integer.parseInt(d.toString()))
//                    .setIcon(R.drawable.liphy_logo_80_shadow);
                .setIcon(R.drawable.robot7);

        MapxusMarkerOptions LiPHYMarker = new MapxusMarkerOptions()
                .setPosition(new com.mapxus.map.mapxusmap.api.map.model.LatLng(22.334469, 114.263424))
                .setFloor("3F")
                .setBuildingId("31742af5bc8446acad14e0c053ae468a")
                .setIcon(R.drawable.lightbulb10);
        mapxusMap.addMarker(robotMarker);
        mapxusMap.addMarker(LiPHYMarker);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        mViewModel = new ViewModelProvider(this).get(VizViewModel.class);
//
//        mViewModel.getCurrentWidgets().observe(getViewLifecycleOwner(), widgetEntities -> {
//            widgetViewGroupview.setWidgets(widgetEntities);
//        });
//
//        mViewModel.getData().observe(getViewLifecycleOwner(), data -> {
//            widgetViewGroupview.onNewData(data);
//        });

//        vizEditModeSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
//            widgetViewGroupview.setVizEditMode(isChecked);
//        });
//
//        optionsOpenButton.setOnClickListener(v -> {
//            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
//                drawerLayout.closeDrawer(GravityCompat.END);
//            } else {
//                drawerLayout.openDrawer(GravityCompat.END);
//            }
//        });
//    }
    @Override
    public void onNewWidgetData(BaseData data) {
        mapxusViewModel.publishData(data);
    }

    @Override
    public void onWidgetDetailsChanged(BaseEntity widgetEntity) {
        mapxusViewModel.updateWidget(widgetEntity);
    }
}

