package com.example.sergiogeek7.appiris;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sergiogeek7.appiris.components.ButtonIris;
import com.example.sergiogeek7.appiris.firemodel.DetectionModel;
import com.example.sergiogeek7.appiris.firemodel.EyeModel;
import com.example.sergiogeek7.appiris.firemodel.MedicalHistoryForm;
import com.example.sergiogeek7.appiris.utils.BitmapUtils;
import com.example.sergiogeek7.appiris.utils.Callback;
import com.example.sergiogeek7.appiris.utils.Message;
import com.example.sergiogeek7.appiris.utils.UserApp;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageFilters extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener {

        private static final String TAG = MainActivity.class.getSimpleName();
        public static final int LEFT_EYE = 0;
        public static final int RIGHT_EYE = 1;
        public static final int ANALYZE_WITH_EXPERT = 9;
        private static boolean onceTour = true;

        private ArrayList<Eye> eyes;
        public Eye currentEye;

        @BindView(R.id.image_preview)
        ImageView imagePreview;

    @BindView(R.id.filter_tab)
    ImageView filterTab;

    @BindView(R.id.fx_tab)
    ImageView settingsTab;

    @BindView(R.id.viewpager)

        ViewPager viewPager;

        @BindView(R.id.coordinator_layout)
        CoordinatorLayout coordinatorLayout;

        @BindView(R.id.left_image)
        ButtonIris left_image;

        @BindView(R.id.right_image)
        ButtonIris right_image;
        @BindView(R.id.analyze_with_expert)
        Button analyze_with_expert_btn;

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

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private String detectionKey;
    final FirebaseStorage storage = FirebaseStorage.getInstance();
    final StorageReference storageRef = storage.getReference("detections");
    final DatabaseReference detectionRef = database.getReference("detections");
    final DatabaseReference medicalHistory = database.getReference("medicalHistory");


    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.eyes = getIntent().getParcelableArrayListExtra(ViewImage.EYE_PARCELABLE);
            this.currentImagePath =  this.eyes.get(LEFT_EYE).getOriginal().getUri();
            this.currentEye = this.eyes.get(LEFT_EYE);
            setContentView(R.layout.activity_image_filters);
            ButterKnife.bind(this);
            left_image.setEnabled(false);
            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
            new LoadBitmap().execute(this.currentEye.getOriginal().getAbsoletePath(), false);
            setupViewPager(viewPager);
            //tabLayout.setupWithViewPager(viewPager);
            SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);
            if(sharedPreferences.getBoolean(getString(R.string.show_tour_key), true) && onceTour){
                Message.show(getString(R.string.tour_image_filter),
                        null, this);
                onceTour = false;
            }
            //Drawable d = ContextCompat.getDrawable(this, R.drawable.ic_editar_2);
        }

        public void goToSettingsTab(View v){
            viewPager.setCurrentItem(1);
            Drawable filterD = filterTab.getDrawable();
            DrawableCompat.setTint(filterD, ContextCompat.getColor(this, R.color.tab_disabled));
            Drawable settingsD = settingsTab.getDrawable();
            DrawableCompat.setTint(settingsD, ContextCompat.getColor(this, R.color.blue_sea));
        }

        public void goToFiltersTab(View v){
            viewPager.setCurrentItem(0);
            Drawable filterD = filterTab.getDrawable();
            DrawableCompat.setTint(filterD, ContextCompat.getColor(this, R.color.blue_sea));
            Drawable settingsD = settingsTab.getDrawable();
            DrawableCompat.setTint(settingsD, ContextCompat.getColor(this, R.color.tab_disabled));
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

            this.currentEye.getFilter().setBitmap(this.originalImage);
            this.currentEye.getFilter().setSaveState(this.imageSaveState);
            this.currentEye = eyes.get(eyeSide);
            ImageSaveState savedState = this.imageSaveState = this.currentEye.getFilter().getSaveState();
            Bitmap original = this.currentEye.getFilter().getBitmap();

            if(original == null){
                new LoadBitmap().execute(this.currentEye.getOriginal().getAbsoletePath() , true);
            }else{
                Bitmap filtered = applyFilter(savedState,
                        original.copy(Bitmap.Config.ARGB_8888, true));

                //RGB_565
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

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if(id == android.R.id.home){
                onBackPressed();
            }

            if (id == R.id.load_from_gallery) {
                return true;
            }

            if(id == R.id.share){
                share();
                return true;
            }
            if(id == R.id.go_to_main_screen){
                startActivity(new Intent(this, MainScreen.class));
            }
            return super.onOptionsItemSelected(item);
        }


        private void share(){
            Bitmap bitmap = BitmapUtils.addWaterMark(filteredImage, getString(R.string.app_name));
            BitmapUtils.saveBitmap(this, bitmap, this.currentEye.getFilter().getUri());
            BitmapUtils.shareImage(this, this.currentEye.getFilter().getUri());
        }

        public void goToDetectActivity(View view){
            BitmapUtils.saveBitmap(this, filteredImage, this.currentEye.getFilter().getUri());
            //filteredImage.recycle();
            Eye eye = this.eyes.get(right_image.isEnabled() ? RIGHT_EYE : LEFT_EYE);
            Bitmap original = eye.getFilter().getBitmap();
            if(original != null){
                Bitmap filtered = applyFilter(eye.getFilter().getSaveState(), original);
                BitmapUtils.saveBitmap(this, filtered, eye.getFilter().getUri());
                //filtered.recycle();
            }
            Intent intent = new Intent(this, DetectActivity.class);
            intent.putParcelableArrayListExtra(ViewImage.EYE_PARCELABLE, eyes);
            intent.putExtra(DetectionModel.class.getName(), detectionKey);
            startActivity(intent);
        }

        /*
        * saves image to camera gallery
        * */
        public void saveImageToGallery(View v) {
            Dexter.withActivity(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            if (report.areAllPermissionsGranted()) {
                                final String path = BitmapUtils.insertImage(getContentResolver(), finalImage, System.currentTimeMillis() + "_profile.jpg", null);
                                if (!TextUtils.isEmpty(path)) {
                                    Snackbar snackbar = Snackbar
                                            .make(coordinatorLayout,
                                                    R.string.image_gallery_saved, Snackbar.LENGTH_LONG)
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
            String imagePath = (String)params[0];
            boolean reloadThumbnail = (boolean) params[1];
            try{
                bitmap = BitmapUtils.compressImage(imagePath);
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

    public void analyzeWithExpert(View v){
        if(user == null){
            Toast.makeText(this, getString(R.string.require_register), Toast.LENGTH_LONG)
                    .show();
            return;
        }
        this.goToFormMedicalHistory();
    }

    void goToFormMedicalHistory(){
        Intent intent = new Intent(this, FormMedicalHistory.class);
        startActivityForResult(intent, ANALYZE_WITH_EXPERT);
    }

    public void uploadImages(Uri left, Uri right, Callback.CB2<String> callback){

        saveToFirebase(left,
                Callback.onSuccessListener(getString(R.string.uploading), leftSnap ->
                        saveToFirebase(right,
                                Callback.onSuccessListener(getString(R.string.uploading), rightSnap ->
                                                getUserApp(
                                                        Callback.valueEventListener((err, data) -> {

                                                            if(err != null){
                                                                Log.e(TAG, err.getMessage());
                                                                return;
                                                            }
                                                            UserApp userapp = data.getValue(UserApp.class);
                                                            EyeModel leftEye = new EyeModel("",
                                                                    leftSnap.getMetadata().getName());
                                                            EyeModel rightEye = new EyeModel("",
                                                                    rightSnap.getMetadata().getName());
                                                            DetectionModel detectionModel
                                                                    = new DetectionModel(leftEye, rightEye,
                                                                    new Date(), user.getUid(),
                                                                    userapp.getFullName().toLowerCase()
                                                                    , userapp.getMessagingToken());

                                                            DatabaseReference detection
                                                                    = detectionRef.push();
                                                            Callback.taskManager(this,
                                                                    detection.setValue(detectionModel));
                                                            callback.call(detection.getKey());
                                                        }, this) )
                                        , this)),this));
    }


    private void getUserApp(ValueEventListener vl){
        DatabaseReference ref_user = database.getReference("users")
                .child(user.getUid());
        ref_user.addListenerForSingleValueEvent(vl);
    }

    private void saveToFirebase(Uri uri, OnSuccessListener<UploadTask.TaskSnapshot> callback){

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String fileName = "JPEG_" + timeStamp + "_";

        UploadTask uploadTaskLeft = storageRef.child(fileName)
                .putFile(uri);
        uploadTaskLeft.addOnFailureListener(exception -> Log.e(TAG, exception.getMessage()))
                .addOnSuccessListener(callback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ANALYZE_WITH_EXPERT) {
            if(resultCode == RESULT_OK){
                analyze_with_expert_btn.setEnabled(false);
                MedicalHistoryForm mh = data.getParcelableExtra(MedicalHistoryForm.class.getName());
                uploadImages(eyes.get(ImageFilters.LEFT_EYE).getOriginal().getUri(),
                        eyes.get(ImageFilters.RIGHT_EYE).getOriginal().getUri(),
                        (key) -> {
                            saveHistory(mh, key);
                        });
            }
            //if (resultCode ==RESULT_CANCELED) {
        }
    }//onActivityResult


    void saveHistory(MedicalHistoryForm mh, String detectionKey){
        this.detectionKey = detectionKey;
        mh.setUserUId(user.getUid());
        Callback.taskManager(this,medicalHistory.child(detectionKey).setValue(mh));
        Callback.taskManager(this,database.getReference("detections")
                .child(detectionKey)
                .child("state")
                .setValue("pending"));
        Toast.makeText(this, getString(R.string.sent),
                Toast.LENGTH_LONG).show();
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

}
