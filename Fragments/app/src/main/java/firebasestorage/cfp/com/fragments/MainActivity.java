package firebasestorage.cfp.com.fragments;

import android.support.annotation.IdRes;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentManager FM= new FragmentManager() {
            @Override
            public FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public Fragment findFragmentByTag(String tag) {
                return null;
            }

            @Override
            public void popBackStack() {

            }

            @Override
            public boolean popBackStackImmediate() {
                return false;
            }

            @Override
            public void popBackStack(String name, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(String name, int flags) {
                return false;
            }

            @Override
            public void popBackStack(int id, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(int id, int flags) {
                return false;
            }

            @Override
            public int getBackStackEntryCount() {
                return 0;
            }

            @Override
            public BackStackEntry getBackStackEntryAt(int index) {
                return null;
            }

            @Override
            public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void putFragment(Bundle bundle, String key, Fragment fragment) {

            }

            @Override
            public Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<Fragment> getFragments() {
                return null;
            }

            @Override
            public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {

            }

            @Override
            public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {

            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }
        };
        firebasestorage.cfp.com.fragments.Fragment newfragment= new firebasestorage.cfp.com.fragments.Fragment();
        final int commit = FM.beginTransaction().add(newfragment,"").commit();
        //FM.add
        //FM.commit();


    }
}
