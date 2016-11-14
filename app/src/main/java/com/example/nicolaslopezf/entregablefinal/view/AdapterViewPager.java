package com.example.nicolaslopezf.entregablefinal.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.nicolaslopezf.entregablefinal.view.YouTube.YouTubeFragment;
import com.example.nicolaslopezf.entregablefinal.view.viewsparafragmentinicio.FragmentRecyclerSoloImagen;

import java.util.ArrayList;

/**
 * Created by Nicolas Lopez F on 11/1/2016.
 */

public class AdapterViewPager extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentsDelAdapter;

    public void agregameUnFragment(Fragment fragment){
        fragmentsDelAdapter.add(fragment);
    }

    public AdapterViewPager(FragmentManager fm) {
        super(fm);
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new FragmentRecyclerSoloImagen());
        fragments.add(new FragmentRecycleGridFavoritas());
        fragments.add(new FragmentRecyclerUsuario());

        this.fragmentsDelAdapter = fragments;
    }

    @Override
    public Fragment getItem(int position) {

        return fragmentsDelAdapter.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsDelAdapter.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String tabTitle = null;

        switch (position){
            case 0:
                tabTitle = "Peliculas";
                break;
            case 1:
                tabTitle = "Favoritos";
                break;
            case 2:
                tabTitle = "Amigos";
        }
        return tabTitle;
    }
}
