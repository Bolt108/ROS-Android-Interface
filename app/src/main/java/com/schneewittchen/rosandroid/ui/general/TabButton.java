package com.schneewittchen.rosandroid.ui.general;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentTransaction;

import com.schneewittchen.rosandroid.R;

import com.schneewittchen.rosandroid.ui.fragments.map.MapxusFragment;
import com.schneewittchen.rosandroid.ui.fragments.master.MasterFragment;
import com.schneewittchen.rosandroid.ui.fragments.ssh.SshFragment;

public class TabButton {

    public final static String TAG = "TabButton";

    public Button btn;

    public TabButton(Button initButton) {btn = initButton;}

    public Button get() {return btn;}
    public void set(Button setButton) {btn = setButton;}

    public void linkToFragment(final int FragmentType, final FragmentTransaction ft) {
        /*
        FragmentType : FragmentName
        0 : HomeFragment
        1 : MapxusFragment
        2 : SettingsFragment
        3 : MasterFragment
        4 : SshFragment
        5 : SmartHomeControlFragment
        6 : RobotArmFragment
        7 : ManualControlFragment
         */

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (FragmentType) {
                    case 0:
                        ft.replace(R.id.main_container, new MapxusFragment());
                        break;
                    case 1:
                        ft.replace(R.id.main_container, new MasterFragment());
                        break;
                    case 2:
                        ft.replace(R.id.main_container, new SshFragment());
                        break;
                    default:
                        Log.e(TAG, "onClick: Fragment type invalid. Tried" + FragmentType);
                }
                ft.commit();
            }
        });
    }

}
