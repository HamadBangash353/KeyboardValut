package com.example.keyboardvalut.data;


import com.example.keyboardvalut.R;
import com.example.keyboardvalut.models.DrawerMenuModel;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenuData {

    public List<DrawerMenuModel> getMenuList() {
        List<DrawerMenuModel> list = new ArrayList<>();
        list.add(new DrawerMenuModel("Home", R.drawable.ic_home));
        list.add(new DrawerMenuModel("Rate us", R.drawable.ic_rate_us));
        list.add(new DrawerMenuModel("My vault", R.drawable.ic_my_vault));
        list.add(new DrawerMenuModel("Vault notes", R.drawable.ic_vault_notes));
        list.add(new DrawerMenuModel("Vault camera", R.drawable.ic_vault_camera));
        list.add(new DrawerMenuModel("Vault settings", R.drawable.ic_vault_settings));


        return list;

    }
}
