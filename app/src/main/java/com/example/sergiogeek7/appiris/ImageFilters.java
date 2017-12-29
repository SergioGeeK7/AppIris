package com.example.sergiogeek7.appiris;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
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
        public static Eye currentEye;

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


        // load native image filters library
        static {
            System.loadLibrary("NativeImageProcessor");
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
            this.currentEye = this.eyes.get(LEFT_EYE);
            setContentView(R.layout.activity_image_filters);
            ButterKnife.bind(this);
            left_image.setEnabled(false);
            //Toolbar toolbar = findViewById(R.id.toolbar);
            //setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.activity_title_main));
            loadImage(null);
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
                Toast.makeText(this,ex.getMessage(),Toast.LENGTH_SHORT);
            }
        }

        public void updateUI(int eyeSide){

            this.currentEye.getCroped().setBitmap(this.originalImage);
            this.currentEye.getCroped().setSaveState(this.imageSaveState);
            this.currentEye = eyes.get(eyeSide);
            ImageSaveState savedState = this.imageSaveState = this.currentEye.getCroped().getSaveState();
            Bitmap original = this.currentEye.getCroped().getBitmap();

            if(original == null){
                loadImage(null);
                return;
            }

            Bitmap savedFilter = original.copy(Bitmap.Config.ARGB_8888, true);
            if(savedState.filter != null){
                savedFilter = savedState.filter.processFilter(savedFilter.copy(Bitmap.Config.ARGB_8888, true));
            }
            if(savedState.isDirty()){
                Filter filter = new Filter();
                filter.addSubFilter(new BrightnessSubFilter(imageSaveState.brightness));
                filter.addSubFilter(new ContrastSubFilter(imageSaveState.contrast));
                filter.addSubFilter(new SaturationSubfilter(imageSaveState.saturation));
                savedFilter = filter.processFilter(savedFilter.copy(Bitmap.Config.ARGB_8888, true));
            }
            restoreFilter(original, savedFilter);
            //loadImage(this.currentEye.getCroped().getBitmap());
            //filtersListFragment.prepareThumbnail(this.currentEye.getCroped().getBitmap());
        }


        private void restoreFilter(Bitmap original, Bitmap savedFilter){
            filtersListFragment.prepareThumbnail(original);
            originalImage = original;
            filteredImage = savedFilter.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = savedFilter.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(filteredImage);
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

        // load the default image from assets on app launch
        private void loadImage(Bitmap bitmap) {
            //originalImage = BitmapUtils.getBitmapFromAssets(this, ViewImage.eyes.peek().getOriginal().getAbsoletePath(), 300, 300);
            originalImage = bitmap != null ? bitmap :
                    BitmapUtils.resamplePic(this, this.currentEye.getCroped().getAbsoletePath());
            filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
            imagePreview.setImageBitmap(originalImage);
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
            if (resultCode == RESULT_OK && requestCode == SELECT_GALLERY_IMAGE) {
                Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);

                // clear bitmap memory
                originalImage.recycle();
                finalImage.recycle();
                finalImage.recycle();

                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                filteredImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
                finalImage = originalImage.copy(Bitmap.Config.ARGB_8888, true);
                imagePreview.setImageBitmap(originalImage);
                bitmap.recycle();

                // render selected image thumbnails
                filtersListFragment.prepareThumbnail(originalImage);
            }
        }

        private void openImageFromGallery() {
            Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, SELECT_GALLERY_IMAGE);
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
            updateUI(right_image.isEnabled() ? RIGHT_EYE : LEFT_EYE);
            BitmapUtils.saveBitmap(this, filteredImage, this.currentEye.getCroped().getUri());
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


}
