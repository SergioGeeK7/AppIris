package com.example.sergiogeek7.appiris;

import android.Manifest;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.opencv.DetectShapes;
import com.example.sergiogeek7.appiris.opencv.ShapesDetected;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFilters extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_image_filters);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }
//
        private static final String TAG = MainActivity.class.getSimpleName();
        public static final int SELECT_GALLERY_IMAGE = 101;
        public static final int LEFT_EYE = 0;
        public static final int RIGHT_EYE = 1;

        private ArrayList<Eye> eyes;
        public Eye currentEye;

        @BindView(R.id.image_preview)
        ImageView imagePreview;

        @BindView(R.id.tabs)
        TabLayout tabLayout;

        @BindView(R.id.viewpager)

        ViewPager viewPager;

        @BindView(R.id.coordinator_layout)
        CoordinatorLayout coordinatorLayout;

        @BindView(R.id.left_image)
        Button left_image;

        @BindView(R.id.right_image)
        Button right_image;

        Bitmap originalImage;
        // to backup image with filter applied
        Bitmap filteredImage;

        // the final image after applying
        // brightness, saturation, contrast
        Bitmap finalImage;

        FiltersListFragment filtersListFragment;
        EditImageFragment editImageFragment;

        // modified image values
        ImageSaveState imageSaveState = new ImageSaveState();

        public static Uri currentImagePath;

        // load native image filters library
        static {
            System.loadLibrary("NativeImageProcessor");
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
            this.currentImagePath =  this.eyes.get(LEFT_EYE).getCroped().getUri();
            this.currentEye = this.eyes.get(LEFT_EYE);
            setContentView(R.layout.activity_image_filters);
            ButterKnife.bind(this);
            left_image.setEnabled(false);
            //Toolbar toolbar = findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.activity_title_main));
            new LoadBitmap().execute(this.currentEye.getCroped().getUri(), false);
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);
        }

        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            // adding filter list fragment
            filtersListFragment = new FiltersListFragment();
            filtersListFragment.setListener(this);
            // adding edit image fragment
            editImageFragment = new EditImageFragment();
            editImageFragment.setListener(this);
            adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));
            adapter.addFragment(editImageFragment, getString(R.string.tab_edit));
            viewPager.setAdapter(adapter);
        }

        public void onLeftImage(View view){
            view.setEnabled(false);
            right_image.setEnabled(true);
            updateUI(LEFT_EYE);
        }

        public void onRightImage(View view){
            try{
                view.setEnabled(false);
                left_image.setEnabled(true);
                updateUI(RIGHT_EYE);
            }catch (Exception ex){
                Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        public void updateUI(int eyeSide){

            finalImage.recycle();
            filteredImage.recycle();

            this.currentEye.getCroped().setBitmap(this.originalImage);
            this.currentEye.getCroped().setSaveState(this.imageSaveState);
            this.currentEye = eyes.get(eyeSide);
            ImageSaveState savedState = this.imageSaveState = this.currentEye.getCroped().getSaveState();
            Bitmap original = this.currentEye.getCroped().getBitmap();

            if(original == null){
                new LoadBitmap().execute(this.currentEye.getCroped().getUri() , true);
            }else{
                Bitmap filtered = applyFilter(savedState,
                        original.copy(Bitmap.Config.ARGB_8888, true));

                filtersListFragment.prepareThumbnail(original);
                originalImage = original;
                filteredImage = filtered;
                finalImage = filtered.copy(Bitmap.Config.ARGB_8888, true);
                imagePreview.setImageBitmap(filteredImage);
            }

        }

        private Bitmap applyFilter(ImageSaveState saveState, Bitmap original){

            if(saveState.filter != null){
                original = saveState.filter.processFilter(original);
            }
            if(saveState.isDirty()){
                Filter filter = new Filter();
                filter.addSubFilter(new BrightnessSubFilter(imageSaveState.brightness));
                filter.addSubFilter(new ContrastSubFilter(imageSaveState.contrast));
                filter.addSubFilter(new SaturationSubfilter(imageSaveState.saturation));
                original = filter.processFilter(original);
            }
            return original;
        }

        @Override
        public void onFilterSelected(Filter filter) {
            // reset image controls
            resetControls();
            imageSaveState.filter = filter;

            // applying the selected filter
            filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            // preview filtered image
            imagePreview.setImageBitmap(filter.processFilter(filteredImage));

            finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
        }

        @Override
        public void onBrightnessChanged(final int brightness) {
            imageSaveState.brightness = brightness;
            Filter myFilter = new Filter();
            myFilter.addSubFilter(new BrightnessSubFilter(brightness));
            imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }

        @Override
        public void onSaturationChanged(final float saturation) {
            imageSaveState.saturation = saturation;
            Filter myFilter = new Filter();
            myFilter.addSubFilter(new SaturationSubfilter(saturation));
            imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }

        @Override
        public void onContrastChanged(final float contrast) {
            imageSaveState.contrast = contrast;
            Filter myFilter = new Filter();
            myFilter.addSubFilter(new ContrastSubFilter(contrast));
            imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
        }

        @Override
        public void onEditStarted() {

        }

        @Override
        public void onEditCompleted() {
            // once the editing is done i.e seekbar is drag is completed,
            // apply the values on to filtered image
            final Bitmap bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
            Filter myFilter = new Filter();
            myFilter.addSubFilter(new BrightnessSubFilter(imageSaveState.brightness));
            myFilter.addSubFilter(new ContrastSubFilter(imageSaveState.contrast));
            myFilter.addSubFilter(new SaturationSubfilter(imageSaveState.saturation));
            finalImage.recycle();
            finalImage = myFilter.processFilter(bitmap);
        }

        /**
         * Resets image edit controls to normal when new filter
         * is selected
         */
        private void resetControls() {
            if (editImageFragment != null) {
                editImageFragment.resetControls();
            }
            imageSaveState.reset();
        }

        class ViewPagerAdapter extends FragmentPagerAdapter {
            private final List<Fragment> mFragmentList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public ViewPagerAdapter(FragmentManager manager) {
                super(manager);
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            public void addFragment(Fragment fragment, String title) {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.action_open) {
                openImageFromGallery();
                return true;
            }

            if (id == R.id.action_save) {
                saveImageToGallery();
                return true;
            }

            if (id == R.id.action_analyze) {
                goDetectActivity();
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == SELECT_GALLERY_IMAGE && resultCode == RESULT_OK && data != null
                    && data.getData() != null) {

                finalImage.recycle();
                //filteredImage.recycle();
                //originalImage.recycle();
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                Uri uri = data.getData();
                new LoadBitmap().execute(uri, true);
            }
        }

        private void openImageFromGallery() {
            Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_a_picture)), SELECT_GALLERY_IMAGE);
                            } else {
                                Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();
        }

        private void goDetectActivity(){
            BitmapUtils.saveBitmap(this, filteredImage, this.currentEye.getCroped().getUri());
            //filteredImage.recycle();
            Eye eye = this.eyes.get(right_image.isEnabled() ? RIGHT_EYE : LEFT_EYE);
            Bitmap original = eye.getCroped().getBitmap();
            if(original != null){
                Bitmap filtered = applyFilter(eye.getCroped().getSaveState(), original);
                BitmapUtils.saveBitmap(this, filtered, eye.getCroped().getUri());
                //filtered.recycle();
            }
            Intent intent = new Intent(this, DetectActivity.class);
            intent.putParcelableArrayListExtra(ViewImage.EYE_PARCELABLE, eyes);
            startActivity(intent);
        }

        /*
        * saves image to camera gallery
        * */
        private void saveImageToGallery() {
            Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                final String path = BitmapUtils.insertImage(getContentResolver(), finalImage, System.currentTimeMillis() + "_profile.jpg", null);
                                if (!TextUtils.isEmpty(path)) {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Image saved to gallery!", Snackbar.LENGTH_LONG)
                                            .setAction("OPEN", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    openImage(path);
                                                }
                                            });

                                    snackbar.show();
                                } else {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout, "Unable to save image!", Snackbar.LENGTH_LONG);

                                    snackbar.show();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Permissions are not granted!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        }

        // opening image in default image viewer app
        private void openImage(String path) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(path), "image/*");
            startActivity(intent);
        }

    private class LoadBitmap extends AsyncTask<Object, Void, Bitmap> {

        protected Bitmap doInBackground(Object... params) {
            Bitmap bitmap = null;
            Uri uri = (Uri)params[0];
            boolean reloadThumbnail = (boolean) params[1];
            try{
                bitmap = Picasso.with(ImageFilters.this)
                        .load(uri)
                        .resize(400, 400)
                        .get();
                if(reloadThumbnail){
                    filtersListFragment.prepareThumbnail(bitmap);
                }
            }catch (Exception ex){
                Log.e(TAG, ex.getMessage());
            }
            return bitmap;
        }

        protected void onProgressUpdate() {
            // TODO: make a progress bar
        }

        protected void onPostExecute(Bitmap bitmap) {
            originalImage = bitmap;
            filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(originalImage);
        }
    }



}
