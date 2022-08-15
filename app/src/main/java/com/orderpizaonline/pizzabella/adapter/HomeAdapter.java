package com.orderpizaonline.pizzabella.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.orderpizaonline.pizzabella.fragments.BaveragesFragment;
import com.orderpizaonline.pizzabella.fragments.BurgSandFragment;
import com.orderpizaonline.pizzabella.fragments.ChineseCornerFragment;
import com.orderpizaonline.pizzabella.fragments.DealsFragment;
import com.orderpizaonline.pizzabella.fragments.FriedRollFragment;
import com.orderpizaonline.pizzabella.fragments.MealsFragment;
import com.orderpizaonline.pizzabella.fragments.PizzaFragment;
import com.orderpizaonline.pizzabella.fragments.SummerFragment;
import com.orderpizaonline.pizzabella.fragments.WinterFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public HomeAdapter(FragmentManager m){
        super(m);
    }
    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new PizzaFragment();
                break;
            case 1:
                fragment=new DealsFragment();
                break;
            case 2:
                fragment=new MealsFragment();
                break;
            case 3:
                fragment=new BurgSandFragment();
                break;
            case 4:
                fragment=new FriedRollFragment();
                break;
            case 5:
                fragment=new ChineseCornerFragment();
                break;
            case 6:
                fragment=new BaveragesFragment();
                break;
            default:
                fragment = new Fragment();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 7;
    }
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "PIZZA";
            case 1:
                return "DEALS";
            case 2:
                return "MEALS";

            case 3:
                return "BURGER/SANDWICHES";
            case 4:
                return "FRIES/ROLL";
            case 5:
                return "KIDS MANIA";
            case 6:
                return "BEVERAGES";
            case 7:
                return "SUMMER SPECIAL";
            case 8:
                return "WINTER SPECIAL";

            default:
                return null;
        }
    }
}
