/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.macinternetservices.sablebusinessdirectory;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.macinternetservices.sablebusinessdirectory.clustering.Cluster;
import com.macinternetservices.sablebusinessdirectory.clustering.ClusterManager;
import com.macinternetservices.sablebusinessdirectory.clustering.view.DefaultClusterRenderer;
import com.macinternetservices.sablebusinessdirectory.model.Business;
import com.macinternetservices.sablebusinessdirectory.clustering.ClusterItem;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates heavy customisation of the look of rendered clusters.
 */
public class MarkerClusteringActivity extends MainActivity implements ClusterManager.OnClusterClickListener<Business>,
        ClusterManager.OnClusterInfoWindowClickListener<Business>,
        ClusterManager.OnClusterItemClickListener<Business>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Business> {

    ClusterManager<Business> mClusterManager;
    private Business clickedVenueMarker;
    ArrayList<ListingsModel> locationReviewShow = new ArrayList<>();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    private int shortAnimationDuration;




    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple locations in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class BusinessRenderer extends DefaultClusterRenderer<Business> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public BusinessRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(Business business, MarkerOptions markerOptions) {
            // Draw a single business.
            // Set the info window to show their name.
            Picasso.get().load(business.profilePhoto).into(mImageView);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(business.name);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Business> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;
            Bitmap dummyBitmap = null;
            Drawable drawable;

            for (Business p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;

                try {
                    dummyBitmap = Glide.with(getApplicationContext())
                            .asBitmap()
                            .load(p.profilePhoto)
                            .into(70, 70).get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                drawable = new BitmapDrawable(getResources(), dummyBitmap);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }

            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    @Override
    public boolean onClusterClick(Cluster<Business> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().name;
        Toast.makeText(this, cluster.getSize() + " (including " + firstName + ")", Toast.LENGTH_SHORT).show();

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }


    @Override
    public void onClusterInfoWindowClick(Cluster<Business> cluster) {
        // Does nothing, but you could go to a list of the users.
        Toast.makeText(this, cluster + " (including " + cluster + ")", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onClusterItemClick(Business item) {

        getMap().setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // shows brief listing summary onclick
                clickedVenueMarker = item;

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = inflater.inflate(R.layout.custom_info_window, null);

                TextView venueNameTextView = view.findViewById(R.id.venue_name);
                venueNameTextView.setText(clickedVenueMarker.getTitle());
                //venueNameTextView.setVisibility(View.GONE);
                TextView venueCity = view.findViewById(R.id.venue_city);
                venueCity.setText(String.valueOf(clickedVenueMarker.getCity()));
                //venueCity.setVisibility(View.GONE);
                TextView venueState = view.findViewById(R.id.venue_state);
                venueState.setText(String.valueOf(clickedVenueMarker.getState()));
                //venueState.setVisibility(View.GONE);
                TextView venueSnippetTextView = view.findViewById(R.id.venue_snippet);
                venueSnippetTextView.setVisibility(View.GONE);
                TextView ratingCount = view.findViewById(R.id.tvRatingCount);
                ratingCount.setVisibility(View.GONE);
                TextView firstReview = view.findViewById(R.id.tvReviewFirst);
                firstReview.setVisibility(View.GONE);
                //RatingBar ratingBar = view.findViewById(R.id.ratingBar3);
                //ratingBar.setVisibility(View.GONE);
                ImageView featuredImage = view.findViewById(R.id.featuredImage);
                TextView ratings = view.findViewById(R.id.tvRatings);

                if(!clickedVenueMarker.getSnippet().isEmpty()){
                    venueSnippetTextView.setText(clickedVenueMarker.getSnippet());
                    venueSnippetTextView.setVisibility(View.VISIBLE);

                }
                if(clickedVenueMarker.getRatingCount().equals(0)){
                    ratingCount.setText(String.valueOf(clickedVenueMarker.getRatingCount()));
                    ratingCount.setVisibility(View.VISIBLE);
                    firstReview.setVisibility(View.VISIBLE);
                }
                if(clickedVenueMarker.getRatingCount().equals(1)) {
                    ratings.setText("Rating");
                }

                if(!clickedVenueMarker.getFeaturedImage().isEmpty()){
                    Picasso.get().load(clickedVenueMarker.getFeaturedImage()).into(featuredImage);
                } else {
                    featuredImage.setImageResource(R.mipmap.sable_logo_black_foreground);
                }
                return view;
            }
        });
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(Business item) {

       for(int i = 0; i< verticalList.size(); i++) {
            if (item.name.equals(verticalList.get(i).title)){
                locationReviewShow.add((new ListingsModel(ListingsModel.IMAGE_TYPE,
                        verticalList.get(i).id,
                        verticalList.get(i).title,
                        verticalList.get(i).link,
                        verticalList.get(i).status,
                        verticalList.get(i).category,
                        verticalList.get(i).featured,
                        verticalList.get(i).featured_image,
                        verticalList.get(i).bldgno,
                        verticalList.get(i).street,
                        verticalList.get(i).city,
                        verticalList.get(i).state,
                        verticalList.get(i).country,
                        verticalList.get(i).zipcode,
                        verticalList.get(i).latitude,
                        verticalList.get(i).longitude,
                        verticalList.get(i).rating,
                        verticalList.get(i).ratingCount,
                        verticalList.get(i).phone,
                        verticalList.get(i).email,
                        verticalList.get(i).website,
                        verticalList.get(i).twitter,
                        verticalList.get(i).facebook,
                        verticalList.get(i).video,
                        //verticalList.get(i).hours,
                        //verticalList.get(i).isOpen,
                        verticalList.get(i).logo,
                        verticalList.get(i).content,
                        verticalList.get(i).featured_image,
                        verticalList.get(i).content,
                        new SimpleGeofence(verticalList.get(i).title, verticalList.get(i).latitude, verticalList.get(i).longitude,
                                100, GEOFENCE_EXPIRATION_IN_MILLISECONDS, verticalList.get(i).featured_image,
                                Geofence.GEOFENCE_TRANSITION_ENTER
                                        | Geofence.GEOFENCE_TRANSITION_DWELL
                                        | Geofence.GEOFENCE_TRANSITION_EXIT))));

                Intent showReviews = new Intent(getApplicationContext(), ListReviewActivity.class);

                Bundle locationReviewBundle = new Bundle();
                locationReviewBundle.putParcelableArrayList("locationReviewBundle", locationReviewShow);
                showReviews.putExtra("locationReview", locationReviewShow);
                startActivity(showReviews);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;

            }
        }
    }

   @Override
    public void setMarkers() {
       shortAnimationDuration = getResources().getInteger(
               android.R.integer.config_shortAnimTime);
        if(mapLocations.size() == 0) {
                showOtherStuff();
                getMap().animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 200));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } else {
            getMap().clear();
            mClusterManager = new ClusterManager<>(this, getMap());
            //mClusterManager.clearItems();
            mClusterManager.setRenderer(new BusinessRenderer());
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);
            mClusterManager.addItems(mapLocations);
            mClusterManager.cluster();

            getMap().setOnCameraIdleListener(mClusterManager);
            getMap().setOnMarkerClickListener(mClusterManager);
            getMap().setOnInfoWindowClickListener(mClusterManager);

            LatLngBounds bounds = MainActivity.latLngBoundsBuilder.build();
            getMap().setOnMapLoadedCallback(() -> getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200)));
            showStuff();
        }
    }
    private void showStuff() {

        tvMore.setAlpha(0f);
        tvMore.setVisibility(View.VISIBLE);
        tvMore.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        sliderLayout.setAlpha(0f);
        sliderLayout.setVisibility(View.VISIBLE);
        sliderLayout.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        loadingLayout.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingLayout.setVisibility(View.GONE);
                    }
                });
        noListingsImageView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        noListingsLayout.setVisibility(View.GONE);
                    }
                });
        noListingsTextView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        noListingsTextView.setVisibility(View.GONE);
                    }
                });

        spinner.setVisibility(View.GONE); //hide progressBar
        loginButton.setVisibility(View.VISIBLE);
    }

    @SuppressLint("SetTextI18n")
    private void showOtherStuff() {

        Animation imgAnimationOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
        Animation imgAnimationIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        spinner.setVisibility(View.GONE); //hide progressBar
        loginButton.setVisibility(View.VISIBLE);
        loadingLayout.setAnimation(imgAnimationOut);
        loadingLayout.setVisibility(View.GONE);
        noListingsImageView.setAnimation(imgAnimationIn);
        noListingsImageView.setVisibility(View.VISIBLE);
        noListingsTextView.setAnimation(imgAnimationIn);
        noListingsTextView.setVisibility(View.VISIBLE);
        //noListingsTextView.setTextSize(16);

        if(isLoggedIn) {
           noListingsTextView.setText("This is terrible "+firstName+"!!!!\n\nLooks like there aren't any black owned businesses near you in our directory.\n" +
                    "Tap Add (+) from the menu to add any black owned business you visit to our directory.");
        } else {
            noListingsTextView.setText("This is terrible!!!!\n\nLooks like there aren't any black owned businesses near you in our directory.\n" +
                    "Tap add (+) to add any black owned business you visit to our directory.");
        }
    }
}
